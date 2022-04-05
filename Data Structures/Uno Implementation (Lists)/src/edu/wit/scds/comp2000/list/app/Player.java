
package edu.wit.scds.comp2000.list.app ;

import edu.wit.scds.comp2000.list.app.card.Card ;
import edu.wit.scds.comp2000.list.app.card.CardColor ;
import edu.wit.scds.comp2000.list.app.card.CardType ;
import edu.wit.scds.comp2000.list.app.pile.Hand ;

/**
 * Class to represent a player in UNO
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-11-18 Initial implementation
 */
public class Player
    {

    /** stores player's name */
    private final String name ;
    /** stores player's score */
    private int score ;
    /** stores player's hand */
    private final Hand hand ;

    /**
     * 1-arg constructor
     *
     * @param name
     *     name of player
     */
    public Player( String name )
        {
        this.name = name ;
        this.score = 0 ;
        this.hand = new Hand() ;

        } // end 1-arg constructor


    /**
     * Retrieves player's name
     *
     * @return player's name
     */
    public String getName()
        {
        return this.name ;

        } // end getName()


    /**
     * Retrieves player's score
     *
     * @return player's score
     */
    public int getScore()
        {
        return this.score ;

        } // end getScore()


    /**
     * Retrieves player's hand
     *
     * @return player's hand
     */
    public Hand getHand()
        {
        return this.hand ;

        } // end getHand()


    /**
     * Updates player's score
     *
     * @param points
     *     points to add to score
     */
    public void updateScore( int points )
        {
        this.score = this.score + points ;

        } // end updateScore()


    /**
     * Removes a card from the player's hand
     */
    public void playCard( Card card )
        {
        getHand().removeCard( card ) ;

        } // end playCard()


    /**
     * Used for testing
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // creating players
        System.out.println( "Creating players:" ) ;
        System.out.println( "Player 1" ) ;
        Player player1 = new Player( "Player 1" ) ;
        System.out.println( "Player 2" ) ;
        Player player2 = new Player( "Player 2" ) ;
        System.out.println( "Player 3" ) ;
        Player player3 = new Player( "Player 3" ) ;
        System.out.println() ;

        // testing getName()
        System.out.println( "Testing getName():" ) ;
        System.out.println( String.format( "player1: %s", player1.getName() ) ) ;
        System.out.println( String.format( "player2: %s", player2.getName() ) ) ;
        System.out.println( String.format( "player3: %s", player3.getName() ) ) ;
        System.out.println() ;

        // testing getScore()
        System.out.println( "Testing getScore():" ) ;
        System.out.println( "Giving player2 10 points" ) ;
        player2.updateScore( 10 ) ;
        System.out.println( "Giving player3 20 points" ) ;
        player3.updateScore( 20 ) ;
        System.out.println( String.format( "player1: %s", player1.getScore() ) ) ;
        System.out.println( String.format( "player2: %s", player2.getScore() ) ) ;
        System.out.println( String.format( "player3: %s", player3.getScore() ) ) ;
        System.out.println() ;

        // testing getHand()
        System.out.println( "Testing getHand()" ) ;
        Card redOne = new Card( CardColor.RED, CardType.ONE ) ;
        System.out.println( "player1 receiving redOne" ) ;
        player1.getHand().addCard( redOne ) ;
        System.out.println( "player2 receiving redOne" ) ;
        player2.getHand().addCard( redOne ) ;
        System.out.println( "player3 receiving redOne" ) ;
        player3.getHand().addCard( redOne ) ;
        System.out.println( String.format( "player1's hand: %s",
                                           player1.getHand() ) ) ;
        System.out.println( String.format( "player2's hand: %s",
                                           player2.getHand() ) ) ;
        System.out.println( String.format( "player3's hand: %s",
                                           player3.getHand() ) ) ;
        System.out.println() ;

        } // end main()
    }
// end class Player