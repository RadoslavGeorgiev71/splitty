package client.scenes;

import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.util.List;
import java.util.ResourceBundle;

public class TagCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;
    private Expense expense;
    private Participant participant;
    private boolean isAddExpense;

    @FXML
    private Label tagLabel;
    @FXML
    private ChoiceBox<Tag> tagMenu;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label colorLabel;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     * @param server - the server
     * @param mainCtrl - the mainCtrl
     */
    @Inject
    public TagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the addEditTag scene
     */
    public void initialize() {
        if(expense != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            List<Tag> tags = server.getTags();
            tagMenu.setItems(FXCollections.observableArrayList(tags));
            tagMenu.setConverter(new StringConverter<Tag>() {
                @Override
                public String toString(Tag tag) {
                    if(tag != null) {
                        return tag.getType();
                    }
                    else {
                        return "";
                    }
                }
                @Override
                public Tag fromString(String s) {
                    return null;
                }
            });
            tagMenu.getSelectionModel().selectFirst();
            nameTextField.setText(tagMenu.getValue().getType());
            colorPicker.setValue(Color.web(tagMenu.getValue().getColor()));
            tagMenu.setOnAction(e -> {
                nameTextField.setText(tagMenu.getValue().getType());
                colorPicker.setValue(Color.web(tagMenu.getValue().getColor()));
            });
        }
    }

    @FXML
    private void onAddTag() {

        if(!nameTextField.getText().isEmpty()) {
            server.addTag(new Tag(nameTextField.getText(), colorPicker.getValue().toString()));
            mainCtrl.showTags(event, expense, participant, isAddExpense);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name");
            alert.setContentText("Please add a name to the tag before adding it!");
            // TODO: translate alert
            alert.showAndWait();
        }
    }

    @FXML
    private void onEditTag() {}

    @FXML
    private void onDeleteTag() {}

    /**
     * Switches the language of the text.
     */
    public void switchTextLanguage() {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
    }

    /**
     * Sets the current expense
     * @param expense - the expense
     */
    public void setExpense(Expense expense){
        this.expense = expense;
    }

    /**
     * Sets the current event
     * @param event - the event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Sets the participant that is paying for the expense
     * @param participant - the participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Set whether the previous scene was addExpense
     */
    public void setIsExpenseTrue(boolean isAddExpense) {
        this.isAddExpense = isAddExpense;
    }


    /**
     * Returns to the previous page
     */
    @FXML
    private void backToExpense() {
        if(this.isAddExpense) {
            mainCtrl.showAddExpense(event, participant);
        }
        else {
            mainCtrl.showEditExpense(event, expense);
        }
    }
}
