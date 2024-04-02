package client.utils;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LanguageButtonUtils {

    private static Path imagesFolderPath =
            Paths.get("client/src/main/resources/client/images/flags/").toAbsolutePath();

    private static Path filePath = Paths.
            get("client/src/main/resources/config.txt").toAbsolutePath();



    /**
     * Updates the language menu button with the current language and flags
     * @param languageButton the language menu button
     * @param config the config client
     */

    public static void updateLanguageMenuButton(MenuButton languageButton, ConfigClient config) {

        languageButton.setLayoutX(482.0);
        languageButton.setLayoutY(345.0);
        languageButton.setMnemonicParsing(false);
        languageButton.getStyleClass().add("language-menu-button");


        createMenuItems(languageButton, config);
    }

    /**
     * Creates the menu items for the language menu button
     * @param languageButton the language menu button
     * @param config the config client
     */

    private static void createMenuItems(MenuButton languageButton, ConfigClient config) {
        MenuItem questionItem = new MenuItem();
        questionItem.setText("New");
        languageButton.getItems().add(questionItem);


        //String imagesFolderPath = "client/src/main/resources/client/images/flags/";

        File imagesFolder = new File(String.valueOf(imagesFolderPath));

        File[] imageFiles = imagesFolder.listFiles((dir, name)
                -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

        for (File imageFile : imageFiles) {
            if(imageFile.getName().equals(config.getLanguage() + ".png")){
                Image image = new Image(Paths.get(String.valueOf(imagesFolderPath),
                        imageFile.getName()).toUri().toString());
                ImageView imageView = new ImageView(image);
                languageButton.setGraphic(imageView);

                String imageName = imageFile.getName();
                String menuButtonText = imageName.substring(0, imageName.lastIndexOf('.'));
                languageButton.setText(menuButtonText.toUpperCase());
                continue;
            }
            MenuItem menuItem = new MenuItem();

            Image image = new Image(Paths.get(String.valueOf(imagesFolderPath),
                    imageFile.getName()).toUri().toString());
            ImageView imageView = new ImageView(image);
            menuItem.setGraphic(imageView);

            String imageName = imageFile.getName();
            String menuItemText = imageName.substring(0, imageName.lastIndexOf('.'));
            menuItem.setText(menuItemText.toUpperCase());

            languageButton.getItems().add(menuItem);
        }
    }

    /**
     * Changes the language of the application + the language in config.
     * @param languageButton the language menu button
     * @param config the config client
     * @param languageResourceBundle the language resource bundle
     * @param initializer the initializer
     * @param keys the keys for the config file
     */

    public static void languageMenu(MenuButton languageButton, ConfigClient config,
                                    LanguageResourceBundle languageResourceBundle,
                                    Runnable initializer, String[] keys) {

        MenuItem downloadItem = (MenuItem) languageButton.getItems().get(0);
        downloadItem.setOnAction(event -> {
            downloadButtonAction();
        });

        for (int i = 1; i < languageButton.getItems().size(); i++){
            MenuItem menuItem = (MenuItem) languageButton.getItems().get(i);
            menuItem.setOnAction(event -> {
                String language = menuItem.getText().toLowerCase();
                languageResourceBundle.switchLanguage(language);

                config.setLanguage(language);
                String[] contents = {config.getServerUrl(), config.getEmail(),
                        config.getIban(), config.getBic(),
                        config.getLanguage(), config.getCurrency(),
                        config.getName(), config.getRecentEvents()};

                config.writeToFile(String.valueOf(filePath), contents, keys);

                ImageView menuItemImageView = (ImageView) menuItem.getGraphic();
                ImageView menuButtonImageView = (ImageView) languageButton.getGraphic();

                Image tempImage = menuButtonImageView.getImage();
                menuButtonImageView.setImage(menuItemImageView.getImage());
                menuItemImageView.setImage(tempImage);

                String tempText = languageButton.getText();
                languageButton.setText(menuItem.getText());
                menuItem.setText(tempText);

                initializer.run();
            });
        }
    }

    private static void downloadButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialFileName("languageTemplate.txt");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                Path languageFilePath = Paths.get(
                        "client/src/main/resources/client/languages/Language_en.properties");
                List<String> lines = Files.readAllLines(languageFilePath, StandardCharsets.UTF_8);
                lines.add(0, "# Language file template");
                lines.add(1, "# Please fill in the translations for the following keys");
                lines.add(2,
                        "# Save the file as Language_xx.properties where xx is the language code");
                lines.add(3, "# For example: Language_fr.properties for French");
                lines.add(4, "# The language code should be the same as the flag image name");
                lines.add(5, "# Then email us at oopteam8@gmail.com" +
                                " with this file and and image of the flag");
                Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
