package com.example.SyncUp.GUI;

import com.example.SyncUp.GUI.PopupViews.CreateEventView;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {

    // Date associated with this pane
    private LocalDate date;

    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(Stage primaryStage, ConfigurableApplicationContext applicationContext, Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            new CreateEventView(primaryStage, applicationContext, date);
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
