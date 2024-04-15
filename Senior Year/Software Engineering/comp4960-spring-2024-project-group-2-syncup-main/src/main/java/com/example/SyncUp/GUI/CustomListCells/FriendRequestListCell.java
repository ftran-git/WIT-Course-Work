package com.example.SyncUp.GUI.CustomListCells;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.GUI.PopupViews.AddFriendBackView;
import com.example.SyncUp.GUI.PopupViews.PendingFriendsView;
import com.example.SyncUp.Objects.AccessRequestData;
import com.inrupt.client.accessgrant.AccessRequest;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FriendRequestListCell extends ListCell<AccessRequestData> {
    private final Button acceptButton;
    private final Button denyButton;

    public FriendRequestListCell(Stage primaryStage, ConfigurableApplicationContext applicationContext) {
        // Get RESTController instance
        RESTController restController = applicationContext.getBean(RESTController.class);

        // Create Accept button
        acceptButton = new Button("Accept");
        // Set action for the "Accept" button
        acceptButton.setOnAction(event -> {
            // Retrieve AccessRequestData for current cell
            AccessRequestData accessRequestData = getItem();
            // Get AccessRequest for current ListView record
            AccessRequest accessRequest = accessRequestData.getAccessRequest();

            // Print Friend's WebID for current ListView record
            String friendWebID = accessRequestData.toString();
            System.out.println("Accepted: " + friendWebID);

            // Create and send AccessGrant using AccessRequest
            restController.createAccessGrant(accessRequest);

            // Reload popup by closing and reopening window
            Stage stage = (Stage) acceptButton.getScene().getWindow();
            stage.close();

            // If accepted friend has not already been added back then launch AddFriendBackView popup
            Set<URI> friendResourceURLs = restController.getContainer(restController.solidPodURL + "friends/");
            List<String> webIDs = new ArrayList<>();
            for(URI friendResourceURL : friendResourceURLs) {
                String webID = restController.getFriend(friendResourceURL.toString()).getWebID();
                webIDs.add(webID);
            }
            if (!webIDs.contains(friendWebID)) {
                new AddFriendBackView(primaryStage, stage, applicationContext, friendWebID);
            } else {
                try {
                    new PendingFriendsView(primaryStage, applicationContext);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Create Accept Button
        denyButton = new Button("Deny");
        // Set action for the "Deny" button
        denyButton.setOnAction(event -> {
            // Retrieve AccessRequestData for current cell
            AccessRequestData accessRequestData = getItem();
            // Get AccessRequest for current ListView record
            AccessRequest accessRequest = accessRequestData.getAccessRequest();

            // TODO Revoke AccessRequest
            //restController.revokeAccessGrant(accessRequest.getCreator().toString(), restController.solidWebID);

            // Reload popup by closing and reopening window
            Stage stage = (Stage) denyButton.getScene().getWindow();
            stage.close();
            try {
                new PendingFriendsView(primaryStage, applicationContext);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //@Override
    protected void updateItem(AccessRequestData item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null); // Clear the default text to avoid duplication
            Label label = new Label(item.toString()); // Create label for URI text
            HBox buttonsBox = new HBox(10, label, acceptButton, denyButton); // Create HBox to contain label and buttons
            buttonsBox.setAlignment(Pos.CENTER_RIGHT); // Align the HBox content to the right
            setGraphic(buttonsBox); // Set the HBox as the graphic for the cell
        }
    }
}
