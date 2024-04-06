package client.scenes;

import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Event;
import commons.Tag;
import jakarta.inject.Inject;

public class TagCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;


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
        if(event != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();
            // switchTextLanguage();
        }
    }
}
