package com.example.SyncUp.GUI.PopupViews;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorView {
    public ErrorView(Stage primaryStage, String errorMessage) {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);

        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red;"); // Set font size and color
        errorLabel.setAlignment(Pos.CENTER); // Center align text

        VBox vBox = new VBox(errorLabel);
        vBox.setAlignment(Pos.CENTER); // Center align VBox
        vBox.setSpacing(10);

        Scene scene = new Scene(vBox, 500, 300);
        popUp.setScene(scene);
        popUp.show();
    }
}
