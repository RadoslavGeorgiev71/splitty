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

import java.util.ArrayList;
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
    private List<Tag> tags;
    private Tag tagOnFocus;

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
    private Button selectButton;
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
     * Sets the tag on focus
     * @param tagOnFocus - the tag to be set on focus
     */
    public void setTagOnFocus(Tag tagOnFocus) {
        this.tagOnFocus = tagOnFocus;
    }

    /**
     * Initializes the addEditTag scene
     */
    public void initialize() {
        if(event != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            tags = server.getTags();
            if(tagOnFocus == null && !tags.isEmpty()) {
                tagOnFocus = tags.getFirst();
            }
            tagMenu.setItems(FXCollections.observableArrayList(tags));
            tagMenu.setConverter(new StringConverter<Tag>() {
                @Override
                public String toString(Tag tag) {
                    if (tag != null) {
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
            tagMenu.setValue(tagOnFocus);
            if(tagOnFocus != null) {
                nameTextField.setText(tagOnFocus.getType());
                colorPicker.setValue(Color.web(tagOnFocus.getColor()));
            }
            else {
                nameTextField.setText("");
                colorPicker.setValue(Color.web("#FFFFFF"));
            }
            tagMenu.setOnAction(e -> {
                if(tagMenu.getValue() != null) {
                    tagOnFocus = tagMenu.getValue();
                    nameTextField.setText(tagOnFocus.getType());
                    colorPicker.setValue(Color.web(tagOnFocus.getColor()));
                }
            });
        }
    }

    /**
     * Selects the current tag for the expense
     */
    @FXML
    private void onSelectTag() {
        if(tagOnFocus == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No tag");
            alert.setContentText("Please provide a tag to be selected!");
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if(this.isAddExpense) {
            mainCtrl.showAddExpenseWithTag(event, participant, tagOnFocus);
        }
        else {
            mainCtrl.showEditExpenseWithTag(event, expense, tagOnFocus);
        }
    }

    /**
     * Adds a tag to the server and refreshes the tag scene
     */
    @FXML
    private void onAddTag() {
        if(tags.stream().map(Tag::getType)
            .toList().contains(nameTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name");
            alert.setContentText("The tag name you provided already exists!");
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if(!nameTextField.getText().isEmpty()) {
            tagOnFocus =
                server.addTag(new Tag(nameTextField.getText(), colorPicker.getValue().toString()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful addition");
            alert.setContentText("Tag has bee added successfully!");
            // TODO: translate alert
            alert.showAndWait();
            mainCtrl.showTags(event, expense, participant, isAddExpense, tagOnFocus);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name");
            alert.setContentText("Please add a name to the tag before adding it!");
            // TODO: translate alert
            alert.showAndWait();
        }
    }

    /**
     * Updates a tag
     */
    @FXML
    private void onEditTag() {
        if(tagOnFocus == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No tag");
            alert.setContentText("Please first add a tag before editing!");
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if(tags.stream().map(Tag::getType)
            .toList().contains(nameTextField.getText()) &&
            !tagOnFocus.getType().equals(nameTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name");
            alert.setContentText("The tag name you provided already exists!");
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if(tagOnFocus.getType().equals(nameTextField.getText()) &&
            tagOnFocus.getColor().equals(colorPicker.getValue().toString())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No changes");
            alert.setContentText("No changes provided for the tag!");
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if (!nameTextField.getText().isEmpty()) {
            tagOnFocus.setType(nameTextField.getText());
            tagOnFocus.setColor(colorPicker.getValue().toString());
            Tag updatedTag = server.updateTag(tagOnFocus);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful update");
            alert.setContentText("Tag has bee edited successfully!");
            // TODO: translate alert
            alert.showAndWait();
            mainCtrl.showTags(event, expense, participant, isAddExpense, tagOnFocus);
            tagOnFocus = updatedTag;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name");
            alert.setContentText("Please add a name to the tag before updating it!");
            // TODO: translate alert
            alert.showAndWait();
        }
    }

    /**
     * Deletes a tag
     */
    @FXML
    private void onDeleteTag() {
        if(tagOnFocus == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No tag");
            alert.setContentText("No provided tag to be deleted!");
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirmation on deletion");
        alert.setContentText("Are you sure you want to delete this tag?");
        // TODO: translate the alert
        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK) {
                server.deleteTag(tagOnFocus);
                tagOnFocus = null;
                mainCtrl.showTags(event, expense, participant, isAddExpense, tagOnFocus);
            }
        });
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
