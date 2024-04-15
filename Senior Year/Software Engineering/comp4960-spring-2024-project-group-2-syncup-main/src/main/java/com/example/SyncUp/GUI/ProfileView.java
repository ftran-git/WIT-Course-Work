package com.example.SyncUp.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ProfileView extends View{
    private VBox view;

    public ProfileView(){
        TextFlow solidIDPText = new TextFlow();
        Text solidIDPLabel=new Text("Solid Identity Provider: ");
        solidIDPLabel.setStyle("-fx-font-weight: bold");
        Text solidIDPValue=new Text(View.CURRENT_USER.getIDP());
        solidIDPValue.setStyle("-fx-font-weight: regular");
        solidIDPText.getChildren().addAll(solidIDPLabel, solidIDPValue);

        TextFlow solidWebIDText = new TextFlow();
        Text solidWebIDLabel=new Text("Solid WebID: ");
        solidWebIDLabel.setStyle("-fx-font-weight: bold");
        Text solidWebIDValue=new Text(CURRENT_USER.getWebID());
        solidWebIDValue.setStyle("-fx-font-weight: regular");
        solidWebIDText.getChildren().addAll(solidWebIDLabel, solidWebIDValue);

        TextFlow solidClientIDText = new TextFlow();
        Text solidClientIDLabel=new Text("Solid Client ID: ");
        solidClientIDLabel.setStyle("-fx-font-weight: bold");
        Text solidClientIDValue=new Text(CURRENT_USER.getClientID());
        solidClientIDValue.setStyle("-fx-font-weight: regular");
        solidClientIDText.getChildren().addAll(solidClientIDLabel, solidClientIDValue);

        //make this censored and add a view button
        HBox solidClientSecret = new HBox(10);
        Text solidClientSecretLabel = new Text("Solid Client Secret: ");
        solidClientSecretLabel.setStyle("-fx-font-weight: bold");
        Label solidClientSecretValue = new Label(CURRENT_USER.getClientSecret());
        solidClientSecretValue.setVisible(false);
        Button revealButton = new Button("Reveal Client Secret");
        revealButton.setOnMousePressed(event -> solidClientSecretValue.setVisible(true));
        revealButton.setOnMouseReleased(event -> solidClientSecretValue.setVisible(false));
        solidClientSecret.getChildren().addAll(solidClientSecretLabel, solidClientSecretValue, revealButton);

        TextFlow authFlowText = new TextFlow();
        Text authFlowLabel=new Text("Authorization Flow: ");
        authFlowLabel.setStyle("-fx-font-weight: bold");
        Text authFlowValue=new Text(CURRENT_USER.getAuthFlow());
        authFlowValue.setStyle("-fx-font-weight: regular");
        authFlowText.getChildren().addAll(authFlowLabel, authFlowValue);

        // Create grid pane to arrange components
        VBox userLoginInfo = new VBox();
        userLoginInfo.getChildren().addAll(solidIDPText, solidWebIDText, solidClientIDText, solidClientSecret, authFlowText);
        userLoginInfo.setPadding(new Insets(20));

        // Center grid pane
        userLoginInfo.setAlignment(Pos.TOP_LEFT);

        this.view = userLoginInfo;
    }

    public VBox getView(){return view;}
}