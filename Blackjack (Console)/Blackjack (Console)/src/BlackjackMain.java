import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Runs the game 
 * 
 * @author Fabio Tran
 */

public class BlackjackMain extends Application {
	
	public static void main(String[] args) {
		launch(args);
		
		BlackjackEvents newGame = new BlackjackEvents();
		newGame.gameInstructions();
		newGame.getPlayersAndNames();
		
		do {
			
			newGame.getBets();
			newGame.initializeDeckAndShuffle();
			newGame.dealCards();
			newGame.displayCards();
			newGame.checksForSplit();
			newGame.doubleDown();
			newGame.hitOrStand();
			newGame.dealerEvents();
			newGame.distrubuteBets();
			newGame.displayUpdatedBank();
			newGame.endGame();

		} while(newGame.newRound());
		
			
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("BlackjackGUI.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("MyExampleApp");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}

	
}