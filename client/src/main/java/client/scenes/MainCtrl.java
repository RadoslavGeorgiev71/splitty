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

    /**
     * Initializes stage
     * @param primaryStage
     * @param overview
     * @param editparticipant
     */
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<EditParticipantCtrl, Parent> editparticipant) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.editParticipantCtrl = editparticipant.getKey();
        this.editparticipant = new Scene(editparticipant.getValue());

        showOverview();
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
     */
    public void showEditParticipant(Event event) {
        primaryStage.setTitle("Edit Participant");
        primaryStage.setScene(editparticipant);
        editParticipantCtrl.setEvent(event);
    }

    /**
     * Switches the scene to overview event window
     * @param event to display
     */
    public void showOverviewEvent(Event event) {
        //TODO show the overview of an event window
    }
}