package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;


    @FXML
    private Label totalCostLabel;

    @FXML
    private AnchorPane pane;

    @FXML
    private PieChart pieChart;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     * @param server - the server
     * @param mainCtrl - the mainCtrl
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialize the scene
     */
    public void initialize() {
        if(this.event != null) {
            // TODO: configure right currency
            totalCostLabel.setText("Total Cost of Event: " +
                String.format("%.2f", event.getExpenses()
                    .stream().mapToDouble(Expense::getAmount).sum()) + "$");
            Map<Tag, Double> distribution = getMoneyPerTag();
            pane.getChildren().remove(pieChart);
            pieChart = new PieChart();
            pane.getChildren().add(pieChart);
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            pieChart.setData(data);
            for(Tag tag : distribution.keySet()) {
                PieChart.Data section = new PieChart.Data(tag.getType(), distribution.get(tag));
                data.add(section);
            }
            List<Tag> tags = distribution.keySet().stream().toList();
            for(int i = 0; i < pieChart.getData().size(); i++) {
                pieChart.getData().get(i).getNode()
                    .setStyle("-fx-pie-color: " +
                        colorToHex(Color.web(tags.get(i).getColor())));
            }
            pieChart.setPadding(new javafx.geometry.Insets(0, 0, 100, 0));
            pieChart.setLayoutY(60);
        }
    }

    /**
     * Retrieves the amount per tag
     * @return - hashmap with the tags
     * and their corresponding amount
     */
    private Map<Tag, Double> getMoneyPerTag() {
        Map<Tag, Double> distribution = new HashMap<>();
        Tag otherTag = new Tag("others", "0xffffffff");
        distribution.put(otherTag, 0.0);
        for(Expense expense : event.getExpenses()) {
            if(expense.getTag() == null || expense.getTag().getType().equals("others")) {
                distribution.put(otherTag, distribution.get(otherTag) + expense.getAmount());
            }
            else {
                if(!distribution.containsKey(expense.getTag())) {
                    distribution.put(expense.getTag(), expense.getAmount());
                }
                else {
                    distribution.put(expense.getTag(),
                        distribution.get(expense.getTag()) + expense.getAmount());
                }
            }
        }
        return distribution;
    }

    /**
     * Convert a web color to a hexadecimal one(for css)
     * @param color - the color to be converted
     * @return the hexadecimal code
     */
    private static String colorToHex(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        return String.format("#%02x%02x%02x", r, g, b);
    }

    /**
     * Set the event for the controller
     * @param event - the event to be set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Returns back to event overview
     */
    @FXML
    public void onBackClick() {
        mainCtrl.showEventOverview(event);
    }
}
