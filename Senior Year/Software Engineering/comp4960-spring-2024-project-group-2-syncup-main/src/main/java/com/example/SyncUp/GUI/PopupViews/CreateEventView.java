package com.example.SyncUp.GUI.PopupViews;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.GUI.CalendarView;
import com.example.SyncUp.Objects.FriendData;
import com.example.SyncUp.RDFResources.Event;
import com.example.SyncUp.RDFResources.Friend;
import com.inrupt.client.accessgrant.AccessGrant;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.scene.control.cell.CheckBoxListCell;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.example.SyncUp.GUI.View.getCalendarView;
import static com.example.SyncUp.GUI.View.getFriendsView;

public class CreateEventView{

    private Date start;
    private Date end;
    private String location;
    private String description;
    private ArrayList<FriendData> attendees;

    public CreateEventView(Stage primaryStage, ConfigurableApplicationContext applicationContext, LocalDate date) {
        RESTController restController = applicationContext.getBean(RESTController.class); // Get RESTController instance
        this.start = java.sql.Date.valueOf(date);

        final Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);

        //Create field labels
        Label startDateLabel = new Label("Start Date: " + date);
        Label startTimeLabel = new Label("Start Time (HH:MM)");
        Label endDateLabel = new Label("End Date:");
        Label endTimeLabel = new Label("End Time (HH:MM)");
        Label locationLabel = new Label("Location:");
        Label descriptionLabel = new Label("Description:");
        Label attendeesLabel = new Label("Attendees:");
        //Create input fields
        // Create a Spinner for hours
        Spinner<Integer> startHourSpinner = new Spinner<>(0, 23, 0);
        startHourSpinner.setEditable(true);
        // Create a Spinner for minutes
        Spinner<Integer> startMinuteSpinner = new Spinner<>(0, 59, 0);
        startMinuteSpinner.setEditable(true);

        // Create a Spinner for hours
        Spinner<Integer> endHourSpinner = new Spinner<>(0, 23, 0);
        endHourSpinner.setEditable(true);
        // Create a Spinner for minutes
        Spinner<Integer> endMinuteSpinner = new Spinner<>(0, 59, 0);
        endMinuteSpinner.setEditable(true);
        DatePicker endDateDatePicker = new DatePicker();

        TextField locationTextField = new TextField();
        locationTextField.setPrefWidth(80);
        TextField descriptionTextField = new TextField();
        descriptionTextField.setPrefWidth(80);
        descriptionTextField.setPrefHeight(80);
        //Get the users Friends as possible Attendees
        Set<URI> resourceURLs = restController.getContainer(restController.solidPodURL + "friends/");
        List<FriendData> friendData = new ArrayList<>();
        for (URI resourceURL : resourceURLs)  {
            String webID = restController.getFriend(resourceURL.toString()).getWebID();
            friendData.add(new FriendData(resourceURL, URI.create(webID)));
        }
        ObservableList<FriendData> items = FXCollections.observableArrayList(friendData);
        ListView<FriendData> listView = new ListView<>(items);

        ArrayList<FriendData> attendeesList = new ArrayList<>();
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<FriendData, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(FriendData item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        attendeesList.add(item);
                    } else {
                        attendeesList.remove(item);
                    }
                });
                return observable;
            }
        }));

        Button createButton = new Button("Create");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        // Add components to grid pane
        gridPane.add(startDateLabel, 0, 0);
        gridPane.add(startTimeLabel, 1, 0);
        gridPane.add(startHourSpinner, 2, 0);
        //colon
        gridPane.add(startMinuteSpinner, 3, 0);
        gridPane.add(endDateLabel, 0, 1);
        gridPane.add(endDateDatePicker, 1, 1);
        //hour minute label
        gridPane.add(endTimeLabel, 0,2);
        gridPane.add(endHourSpinner, 1, 2);
        gridPane.add(endMinuteSpinner, 2, 2);
        gridPane.add(locationLabel, 0, 3);
        gridPane.add(locationTextField, 1, 3);
        gridPane.add(descriptionLabel, 0, 4);
        gridPane.add(descriptionTextField, 1, 4);
        gridPane.add(attendeesLabel, 0, 5);
        gridPane.add(listView, 1, 5);
        gridPane.add(createButton, 1, 6);

        // Event Handler for create button
        EventHandler<ActionEvent> createB = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Get field inputs from user
                setStart(date, LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()));
                setEnd(endDateDatePicker.getValue(), LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue()));
                setLocation(locationTextField.getText());
                setDescription(descriptionTextField.getText());
                setAttendees(attendeesList);
                //Create event on pod
                String eventTitle = getDescription().replace(" ", "-");
                // Format the date into the desired string format
                SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyyMM");
                String yearMonth = desiredFormat.format(getStart());
                try{
                    URI resourceURL = new URI(restController.solidPodURL + "events/" + yearMonth +"/" + eventTitle);
                    System.out.println(resourceURL);
                    Event newEvent = new Event(resourceURL, getStart(), getEnd(), getLocation(), getDescription(), getAttendees());
                    restController.createEvent(newEvent);
                    System.out.println("EVENT CREATED");

                    //add event to the attendees calendar
                    for (FriendData f : attendeesList) {
                        // Get Friend's Pod URL through their WebID
                        Set<URI> response = restController.getPods(f.getWebID().toString());
                        // Get first Pod URL from set returned
                        URI URI = null;
                        String podURL = "";
                        for (URI uri : response) {
                            URI = uri;
                            podURL = uri.toString();
                            break;
                        }
                        try {
                            AccessGrant accessGrant = restController.getAccessGrant(f.getWebID().toString(), restController.solidWebID);
                            Set<URI> resourceURLs = restController.getContainerFromFriend(accessGrant, podURL + "events/");
                            for (URI container: resourceURLs) {
                                String containerAsString = String.valueOf(container);
                                String containerYearMonth = containerAsString.substring(containerAsString.length() - 7);
                                String containerWithResourceName = container.toString() + getDescription().toString();
                                if (containerYearMonth.equals(yearMonth+"/")){
                                    newEvent = new Event(URI.create(containerWithResourceName), getStart(), getEnd(), getLocation(), getDescription(), getAttendees());
                                    restController.createEventForFriend(accessGrant, newEvent);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Could not access events from " + f);
                        }


                        //URI friendResourceURL = new URI(f.getWebID() + "events/" + yearMonth +"/" + eventTitle);
                        //newEvent = new Event(friendResourceURL, getStart(), getEnd(), getLocation(), getDescription(), getAttendees());
                        //restController.createEvent(newEvent);
                    }
                    Stage stage = (Stage) createButton.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

                // Refresh CalendarView
                Stage currentStage = (Stage)primaryStage.getScene().getWindow();
                currentStage.setScene(getCalendarView(primaryStage));
            }
        };

        createButton.setOnAction(createB);

        Scene dialogScene = new Scene(gridPane, 700, 400);
        popUp.setScene(dialogScene);
        popUp.show();
    }

    private Date getStart(){ return start;}

    private void setStart(LocalDate date, LocalTime time){
        LocalDateTime startDateTime = LocalDateTime.of(date, time);
        this.start = java.sql.Timestamp.valueOf(startDateTime);
    }

    private Date getEnd(){ return end;}

    private void setEnd(LocalDate date, LocalTime time){
        LocalDateTime endDateTime = LocalDateTime.of(date, time);
        this.end = java.sql.Timestamp.valueOf(endDateTime);    }

    private String getLocation(){ return location;}

    private void setLocation(String location){
        this.location = location;
    }

    public String getDescription(){ return description;}

    private void setDescription(String description){
        this.description = description;
    }

    private ArrayList<String> getAttendees(){
        ArrayList<String> friendsString = new ArrayList<>();
        for (FriendData f: attendees){
            friendsString.add(f.toString());
        }
        return friendsString;
    }

    private void setAttendees(ArrayList<FriendData> attendees){
        this.attendees = attendees;
    }
}