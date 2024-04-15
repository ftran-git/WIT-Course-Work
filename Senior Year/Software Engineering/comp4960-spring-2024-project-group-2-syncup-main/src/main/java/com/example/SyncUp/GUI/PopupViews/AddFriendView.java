package com.example.SyncUp.GUI.PopupViews;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.Exceptions.DuplicateFriendException;
import com.example.SyncUp.RDFResources.Friend;
import com.inrupt.client.solid.NotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.jena.sparql.exec.RowSet;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.SyncUp.GUI.View.getFriendsView;

public class AddFriendView {

    public AddFriendView(Stage primaryStage, ConfigurableApplicationContext applicationContext) {
        final Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);

        // Create field labels
        Label firstName = new Label("First Name:");
        Label lastName = new Label("Last Name:");
        Label webID = new Label("WebID:");
        // Create input fields
        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        TextField webIDTextField = new TextField();
        // Create save button
        Button saveButton = new Button("Save");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add components to grid pane
        gridPane.add(firstName, 0, 0);
        gridPane.add(firstNameTextField, 1, 0);
        gridPane.add(lastName, 0, 1);
        gridPane.add(lastNameTextField, 1, 1);
        gridPane.add(webID, 0, 2);
        gridPane.add(webIDTextField, 1, 2);
        gridPane.add(saveButton, 1, 3);

        // Event Handler for login button
        EventHandler<ActionEvent> saveFriendEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // Get RESTController instance
                RESTController restController = applicationContext.getBean(RESTController.class);

                // Get field inputs from user
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String webID = webIDTextField.getText();

                // Validate WebID before creating friend
                try {
                    // Check WebID entered is not a duplicate of an existing Friend
                    System.out.println("CHECKING FOR DUPLICATE WebID!");
                    Set<URI> resourceURLs = restController.getContainer(restController.solidPodURL + "friends/");
                    for (URI resourceURL : resourceURLs) {
                        Friend friend = restController.getFriend(resourceURL.toString());
                        if (friend.getWebID().equals(webID)) {
                            // Throw exception if duplicate found
                            throw new DuplicateFriendException();
                        }
                    }

                    // Check WebID is valid by using getPods() (URL retrieved indicates a valid WebID entered)
                    System.out.println("CHECKING FOR VALID WebID!");
                    Set<URI> response = restController.getPods(webID);
                    // Get first Pod URL from set returned
                    URI URI = null;
                    String podURL = "";
                    for (URI uri : response) {
                        URI = uri;
                        podURL = uri.toString();
                        break;
                    }

                    // Create Friend resource and save to user's solid pod in pendingFriends container (Only after acceptance will this resource be moved to friends container)
                    System.out.println("SAVING FRIEND RESOURCE TO PENDING FRIENDS CONTAINER");
                    URI resourceURL;
                    try {
                        resourceURL = new URI(restController.solidPodURL + "pendingFriends/" + lastName + "-" + firstName);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    Friend newFriend = new Friend(resourceURL, firstName, lastName, webID);
                    restController.createFriend(newFriend);

                    // List of resource(s) to request access to (only events container should be requested at this time)
                    List<String> resourceUrls = List.of(new String[]{podURL + "events/"});
                    // Create AccessRequest to friend's Solid Pod's "Events" container
                    System.out.println("SENDING ACCESS REQUEST");
                    restController.createAccessRequest(resourceUrls, webID);

                    // Close popup window
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();

                    // Refresh scene
                    Stage currentStage = (Stage)primaryStage.getScene().getWindow();
                    currentStage.setScene(getFriendsView(primaryStage));
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: INVALID WebID ENTERED!");
                    new ErrorView(primaryStage, "Invalid WebID entered!");
                } catch (NotFoundException e) {
                    System.out.println("ERROR: WebID NOT FOUND!");
                    new ErrorView(primaryStage, "No resource found at WebID entered!");
                } catch (DuplicateFriendException e) {
                    System.out.println("ERROR: DUPLICATE WebID ENTERED!");
                    new ErrorView(primaryStage, "Friend already exists!");
                } catch (Exception e) {
                    System.out.println("ERROR: SERVICE NAME UNKNOWN");
                    new ErrorView(primaryStage, "Solid service provided is unknown!");
                }
            }
        };
        saveButton.setOnAction(saveFriendEvent);

        Scene dialogScene = new Scene(gridPane, 500, 300);
        popUp.setScene(dialogScene);
        popUp.show();
    }
}
