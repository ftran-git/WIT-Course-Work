/**
 * Runs the game
 *
 * @author Fabio Tran
 */
public class BlackjackMain
    {

    public static void main( String[] args )
        {

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

    }
