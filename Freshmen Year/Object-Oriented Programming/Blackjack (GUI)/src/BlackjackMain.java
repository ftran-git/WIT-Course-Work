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
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

/**
 * Runs the game 
 * 
 * @author Fabio Tran
 */

public class BlackjackMain extends Application implements Initializable {
	
    @FXML
    private Button enterBtn;
    
    @FXML
    private Button startBtn;
    
    @FXML
    private Button newRoundBtn;

    @FXML
    private TextField enterTxtField;

    @FXML
    private TextArea enterTxtArea;
    
    @FXML
    private Text enterTxt;
    
	BlackjackEvents newGame = new BlackjackEvents();
	int eventCount = 0;
	boolean grabNames = false;
	boolean grabBets = false;
	int betCounter = 0;
	int hitCounter = 0;
	boolean first = true;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("BlackjackGUI.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Blackjack");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		enterBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				String txt;
				txt = enterTxtField.getText();
				
				//grabs amount of players
				if(eventCount == 1) {
						
						if(txt.equals("1") || txt.equals("2") || txt.equals("3") || txt.equals("4") || txt.equals("5") || txt.equals("6")) {
							
							newGame.players = Integer.parseInt(txt);
							
							for(int i = 1; i <= newGame.players; i++) {
								
								enterTxtArea.appendText("\n");
								enterTxtArea.appendText("What is player " + i + " name?");
								
							}
							
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText("Enter each name one by one");
							
							eventCount++;
							
						} else {
							
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText("Invalid number of players, try again!");
							
						}
				//gets names
				} else if (eventCount == 2) {
					
					if(grabNames == false && first == true) {
						
						String msg = enterTxtField.getText();
						newGame.playersArray.add(new Player(msg));
						
					}
					
					if((newGame.playersArray.size() == newGame.players)) {
						
						grabNames = true;
						
					}	
					
					if(grabBets == false && grabNames == true) {
						
						for(int i = 0; i < newGame.players; i++) {
							
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(i).name + ", how much do you want to bet? You have " + newGame.playersArray.get(i).money + " credits");
							
						}
						
						eventCount++;
						first = false;
						
					}
					
				} else if (eventCount == 3) {
					
					String msg = enterTxtField.getText();
					
					try {
						
						newGame.playersArray.get(betCounter).hand.bet(Integer.parseInt(msg));	
						
					} catch (NumberFormatException ex) {
				
					}
					
					betCounter++;
	
					if(betCounter == newGame.players) {
						
						grabBets = true;
						newGame.dealCards();
						
						
						enterTxtArea.appendText("\n");
						enterTxtArea.appendText("The dealer's face up card is " + newGame.dealer.hand.get(0));
						
						for(int i = 0; i < newGame.playersArray.size(); i++) {
							
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(i).showHand());
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(i).hand.getTotal());
							
						}
						
						for(int i = 0; i < newGame.playersArray.size(); i++) {
						
								enterTxtArea.appendText("\n");
								enterTxtArea.appendText(newGame.playersArray.get(i).name + ", do you want to Hit (H) or Stand (S)?");
							
						}
					
					eventCount++;
					
					}
					
				} else if (eventCount == 4) {
					
					String a = enterTxtField.getText();
			
					if( !newGame.playersArray.get(hitCounter).doneHitOrStand) {
							
						if(newGame.playersArray.get(hitCounter).hand.finalTotal > 21) {
								
							newGame.playersArray.get(hitCounter).hand.bust = true;
							newGame.playersArray.get(hitCounter).doneHitOrStand = true;
							hitCounter++;
								
						} else if (newGame.playersArray.get(hitCounter).hand.finalTotal == 21) {
								
							newGame.playersArray.get(hitCounter).doneHitOrStand = true;
							hitCounter++;
								
						} else if (a.equals("H") || a.equals("h")) {
									
							newGame.playersArray.get(hitCounter).hand.addCard(newGame.deck.getCard());
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(hitCounter).showHand());
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(hitCounter).hand.getTotal());
							
						} else if(a.equals("S") || a.equals("s")) {
							
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(hitCounter).name +" stands");
							newGame.playersArray.get(hitCounter).doneHitOrStand = true;
							hitCounter++;
										
						}
										
					}	
								
					//runs dealer events and displays updated money		
					if((hitCounter == newGame.players) && (newGame.playersArray.get(hitCounter-1).doneHitOrStand = true)) {
						
						eventCount++;
						newGame.dealerEvents();
						enterTxtArea.appendText("\n");
						enterTxtArea.appendText("Dealer has: " + newGame.dealer.toString());
						enterTxtArea.appendText("\n");
						enterTxtArea.appendText(Integer.toString(newGame.dealer.finalTotal));
						newGame.distributeBets();
									
						for(int i = 0; i < newGame.playersArray.size(); i++) {
										
							if(newGame.playersArray.get(i).hand.result.equals("win")) {
											
								newGame.playersArray.get(i).addMoney(newGame.playersArray.get(i).hand.getBet());
								enterTxtArea.appendText("\n");
								enterTxtArea.appendText(newGame.playersArray.get(i).name + " has: " + newGame.playersArray.get(i).money);
											
							} 
							
							else if(newGame.playersArray.get(i).hand.result.equals("lose")) {
											
								newGame.playersArray.get(i).minusMoney(newGame.playersArray.get(i).hand.getBet());
								enterTxtArea.appendText("\n");
								enterTxtArea.appendText(newGame.playersArray.get(i).name + " has: " + newGame.playersArray.get(i).money);
											
							} else if(newGame.playersArray.get(i).hand.result.equals("draw")) {
											
								enterTxtArea.appendText("\n");
								enterTxtArea.appendText(newGame.playersArray.get(i).name + " has: " + newGame.playersArray.get(i).money);
											
							}
									
						}
					}
								
				} 
						
			}
			
		});
	
		startBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(eventCount == 0) {
					
					eventCount++;
					enterTxtArea.setText(newGame.gameInstructions());
					newGame.initializeDeckAndShuffle();
					enterTxtArea.appendText("\n");
					enterTxtArea.appendText("How many players (Up to 6)?");
					
				}
			}
			
		});
		
		newRoundBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(eventCount == 5) {
					
					newGame.initializeDeckAndShuffle();
					
					for(int i = 0; i < newGame.playersArray.size(); i++) {
						
						if(newGame.playersArray.get(i).money<=0) {
							enterTxtArea.appendText("\n");
							enterTxtArea.appendText(newGame.playersArray.get(i).name + " you have no more money, game over!");
							newGame.playersArray.remove(i);
							i = i - 1;
							
						}
			
					}
					
					if(newGame.playersArray.size()!=0) {
						
						enterTxtArea.appendText("\n");
						enterTxtArea.appendText("NEW ROUND");
						
						for(int a = 0; a < newGame.playersArray.size();a++) {
							
							newGame.playersArray.get(a).hand.clearHand();
							newGame.dealer.clearHand();
							newGame.playersArray.get(a).doneHitOrStand = false;
						
						}
					
					}
					
					newGame.players = newGame.playersArray.size();
					eventCount = 2; 
					grabNames = true;
					grabBets = false;
					betCounter = 0;
					hitCounter = 0;
						
				}
					
			}
				
		});
	
	}
	
	public static void main(String[] args) {
		
		launch(args);	
		
	}

	
}