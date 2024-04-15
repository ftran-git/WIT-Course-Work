package com.example.SyncUp.GUI.PopupViews;

import com.example.SyncUp.Objects.FriendData;
import com.example.SyncUp.Objects.TimeSlot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class FriendAvailabilityView {
    public FriendAvailabilityView(Stage primaryStage, ConfigurableApplicationContext applicationContext, List<TimeSlot> availableTimeSlots, FriendData friendData) {
        // Get first available time slot
        String firstDate = availableTimeSlots.get(0).toString();
        // Get other available time slots
        StringBuilder otherDates = new StringBuilder();
        for (int i = 1; i < availableTimeSlots.size(); i++) {
            otherDates.append(availableTimeSlots.get(i).toString()).append("\n");
        }

        // Initiating popup
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);

        // Create field labels
        Label title = new Label("Availability Found for " + friendData.toString() + "!");
        title.setStyle("-fx-font-size: 20pt"); // Set font size

        // Create labels
        Label firstAvailableTimeSlotLabel = new Label("First available time slot: ");
        if (otherDates.isEmpty()) {
            firstAvailableTimeSlotLabel = new Label("Only available time slot: ");
        }
        Label firstDateLabel = new Label(firstDate);
        Label otherAvailableTimeSlotsLabel = new Label("Other available time slots: ");
        otherAvailableTimeSlotsLabel.setVisible(false);
        Label otherDatesLabel = new Label(otherDates.toString().toString());
        otherDatesLabel.setVisible(false);

        // Create the GridPane for the popup content
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Create a toggle button to show/hide otherDatesLabel
        ToggleButton toggleButton = new ToggleButton("Show Other Dates");
        toggleButton.setOnAction(event -> {
            otherDatesLabel.setVisible(toggleButton.isSelected());
        });
        // Hide toggle button if there are no other dates to display
        if(otherDates.isEmpty()) {
            toggleButton.setVisible(false);
        }

        // Add title label to the grid pane
        gridPane.add(title, 0, 0, 2, 1); // Span title across two columns

        // Add components to grid pane
        gridPane.add(firstAvailableTimeSlotLabel, 0, 1);
        gridPane.add(firstDateLabel, 1, 1);
        gridPane.add(toggleButton, 0, 2);
        //gridPane.add(otherAvailableTimeSlotsLabel, 0, 3);
        gridPane.add(otherDatesLabel, 1, 2);

        Scene scene = new Scene(gridPane, 700, 300);
        popUp.setScene(scene);
        popUp.show();
    }
}