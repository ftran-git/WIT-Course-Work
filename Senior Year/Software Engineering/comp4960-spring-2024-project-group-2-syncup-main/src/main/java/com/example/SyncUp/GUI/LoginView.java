package com.example.SyncUp.GUI;

import com.example.SyncUp.Controllers.RESTController;
import com.example.SyncUp.Objects.User;
import com.example.SyncUp.RDFResources.Container;
import com.example.SyncUp.RDFResources.Event;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class LoginView extends View{
    private GridPane view;

    public LoginView(Stage primaryStage, ConfigurableApplicationContext applicationContext){
        Label solidIDPLabel = new Label("Solid Identity Provider:");
        Label solidWebIDLabel = new Label("Solid WebID:");
        Label solidClientIDLabel = new Label("Solid Client ID:");
        Label solidClientSecretLabel = new Label("Solid Client Secret:");
        Label authFlowLabel = new Label("Authorization Flow:");

        // Create input fields for each login field
        String IDP_providers[] = { "https://login.inrupt.com/" };
        ComboBox idpComboBox = new ComboBox(FXCollections.observableArrayList(IDP_providers));
        TextField solidWebIDTextField = new TextField();
        TextField solidClientIDTextField = new TextField();
        PasswordField solidClientSecretTextField = new PasswordField();
        String authFlows[] = { "client_secret_basic" };
        ComboBox authFlowComboBox = new ComboBox(FXCollections.observableArrayList(authFlows));

        // Create login button
        Button loginButton = new Button("Login");

        // Create grid pane to arrange components
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add components to grid pane
        gridPane.add(solidIDPLabel, 0, 0);
        gridPane.add(idpComboBox, 1, 0);
        gridPane.add(solidWebIDLabel, 0, 1);
        gridPane.add(solidWebIDTextField, 1, 1);
        gridPane.add(solidClientIDLabel, 0, 2);
        gridPane.add(solidClientIDTextField, 1, 2);
        gridPane.add(solidClientSecretLabel, 0, 3);
        gridPane.add(solidClientSecretTextField, 1, 3);
        gridPane.add(authFlowLabel, 0, 4);
        gridPane.add(authFlowComboBox, 1, 4);
        gridPane.add(loginButton, 1, 5);

        // Center grid pane
        gridPane.setAlignment(Pos.CENTER);

        this.view = gridPane;

        // Event Handler for login button
        EventHandler<ActionEvent> login = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                ProgressIndicator spinner = new ProgressIndicator();
                gridPane.add(spinner, 1, 6);

                // Get text field inputs from user
                String solidIDP = (String)idpComboBox.getValue();
                String solidWebID = solidWebIDTextField.getText();
                String solidClientID = solidClientIDTextField.getText();
                String solidClientSecret = solidClientSecretTextField.getText();
                String authFlow = (String)authFlowComboBox.getValue();

                Task<Void> solidLogin = new Task<Void>() {
                    protected Void call() throws Exception {
                        // Get RESTController instance
                        RESTController restController = applicationContext.getBean(RESTController.class);
                        // Update EventController credential variables based on the user input
                        restController.updateState(solidIDP, solidClientID, solidClientSecret, authFlow);

                        // Get Pod URLs using WebID
                        System.out.println("Grabbing User's Solid Pod URLs:");
                        Set<URI> responseOne = restController.getPods(solidWebID);

                        // Get first Pod URL from set returned
                        URI URI = null;
                        String podURL = "";
                        for (URI uri : responseOne) {
                            URI = uri;
                            podURL = uri.toString();
                            break;
                        }
                        System.out.println(podURL);

                        // Use Pod URL to attempt any CRUD operation for authentication purposes
                        System.out.println("Testing Credentials:");
                        Event responseTwo = restController.getEvent(podURL);
                        if (responseTwo == null) {
                            spinner.setVisible(false);
                            // Null response indicates UnauthorizedException
                            System.out.println("ERROR: UNABLE TO AUTHORIZE");
                            throw new Exception("UNABLE TO AUTHORIZE");
                        } else {
                            // Non-null response indicates successful authorization
                            System.out.println("AUTHORIZATION SUCCESS");

                            // Save Solid WebID in RESTController instance for other operations during session
                            restController.setSolidWebID(solidWebID);
                            // Save Solid Pod URL in RESTController instance for other operations during session
                            restController.setSolidPodURL(podURL);

                            // Create any container in user's Solid Pod if necessary for SyncUp functions
                            try {
                                System.out.println("Creating Containers in User's Solid Pod (if missing):");
                                // Events container
                                Container eventsContainer = new Container(new URI(restController.solidPodURL + "events/"),null);
                                restController.createContainer(eventsContainer);
                                // Friends container
                                Container friendsContainer = new Container(new URI(restController.solidPodURL + "friends/"),null);
                                restController.createContainer(friendsContainer);
                                // Pending Friends container
                                Container pendingFriendsContainer = new Container(new URI(restController.solidPodURL + "pendingFriends/"),null);
                                restController.createContainer(pendingFriendsContainer);
                            } catch (URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        return null;
                    }
                };

                solidLogin.setOnSucceeded(e->{
                    // Update scene from login to calendar view if 404 error not found which indicates successful authentication
                    Stage currentStage = (Stage)primaryStage.getScene().getWindow();
                    currentStage.setScene(View.getCalendarView(primaryStage));
                    System.out.println("WELCOME TO SYNCUP!");
                    View.CURRENT_USER = new User(solidIDP, solidWebID, solidClientID, solidClientSecret, authFlow);
                });
                solidLogin.setOnFailed(e->{
                    Throwable throwable = solidLogin.getException();
                    System.err.println("Task failed with exception: " + throwable.getMessage());
                    gridPane.getChildren().remove(spinner);
                });

                new Thread(solidLogin).start();

            }
        };
        // Set Event Handler to login buttons
        loginButton.setOnAction(login);
    }

    public GridPane getView() {
        return view;
    }

}