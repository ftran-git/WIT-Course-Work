package com.example.SyncUp.GUI.PopupViews;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.RDFResources.Event;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EventDetailsView {
    private Event event;
    private Stage popUp;

    

    public EventDetailsView(Stage owner, Event event, ConfigurableApplicationContext applicationContext) {
        RESTController restController = applicationContext.getBean(RESTController.class); // Get RESTController instance

        this.event = event;
        this.popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(owner);

        // format the way dates are displayed
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy HH:mm");

        // to pass through format
        LocalDateTime startDateTime = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Setup labels with event details
        Label startDateLabel = new Label("Start Date: " + formatter.format(startDateTime));
        Label endDateLabel = new Label("End Date: " + formatter.format(endDateTime));
        Label locationLabel = new Label("Location: " + event.getLocation());
        Label descriptionLabel = new Label("Description: " + event.getDescription());
        Label attendeesLabel = new Label("Attendees: " + String.join(", ", event.getAttendees()));
        Button deleteButton = new Button("Delete Event");

        // Style the labels
        startDateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        endDateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        locationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        descriptionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        attendeesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add components to grid pane
        gridPane.add(startDateLabel, 0, 0);
        gridPane.add(endDateLabel, 0, 1);
        gridPane.add(locationLabel, 0, 2);
        gridPane.add(descriptionLabel, 0, 3);
        gridPane.add(attendeesLabel, 0, 4);
        gridPane.add(deleteButton, 0, 5);

        deleteButton.setOnMousePressed(e -> {
            SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyyMM");
            String yearMonth = desiredFormat.format(event.getStartDate());
            String eventTitle = event.getDescription().replace(" ", "-");
            try {
                restController.deleteEvent(restController.solidPodURL + "events/" + yearMonth + "/" + eventTitle);
                Stage stage = (Stage) deleteButton.getScene().getWindow();
                stage.close();
            } catch (Exception ex) {
                System.out.println("Could not delete event : " + ex);
            }

        });

        Scene dialogScene = new Scene(gridPane, 400, 250);
        popUp.setScene(dialogScene);
    }

    public void show() {
        popUp.show();
    }
}
