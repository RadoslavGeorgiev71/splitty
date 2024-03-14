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
package client.scenes;

import commons.Event;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private EditParticipantCtrl editParticipantCtrl;
    private Scene editparticipant;

    private EventOverviewCtrl eventOverviewCtrl;
    private Scene eventoverview;

    private OpenDebtsCtrl openDebtsCtrl;
    private Scene opendebts;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private InvitationCtrl invitationCtrl;
    private Scene invitation;

    private StartScreenCtrl startScreenCtrl;
    private Scene startscreen;

    /**
     * Initializes stage
     * @param primaryStage
     * @param overview
     * @param editparticipant
     * @param add
     * @param invitation
     */
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<StartScreenCtrl, Parent> startscreen) 
                           Pair<AddQuoteCtrl, Parent> add, Pair<InvitationCtrl, Parent> invitation) {
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<EditParticipantCtrl, Parent> editparticipant,
                           Pair<EventOverviewCtrl, Parent> eventoverview,
                           Pair<OpenDebtsCtrl, Parent> opendebts,
                           Pair<StartScreenCtrl, Parent> startscreen) {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editParticipantCtrl = editparticipant.getKey();
        this.editparticipant = new Scene(editparticipant.getValue());

        this.eventOverviewCtrl = eventoverview.getKey();
        this.eventoverview = new Scene(eventoverview.getValue());

        this.openDebtsCtrl = opendebts.getKey();
        this.opendebts = new Scene(opendebts.getValue());

        this.startScreenCtrl = startscreen.getKey();
        this.startscreen = new Scene(startscreen.getValue());

        showStartScreen();
        primaryStage.show();

    }

    /**
     * Shows the starting  screen
     */
    private void showStartScreen() {
        primaryStage.setTitle("Start Screen");
        primaryStage.setScene(startscreen);
    }

    /**
     * Shows quote overview
     */
    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    /**
     * Switches the scene to the edit participant window
     * @param event takes an event as a parameter for which we edit a participant
     * @param participant takes the participant as a parameter to edit
     */
    public void showEditParticipant(Event event, Participant participant) {
        primaryStage.setTitle("Edit Participant");
        primaryStage.setScene(editparticipant);
        editParticipantCtrl.setEvent(event);
        editParticipantCtrl.setParticipant(participant);
        editParticipantCtrl.initialize();
    }

    public void showEventOverview(Event event) {
        primaryStage.setTitle("Event Overview");
        eventOverviewCtrl.setEvent(event);
        eventOverviewCtrl.initialize();
        primaryStage.setScene(eventoverview);
    }


    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showOpenDebts() {
        primaryStage.setTitle("Open Debts");
        primaryStage.setScene(opendebts);
    }
}