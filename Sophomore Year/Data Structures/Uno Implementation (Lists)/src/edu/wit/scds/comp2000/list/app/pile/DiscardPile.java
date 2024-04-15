
package edu.wit.scds.comp2000.list.app.pile ;

import edu.wit.scds.comp2000.list.app.card.Card ;
import edu.wit.scds.comp2000.list.app.card.CardColor ;
import edu.wit.scds.comp2000.list.app.card.CardType ;

import java.util.ArrayList ;
import java.util.List ;

/**
 * Class to represent UNO discard pile
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-12-01 Initial implementation
 */
public class DiscardPile extends Pile
    {

    /** stores last played card */
    private Card lastPlayed ;

    /*
     * default constructor
     */
    public DiscardPile()
        {
        super() ;
        this.lastPlayed = null ;

        } // end default constructor


    /**
     * Retrieves last played card
     *
     * @return card
     */
    public Card getLastPlayed()
        {
        return this.lastPlayed ;

        } // end getLastPlayed()


    /**
     * Adds card to discard pile and updates last played card
     *
     * @param card
     *     card to add
     */
    @Override
    public void addCard( Card card )
        {
        super.addCard( card ) ;
        // updates last played
        this.lastPlayed = card ;

        } // end discardCard()


    /**
     * Removes all cards in pile and sets last played to null
     */
    @Override
    public void clear()
        {
        super.clear() ;
        this.lastPlayed = null ;

        } // end clear()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
        {
        // stores string display name of cards in a list to be separated with commas
        // using String.join
        List<String> results = new ArrayList<>( 108 ) ;

        for ( Card card : this.pile )
            {
            results.add( card.toString() ) ;
            } // end for

        return "Cards in Discard Pile: " + String.join( ", ", results ) ;

        } // end toString()


    /**
     * Used for testing
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        System.out.println( "Creating cards:" ) ;
        Card redOne = new Card( CardColor.RED, CardType.ONE ) ;
        System.out.println( "redOne" ) ;
        Card anotherRedOne = new Card( CardColor.RED, CardType.ONE ) ;
        System.out.println( "anotherRedOne" ) ;
        Card blueReverse = new Card( CardColor.BLUE, CardType.REVERSE ) ;
        System.out.println( "blueReverse" ) ;
        Card yellowReverse = new Card( CardColor.YELLOW, CardType.REVERSE ) ;
        System.out.println( "yellowReverse" ) ;
        Card wildCard = new Card( CardColor.WILD, CardType.WILD ) ;
        System.out.println( "wildCard" ) ;
        System.out.println() ;

        // creating discard pile
        System.out.println( "Creating discard pile:" ) ;
        DiscardPile discardPile = new DiscardPile() ;
        System.out.println() ;

        // testing getLastPlayed()
        System.out.println( "Testing getLastPlayed():" ) ;
        System.out.println( discardPile.getLastPlayed() ) ;
        System.out.println() ;

        // testing addCard()
        System.out.println( "Testing addCard():" ) ;
        System.out.println( "Adding redOne" ) ;
        discardPile.addCard( redOne ) ;
        System.out.println( "Adding wildCard" ) ;
        discardPile.addCard( wildCard ) ;
        System.out.println( String.format( "Last played: %s",
                                           discardPile.getLastPlayed() ) ) ;
        System.out.println() ;

        // testing toString()
        System.out.println( "Testing toString()" ) ;
        System.out.println( discardPile.toString() ) ;

        }

    }
// end class DiscardPile