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

    private AddQuoteCtrl addCtrl;
    private Scene add;

    /**
     * Initializes stage
     * @param primaryStage
     * @param overview
     * @param editparticipant
     * @param add
     */
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<EditParticipantCtrl, Parent> editparticipant,
                           Pair<EventOverviewCtrl, Parent> eventoverview) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editParticipantCtrl = editparticipant.getKey();
        this.editparticipant = new Scene(editparticipant.getValue());

        this.eventOverviewCtrl = eventoverview.getKey();
        this.eventoverview = new Scene(eventoverview.getValue());

        showEventOverview();
        primaryStage.show();
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
    }

    public void showEventOverview() {
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(eventoverview);
    }

    /**
     * Switches the scene to overview event window
     * @param event to display
     */
    public void showOverviewEvent(Event event) {
        //TODO show the overview of an event window
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
}