/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.utils.ConfigClient;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private ConfigClient configClient;
    private final String filePath = "client/src/main/resources/config.txt";

    /**
     * Launches the app
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        configClient =  new ConfigClient();
        configClient.readFromFile(filePath);

        var editparticipant = FXML.load(EditParticipantCtrl.class, "client",
                "scenes", "EditParticipant.fxml");
        var addparticipant = FXML.load(AddParticipantCtrl.class, "client",
                "scenes", "AddParticipant.fxml");
        var eventoverview = FXML.load(EventOverviewCtrl.class, "client",
                "scenes", "EventOverview.fxml");
        var addexpense = FXML.load(AddExpenseCtrl.class, "client",
                "scenes", "AddEditExpense.fxml");
        var opendebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var startscreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var usersettings = FXML.load(
                UserSettingsCtrl.class, "client", "scenes", "UserSettings.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, editparticipant,
                addparticipant, eventoverview, addexpense,
                opendebts, startscreen, invitation, usersettings);

        primaryStage.setOnCloseRequest(e -> {
            eventoverview.getKey().stop();
        });
    }
}