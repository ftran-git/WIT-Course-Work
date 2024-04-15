package com.example.SyncUp.GUI.PopupViews;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.Objects.FriendData;
import com.example.SyncUp.Objects.TimeSlot;
import com.example.SyncUp.RDFResources.Event;
import com.inrupt.client.accessgrant.AccessGrant;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FindAvailabilityView {

    public FindAvailabilityView(Stage primaryStage, ConfigurableApplicationContext applicationContext, FriendData friendData) {
        // Get RESTController instance
        RESTController restController = applicationContext.getBean(RESTController.class);

        // Get name of selected friend from ListView
        String selectedFriend = friendData.toString();

        // Initiating popup
        final Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);

        // Create field labels
        Label title = new Label("Find Availability for " + selectedFriend);
        title.setStyle("-fx-font-size: 20pt"); // Set font size

        // Create field labels
        Label farthestDateLabel = new Label("Farthest out date to schedule by?");
        Label preferredDayLabel = new Label("Preferred day(s) of the week?");
        Label desiredStartTimeLabel = new Label("Desired start time?");
        Label desiredEndTimeLabel = new Label("Desired end time?");

        // Create DatePicker control for farthest date
        DatePicker farthestDatePicker = new DatePicker();
        // Create Checkboxes for each day of the week (add to list for
        List<CheckBox> checkBoxList = new ArrayList<>();
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : days) {
            checkBoxList.add(new CheckBox(day));
        }
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

        // Create save button
        Button findButton = new Button("Find");

        // Create GridPane to store components
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add title label to the grid pane
        GridPane.setColumnSpan(title, 3); // Span the title across all columns
        gridPane.add(title, 0, 0);

        // Add components to grid pane
        gridPane.add(farthestDateLabel, 0, 1);
        gridPane.add(farthestDatePicker, 1, 1);
        gridPane.add(preferredDayLabel, 0, 2);
        gridPane.add(checkBoxList.get(0), 1, 2);
        gridPane.add(checkBoxList.get(1), 1, 3);
        gridPane.add(checkBoxList.get(2), 1, 4);
        gridPane.add(checkBoxList.get(3), 1, 5);
        gridPane.add(checkBoxList.get(4), 1, 6);
        gridPane.add(checkBoxList.get(5), 1, 7);
        gridPane.add(checkBoxList.get(6), 1, 8);
        gridPane.add(desiredStartTimeLabel, 0, 9);
        gridPane.add(startHourSpinner, 1, 9);
        gridPane.add(startMinuteSpinner, 2, 9);
        gridPane.add(desiredEndTimeLabel, 0, 10);
        gridPane.add(endHourSpinner, 1, 10);
        gridPane.add(endMinuteSpinner, 2, 10);
        gridPane.add(findButton, 1, 11);

        // Event Handler for find button
        EventHandler<ActionEvent> findAvailabilityEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Create ProgressIndicator
                ProgressIndicator spinner = new ProgressIndicator();
                gridPane.add(spinner, 1, 12);

                // List to store available time slots for user and friend to schedule an event
                List<TimeSlot> availableTimeSlots = new ArrayList<>();
                Task<Void> findAvailability = new Task<Void>() {
                    protected Void call() throws Exception {
                        // Get the farthest date the user wants to schedule by
                        LocalDate farthestDate = farthestDatePicker.getValue();
                        // Get the preferred days the user wants to schedule on
                        List<String> preferredDays = new ArrayList<>();
                        for (CheckBox checkBox : checkBoxList) {
                            if (checkBox.isSelected()) {
                                preferredDays.add(checkBox.getText().toUpperCase());
                            }
                        }
                        // Get the desired start time
                        int startHour = startHourSpinner.getValue();
                        int startMinute = startMinuteSpinner.getValue();
                        LocalTime desiredStartTime = LocalTime.of(startHour, startMinute);
                        // Get the desired end time
                        int endHour = endHourSpinner.getValue();
                        int endMinute = endMinuteSpinner.getValue();
                        LocalTime desiredEndTime = LocalTime.of(endHour, endMinute);

                        // Get friend's webID from ListView
                        URI friendWebID = friendData.getWebID();
                        // Get friend's Pod URL through their WebID
                        Set<URI> response = restController.getPods(friendWebID.toString());
                        // Get first Pod URL from set returned
                        URI URI = null;
                        String friendPodURL = "";
                        for (URI uri : response) {
                            URI = uri;
                            friendPodURL = uri.toString();
                            break;
                        }
                        // Get all event resources from user and friend
                        Set<URI> userYearMonthResources = null;
                        Set<URI> friendYearMonthResources = null;
                        AccessGrant accessGrant = null;
                        if (selectedFriend != null) {
                            // Print the selected friend to find availability
                            System.out.println("Selected friend to find availability: " + selectedFriend);

                            // Get all year-month resource URLs from user's events container
                            userYearMonthResources = restController.getContainer(restController.solidPodURL + "events/");
                            System.out.println("userYearMonthResources: " + userYearMonthResources.toString());

                            // Get all year-month resource URLs from friend's events container
                            try {
                                // Get AccessGrant
                                accessGrant = restController.getAccessGrant(friendWebID.toString(), restController.solidWebID);

                                // Use AccessGrant to get all year-month resource URLs from friend's events container
                                friendYearMonthResources = restController.getContainerFromFriend(accessGrant, friendPodURL + "events/");
                                System.out.println("friendYearMonthResources: " + friendYearMonthResources.toString());
                            } catch (Exception e) {
                                spinner.setVisible(false);
                                System.out.println("ERROR: NO ACCESSGRANT FOUND!");
                            }

                            // Get the current date
                            LocalDate currentDate = LocalDate.now();

                            // Check all time slots up until the farthest out date specified by the user
                            int counter = 0;
                            while (!currentDate.isEqual(farthestDate)) {
                                System.out.println("Checking Date: " + currentDate.toString());

                                // Check if current day is a preferred day by the user
                                if (!preferredDays.contains(currentDate.getDayOfWeek().toString())) {
                                    // Skip to the next date and continue to next iteration
                                    System.out.println("SKIPPED BECAUSE NOT PREFERRED DAY OF THE WEEK!");
                                    currentDate = currentDate.plusDays(1);
                                    continue;
                                }

                                // Get and format year-month as it will be formatted in events container (yyyyMM)
                                int currentYear = currentDate.getYear();
                                int currentMonth = currentDate.getMonthValue();
                                String formattedMonth = String.format("%02d", currentMonth);
                                String currentYearMonth = String.valueOf(currentYear) + String.valueOf(formattedMonth);

                                // Check if year-month container exists in user's Pod
                                boolean yearMonthExistsForUser = false;
                                for (URI resource : userYearMonthResources) {
                                    if (resource.toString().contains(currentYearMonth)) {
                                        yearMonthExistsForUser = true;
                                    }
                                }
                                // Check if year-month container exists in friend's Pod
                                boolean yearMonthExistsForFriend = false;
                                for (URI resource : friendYearMonthResources) {
                                    if (resource.toString().contains(currentYearMonth)) {
                                        yearMonthExistsForFriend = true;
                                    }
                                }

                                // If both year-month containers do not exist then no events are saved for both parties so add to list of available time slots
                                if (!yearMonthExistsForUser && !yearMonthExistsForFriend) {
                                    // Add current date along with desired start and end time to list of available time slots
                                    TimeSlot timeSlot = new TimeSlot(currentDate, desiredStartTime, desiredEndTime);
                                    availableTimeSlots.add(timeSlot);
                                } else if (yearMonthExistsForUser && yearMonthExistsForFriend) {
                                    // Get all resource URLs to events in current year-month container for user
                                    Set<URI> userEventResourceURLS = restController.getContainer(restController.solidPodURL + "events/" + currentYearMonth + "/");

                                    // Get each event resource and check if the event conflicts with current date
                                    boolean userConflictFound = false;
                                    for (URI resourceURl : userEventResourceURLS) {
                                        Event event = restController.getEvent(resourceURl.toString());
                                        // If event start date conflicts with current date and start time then set conflictFound to true
                                        LocalDate eventDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        LocalTime eventStartTime = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        LocalTime eventEndTime = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        if (eventDate.isEqual(currentDate)) {
                                            if (isOverlapping(eventStartTime, eventEndTime, desiredStartTime, desiredEndTime)) {
                                                userConflictFound = true;
                                                break;
                                            }
                                        } else {
                                            // Continue to next event if it is not on the same day
                                            continue;
                                        }
                                    }

                                    // Get all resource URLs to events in current year-month container for friend
                                    Set<URI> friendEventResourceURLS = restController.getContainerFromFriend(accessGrant, friendPodURL + "events/" + currentYearMonth + "/");

                                    // Get each event resource and check if the event conflicts with current date
                                    boolean friendConflictFound = false;
                                    for (URI resourceURl : friendEventResourceURLS) {
                                        Event event = restController.getEventFromFriend(accessGrant, resourceURl.toString());
                                        // If event start date conflicts with current date and start time then set conflictFound to true
                                        LocalDate eventDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        LocalTime eventStartTime = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        LocalTime eventEndTime = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        if (eventDate.isEqual(currentDate)) {
                                            if (isOverlapping(eventStartTime, eventEndTime, desiredStartTime, desiredEndTime)) {
                                                friendConflictFound = true;
                                                break;
                                            }
                                        } else {
                                            // Continue to next event if it is not on the same day
                                            continue;
                                        }
                                    }

                                    // If no conflict found for user and friend then add current date along with desired start and end time to list of available time slots
                                    if (!userConflictFound && !friendConflictFound) {
                                        TimeSlot timeSlot = new TimeSlot(currentDate, desiredStartTime, desiredEndTime);
                                        availableTimeSlots.add(timeSlot);
                                    }
                                } else if (yearMonthExistsForUser) {
                                    // Get all resource URLs to events in current year-month container for user
                                    Set<URI> eventResourceURLS = restController.getContainer(restController.solidPodURL + "events/" + currentYearMonth + "/");

                                    // Get each event resource and check if the event conflicts with current date
                                    boolean conflictFound = false;
                                    for (URI resourceURl : eventResourceURLS) {
                                        Event event = restController.getEvent(resourceURl.toString());
                                        // If event start date conflicts with current date and start time then set conflictFound to true
                                        LocalDate eventDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        LocalTime eventStartTime = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        LocalTime eventEndTime = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        if (eventDate.isEqual(currentDate)) {
                                            if (isOverlapping(eventStartTime, eventEndTime, desiredStartTime, desiredEndTime)) {
                                                conflictFound = true;
                                                break;
                                            }
                                        } else {
                                            // Continue to next event if it is not on the same day
                                            continue;
                                        }
                                    }

                                    // If no conflict found then add current date along with desired start and end time to list of available time slots
                                    if (!conflictFound) {
                                        TimeSlot timeSlot = new TimeSlot(currentDate, desiredStartTime, desiredEndTime);
                                        availableTimeSlots.add(timeSlot);
                                    }
                                } else {
                                    // Get all resource URLs to events in current year-month container for friend
                                    Set<URI> eventResourceURLS = restController.getContainerFromFriend(accessGrant, friendPodURL + "events/" + currentYearMonth + "/");

                                    // Get each event resource and check if the event conflicts with current date
                                    boolean conflictFound = false;
                                    for (URI resourceURl : eventResourceURLS) {
                                        Event event = restController.getEventFromFriend(accessGrant, resourceURl.toString());
                                        // If event start date conflicts with current date and start time then set conflictFound to true
                                        LocalDate eventDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                        LocalTime eventStartTime = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        LocalTime eventEndTime = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                                        if (eventDate.isEqual(currentDate)) {
                                            if (isOverlapping(eventStartTime, eventEndTime, desiredStartTime, desiredEndTime)) {
                                                conflictFound = true;
                                                break;
                                            }
                                        } else {
                                            // Continue to next event if it is not on the same day
                                            continue;
                                        }
                                    }

                                    // If no conflict found then add current date along with desired start and end time to list of available time slots
                                    if (!conflictFound) {
                                        TimeSlot timeSlot = new TimeSlot(currentDate, desiredStartTime, desiredEndTime);
                                        availableTimeSlots.add(timeSlot);
                                    }
                                }

                                // Skip to the next date
                                currentDate = currentDate.plusDays(1);
                            }
                        } else {
                            System.out.println("No friend selected to find availability.");
                        }
                        spinner.setVisible(false);
                        return null;
                    }
                };

                findAvailability.setOnSucceeded(e->{
                    if (!availableTimeSlots.isEmpty()) {
                        // Launch FriendAvailabilityView popup
                        new FriendAvailabilityView(primaryStage, applicationContext, availableTimeSlots, friendData);
                    } else {
                        // Launch ErrorView popup
                        System.out.println("NO AVAILABLE TIME SLOT FOUND!");
                        new ErrorView(primaryStage, "No available time slot found!");
                    }
                });
                findAvailability.setOnFailed(e->{
                    Throwable throwable = findAvailability.getException();
                    System.err.println("Task failed with exception: " + throwable.getMessage());
                    new ErrorView(primaryStage, "Access has not been granted yet!");
                    gridPane.getChildren().remove(spinner);
                });

                new Thread(findAvailability).start();
            }
        };
        findButton.setOnAction(findAvailabilityEvent);

        Scene dialogScene = new Scene(gridPane, 600, 500);
        popUp.setScene(dialogScene);
        popUp.show();
    }

    /***
     * Utility method to check if two time periods are overlapping
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    public static boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
