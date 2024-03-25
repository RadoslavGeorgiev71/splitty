package client.utils;

import client.scenes.StartScreenCtrl;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Paths;

public class LanguageButtonUtils {

    public static void updateLanguageMenuButton(MenuButton languageButton, ConfigClient config) {
        // Set the properties of the MenuButton
        languageButton.setLayoutX(482.0);
        languageButton.setLayoutY(345.0);
        languageButton.setMnemonicParsing(false);
        languageButton.getStyleClass().add("language-menu-button");

        // Create the MenuItems
        createMenuItems(languageButton, config);
    }

    private static void createMenuItems(MenuButton languageButton, ConfigClient config) {
        MenuItem questionItem = new MenuItem();
        questionItem.setText("New");
        languageButton.getItems().add(questionItem);

        String imagesFolderPath = "client/src/main/resources/client/images/flags/";

        File imagesFolder = new File(imagesFolderPath);
        File[] imageFiles = imagesFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

        for (File imageFile : imageFiles) {
            if(imageFile.getName().equals(config.getLanguage() + ".png")){
                Image image = new Image(Paths.get(imagesFolderPath, imageFile.getName()).toUri().toString());
                ImageView imageView = new ImageView(image);
                languageButton.setGraphic(imageView);

                String imageName = imageFile.getName();
                String menuButtonText = imageName.substring(0, imageName.lastIndexOf('.'));
                languageButton.setText(menuButtonText.toUpperCase());
                continue;
            }
            MenuItem menuItem = new MenuItem();

            Image image = new Image(Paths.get(imagesFolderPath, imageFile.getName()).toUri().toString());
            ImageView imageView = new ImageView(image);
            menuItem.setGraphic(imageView);

            String imageName = imageFile.getName();
            String menuItemText = imageName.substring(0, imageName.lastIndexOf('.'));
            menuItem.setText(menuItemText.toUpperCase());

            languageButton.getItems().add(menuItem);
        }
    }

    public static void languageMenu(MenuButton languageButton, ConfigClient config,
                                    LanguageResourceBundle languageResourceBundle,
                                    StartScreenCtrl controller, String[] keys) {
        for (int i = 1; i < languageButton.getItems().size(); i++){
            MenuItem menuItem = (MenuItem) languageButton.getItems().get(i);
            menuItem.setOnAction(event -> {
                String language = menuItem.getText().toLowerCase();
                languageResourceBundle.switchLanguage(language);
                // switchTextLanguage(); // This method should be in your controller
                config.setLanguage(language);
                String[] contents = {config.getServerUrl(), config.getEmail(),
                        config.getIban(), config.getBic(),
                        config.getLanguage(), config.getCurrency(),
                        config.getName(), config.getRecentEvents()};

                config.writeToFile("client/src/main/resources/config.txt", contents, keys);

                ImageView menuItemImageView = (ImageView) menuItem.getGraphic();
                ImageView menuButtonImageView = (ImageView) languageButton.getGraphic();

                Image tempImage = menuButtonImageView.getImage();
                menuButtonImageView.setImage(menuItemImageView.getImage());
                menuItemImageView.setImage(tempImage);

                String tempText = languageButton.getText();
                languageButton.setText(menuItem.getText());
                menuItem.setText(tempText);

                controller.initialize(); // This method should be in your controller
            });
        }
    }
}
