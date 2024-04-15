package com.example.SyncUp.GUI.PopupViews;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.GUI.CustomListCells.FriendRequestListCell;
import com.example.SyncUp.Objects.AccessRequestData;
import com.example.SyncUp.Objects.FriendData;
import com.inrupt.client.accessgrant.AccessGrant;
import com.inrupt.client.accessgrant.AccessRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PendingFriendsView {

    public PendingFriendsView(Stage primaryStage, ConfigurableApplicationContext applicationContext) throws InterruptedException {
        // Get RESTController instance
        RESTController restController = applicationContext.getBean(RESTController.class);

        // Create popup
        final Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);

        // Create gridPane to store components
        GridPane gridPane = new GridPane();

        // Create Label for left list header
        Label leftHeader = new Label("Outgoing Friend Requests");
        GridPane.setHalignment(leftHeader, HPos.CENTER);
        gridPane.add(leftHeader, 0, 0);
        // Get all resources from pendingFriends container
        Set<URI> resourceURLs = restController.getContainer(restController.solidPodURL + "pendingFriends/");
        // Create FriendData objects to store resourceURLs and generate specific toString() method to format and display name
        List<FriendData> friendData = new ArrayList<>();
        for (URI resourceURL : resourceURLs)  {
            String webID = restController.getFriend(resourceURL.toString()).getWebID();
            friendData.add(new FriendData(resourceURL, URI.create(webID)));
        }
        // Store FriendData objects in ListView to display
        ObservableList<FriendData> friendDataItems = FXCollections.observableArrayList(friendData);
        // Create the ListView
        ObservableList<FriendData> records = FXCollections.observableArrayList(friendDataItems);
        ListView<FriendData> listViewLeft = new ListView<>(records);
        // Add listViewLeft to gridPane at column 0
        gridPane.add(listViewLeft, 0, 1);

        // Create Label for middle list header
        Label middleHeader = new Label("Incoming Friend Requests");
        GridPane.setHalignment(middleHeader, HPos.CENTER);
        gridPane.add(middleHeader, 1, 0);
        // Get any pending AccessRequests
        List<AccessRequest> accessRequests = restController.getAccessRequest(restController.solidWebID);
        // Remove any AccessRequests that already was used to create an AccessGrant
        Iterator<AccessRequest> iterator = accessRequests.iterator();
        while (iterator.hasNext()) {
            AccessRequest accessRequest = iterator.next();
            String requester = accessRequest.getCreator().toString();
            AccessGrant accessGrant = restController.getAccessGrant(restController.solidWebID, requester);
            if (accessGrant != null) {
                iterator.remove();
            }
        }
        // Create AccessRequestData objects to store both request creator URI and AccessRequest object
        List<AccessRequestData> accessRequestData = new ArrayList<>();
        for (AccessRequest accessRequest : accessRequests) {
            accessRequestData.add(new AccessRequestData(accessRequest.getCreator(), accessRequest));
        }
        // Store AccessRequestData objects in ListView to display
        ObservableList<AccessRequestData> accessRequestDataItems = FXCollections.observableArrayList(accessRequestData);
        ListView<AccessRequestData> listViewMiddle = new ListView<>(accessRequestDataItems);
        // Set custom ListCell to add Accept and Deny buttons on each record in the ListView
        listViewMiddle.setCellFactory(param -> new FriendRequestListCell(primaryStage, applicationContext));
        // Add listViewMiddle to gridPane at column 1
        gridPane.add(listViewMiddle, 1, 1);

        /** Commenting out Add Friend Back list because it is buggy **/

        /*
        // Create Label for right list header
        Label rightHeader = new Label("Add Friend Back?");
        GridPane.setHalignment(rightHeader, HPos.CENTER);
        gridPane.add(rightHeader, 2, 0);
        // Get all AccessGrants
        List<AccessGrant> accessGrants = restController.getAllAccessGrant(restController.solidWebID);
        // Get all WebID's of friends in friends container
        Set<URI> friendResourceURLs = restController.getContainer(restController.solidPodURL + "friends/");
        List<String> friendWebIDs = new ArrayList<>();
        for (URI friendResourceURL : friendResourceURLs) {
            Friend friend = restController.getFriend(friendResourceURL.toString());
            friendWebIDs.add(friend.getWebID());
        }
        // Check if recipient of any AccessGrant is in Friends list already if not add to Add Friend Back list
        List<String> addFriendBackList = new ArrayList<>();
        for (AccessGrant accessGrant : accessGrants) {
            if (!friendWebIDs.contains(accessGrant.getRecipient())){
                addFriendBackList.add(accessGrant.getRecipient().toString().replace("Optional[", "").replace("]", ""));
            }
        }
        // Store FriendData objects in ListView to display
        ObservableList<String> addFriendBackItems = FXCollections.observableArrayList(addFriendBackList);
        // Create the ListView
        ObservableList<String> addFriendBackRecords = FXCollections.observableArrayList(addFriendBackItems);
        ListView<String> listViewRight = new ListView<>(addFriendBackRecords);
        listViewRight.setCellFactory(param -> new AddFriendBackListCell(primaryStage, applicationContext));
        gridPane.add(listViewRight, 2, 1);
        */

        /*
        // Set constraints for column widths
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(33); // 33% width for each column
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(33); // 33% width for each column
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(33); // 33% width for each column
        gridPane.getColumnConstraints().addAll(column1, column2, column3);
         */

        // Set constraints for column widths
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50); // 33% width for each column
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50); // 33% width for each column
        gridPane.getColumnConstraints().addAll(column1, column2);

        // Create the scene with the gridPane
        Scene dialogScene = new Scene(gridPane, 750, 300);
        popUp.setScene(dialogScene);
        popUp.show();
    }
}
