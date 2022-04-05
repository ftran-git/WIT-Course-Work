/**
 * Runs the game
 *
 * @author Fabio Tran
 */
// extends Application for GUI
public class BlackjackMain
    {

    public static void main( String[] args )
        {
         //launch(args);

        BlackjackEvents newGame = new BlackjackEvents() ;
        newGame.gameInstructions() ;
        newGame.getPlayersAndNames() ;

        do
            {

            newGame.getBets() ;
            newGame.initializeDeckAndShuffle() ;
            newGame.dealCards() ;
            newGame.displayCards() ;
            newGame.checksForSplit() ;
            newGame.doubleDown() ;
            newGame.hitOrStand() ;
            newGame.dealerEvents() ;
            newGame.distrubuteBets() ;
            newGame.displayUpdatedBank() ;
            newGame.endGame() ;

            }
        while ( newGame.newRound() ) ;

        }

// @Override
// public void start(Stage primaryStage) throws Exception {
//
// Parent root = FXMLLoader.load(getClass().getResource("BlackjackGUI.fxml"));
// Scene scene = new Scene(root);
// primaryStage.setTitle("MyExampleApp");
// primaryStage.setScene(scene);
// primaryStage.show();


// }

    }