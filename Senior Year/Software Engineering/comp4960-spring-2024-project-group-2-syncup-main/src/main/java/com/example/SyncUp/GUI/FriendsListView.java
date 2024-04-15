package com.example.SyncUp.GUI;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.GUI.PopupViews.AddFriendView;
import com.example.SyncUp.GUI.PopupViews.ErrorView;
import com.example.SyncUp.GUI.PopupViews.FindAvailabilityView;
import com.example.SyncUp.GUI.PopupViews.PendingFriendsView;
import com.example.SyncUp.Objects.FriendData;
import com.example.SyncUp.RDFResources.Friend;
import com.inrupt.client.accessgrant.AccessGrant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.SyncUp.GUI.View.getFriendsView;

public class FriendsListView extends View{
    private GridPane view;

    /***
     * Create a friends list view
     */
    public FriendsListView(Stage primaryStage, ConfigurableApplicationContext applicationContext) {
        // Get RESTController instance
        RESTController restController = applicationContext.getBean(RESTController.class);

        // Get resourceURLs to any Friend in pendingFriends container
        System.out.println("UPDATING FRIENDS LIST IF ANY REQUEST ACCEPTANCE HAD OCCURRED!");
        Set<URI> resourceURLs = restController.getContainer(restController.solidPodURL + "pendingFriends/");
        // Check if any Friend in pendingFriends container has an associated AccessGrant received
        for (URI resourceURL : resourceURLs) {
            // Get Friend resource using resourceURL
            Friend friend = restController.getFriend(resourceURL.toString());

            // Look for AccessGrant associated to Friend's WebID if present
            AccessGrant accessGrant = restController.getAccessGrant(friend.getWebID(), restController.solidWebID);

            // "Move" Friend resource from pendingFriends to friends container if AccessGrant is present
            if (accessGrant != null) {
                // Delete Friend resource from pendingFriends container
                restController.deleteFriend(resourceURL.toString());

                // Create same Friend resource but store in friends container instead of pendingFriends
                restController.createFriend(new Friend(
                        URI.create(restController.solidPodURL + "friends/" + friend.getLastName() + "-" + friend.getFirstName()),
                        friend.getFirstName(),
                        friend.getLastName(),
                        friend.getWebID())
                );
            }
        }

        // Get all resources from Friends container
        resourceURLs = restController.getContainer(restController.solidPodURL + "friends/");

        // Create FriendData objects to store resourceURLs and generate specific toString() method to format and display name
        List<FriendData> friendData = new ArrayList<>();
        for (URI resourceURL : resourceURLs)  {
            String webID = restController.getFriend(resourceURL.toString()).getWebID();
            friendData.add(new FriendData(resourceURL, URI.create(webID)));
        }

        // Store FriendData objects in ListView to display
        ObservableList<FriendData> items = FXCollections.observableArrayList(friendData);

        // Create the ListView
        System.out.println("POPULATING FRIENDS LIST!");
        ObservableList<FriendData> records = FXCollections.observableArrayList(items);
        ListView<FriendData> listView = new ListView<>(records);
        listView.setPrefSize(1000, 600); // Set preferred size

        // Add handler to ListView selection
        listView.setOnMouseClicked(event -> {
            String selectedFriend = listView.getSelectionModel().getSelectedItem().toString();
            if (selectedFriend != null) {
                // Print the selected friend
                System.out.println("Selected friend: " + selectedFriend);
            }
        });

        // Create the "Add" button
        Button addButton = new Button("Add");
        // Set action for the "Add" button
        EventHandler<ActionEvent> addFriend = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Launch AddFriendView popup
                new AddFriendView(primaryStage, applicationContext);
            }
        };
        addButton.setOnAction(addFriend);

        // Create the "Delete" button
        Button deleteButton = new Button("Delete");
        // Set action for the "Delete" button
        EventHandler<ActionEvent> deleteFriend = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Get name of selected friend from ListView
                String selectedFriend = listView.getSelectionModel().getSelectedItem().toString();

                if (selectedFriend != null) {
                    // Print the selected friend to delete
                    System.out.println("Selected friend to delete: " + selectedFriend);

                    // Delete friend from Solid Pod
                    String[] temp = selectedFriend.replace(" ", "").split(",");
                    restController.deleteFriend(restController.solidPodURL + "/friends/" + temp[0] + "-" + temp[1]);

                    // TODO Revoke users access to friend's resources
                    //restController.revokeAccessGrant(listView.getSelectionModel().getSelectedItem().getWebID().toString(), restController.solidWebID);

                    // Refresh scene
                    Stage currentStage = (Stage)primaryStage.getScene().getWindow();
                    currentStage.setScene(getFriendsView(primaryStage));
                } else {
                    System.out.println("No friend selected to delete.");
                }
            }
        };
        deleteButton.setOnAction(deleteFriend);

        // Create the "View" button
        Button viewButton = new Button("View");
        // Set action for the "Add" button
        EventHandler<ActionEvent> viewFriend = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Get Friend's webID from ListView
                URI friendWebID = listView.getSelectionModel().getSelectedItem().getWebID();

                // Get Friend's Pod URL through their WebID
                Set<URI> response = restController.getPods(friendWebID.toString());
                // Get first Pod URL from set returned
                URI URI = null;
                String friendPodURL = "";
                for (URI uri : response) {
                    URI = uri;
                    friendPodURL = uri.toString();
                    break;
                }

                // Check if proper AccessGrant exists
                try {
                    // Get AccessGrant
                    AccessGrant accessGrant = restController.getAccessGrant(friendWebID.toString(), restController.solidWebID);

                    // Get FriendData object for current friend to pass to FriendCalendarView which contains its WebID and Display Name
                    FriendData friendData = listView.getSelectionModel().getSelectedItem();

                    // Switch to FriendCalendarView scene
                    Stage currentStage = (Stage)primaryStage.getScene().getWindow();
                    currentStage.setScene(View.getFriendCalendarView(primaryStage, friendData, friendPodURL));
                } catch (Exception e) {
                    System.out.println("ERROR: NO ACCESSGRANT FOUND!");
                    new ErrorView(primaryStage, "Access has not been granted yet!");
                }
            }
        };
        viewButton.setOnAction(viewFriend);

        // Create the "Find Availability" button
        Button findAvailabilityButton = new Button("Find Availability");
        // Set action for the "Add" button
        EventHandler<ActionEvent> findAvailability = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Grab FriendData object for current cell
                FriendData friendData = listView.getSelectionModel().getSelectedItem();

                // Launch FindAvailabilityView popup
                new FindAvailabilityView(primaryStage, applicationContext, friendData);
            }
        };
        findAvailabilityButton.setOnAction(findAvailability);

        // Create the "Pending" button
        Button pendingButton = new Button("Pending");
        // Set action for the "Pending" button
        EventHandler<ActionEvent> pendingAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Launch PendingFriendsView popup
                try {
                    new PendingFriendsView(primaryStage, applicationContext);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        pendingButton.setOnAction(pendingAction);

        // Create an HBox to hold add, delete, and view buttons
        HBox buttonsBox = new HBox();
        buttonsBox.getChildren().addAll(addButton, deleteButton, viewButton, findAvailabilityButton);
        buttonsBox.setSpacing(20); // Set spacing between buttons
        buttonsBox.setAlignment(Pos.CENTER); // Align buttons to the center horizontally

        // Create a VBox to hold the ListView, buttons box, and pending button
        VBox vBox = new VBox();
        vBox.getChildren().addAll(listView, buttonsBox, pendingButton);
        vBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Adjust spacing margins
        VBox.setMargin(pendingButton, new Insets(10, 10, 10, 10));
        VBox.setMargin(buttonsBox, new Insets(10, 10, -35, 10));

        // Create the root layout
        GridPane root = new GridPane();
        root.getChildren().add(vBox);

        // Set view
        this.view = root;
    }

    public GridPane getView() {
        return view;
    }
}