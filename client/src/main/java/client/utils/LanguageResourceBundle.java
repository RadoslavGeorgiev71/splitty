package client.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageResourceBundle {
    private static LanguageResourceBundle instance;
    private ResourceBundle resourceBundle;

    private LanguageResourceBundle(String language) {
        if (language == null || language.equals("")) {
            language = "en";
        }
        resourceBundle = ResourceBundle.getBundle("client.languages.Language", new Locale(language));
    }

    public static LanguageResourceBundle getInstance(String language) {
        if (instance == null) {
            instance = new LanguageResourceBundle(language);
        }
        return instance;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void switchLanguage(String language) {
        resourceBundle = ResourceBundle.getBundle("client.languages.Language", new Locale(language));
    }
}
