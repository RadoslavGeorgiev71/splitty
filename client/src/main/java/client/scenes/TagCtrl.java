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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

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
            tagMenu.setItems(FXCollections.observableArrayList(server.getTags()));
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
        }
    }

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
