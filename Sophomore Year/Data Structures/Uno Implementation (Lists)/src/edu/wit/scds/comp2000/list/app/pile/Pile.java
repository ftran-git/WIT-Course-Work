
package edu.wit.scds.comp2000.list.app.pile ;

import edu.wit.scds.comp2000.list.app.card.Card ;
import edu.wit.scds.comp2000.list.app.card.CardColor ;
import edu.wit.scds.comp2000.list.app.card.CardType ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

/**
 * Class to represent a pile of cards
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-11-18 Initial implementation
 */
public class Pile
    {

    // array-list used to store pile of cards
    final protected List<Card> pile ;

    /**
     * no-arg constructor
     */
    protected Pile()
        {
        // capacity is chosen because a deck of UNO has at most 108 cards
        this.pile = new ArrayList<>( 108 ) ;

        } // end no-arg constructor


    /**
     * Adds a card to the pile
     *
     * @param card
     *     card to add
     */
    public void addCard( Card card )
        {
        this.pile.add( card ) ;

        } // end addCard()


    /**
     * Removes a card from the pile
     *
     * @param card
     *     card to remove
     */
    public void removeCard( Card card )
        {
        this.pile.remove( card ) ;

        } // end removeCard()


    /**
     * Unspecified removal of card from the pile
     *
     * @return card removed
     */
    public Card removeCard()
        {
        return this.pile.remove( 0 ) ;

        } // end removeCard()


    /**
     * Shuffles pile of cards
     */
    public void shuffle()
        {
        Collections.shuffle( this.pile ) ;

        } // end shuffle()


    /**
     * Removes all cards in pile
     */
    public void clear()
        {
        this.pile.clear() ;

        } // end clear()


    /**
     * Checks if pile is empty
     *
     * @return true if empty, false if not
     */
    public boolean isEmpty()
        {
        return this.pile.size() == 0 ;

        } // end isEmpty()


    /**
     * Searches for a card in the pile (for debugging)
     *
     * @return index of card in the pile
     */
    protected int search( Card card )
        {
        return this.pile.indexOf( card ) ;

        } // end search()


    /**
     * Sorts pile of cards (for debugging)
     */
    protected void sort()
        {
        Collections.sort( this.pile ) ;

        } // end sort()


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

        return String.join( ", ", results ) ;

        } // end toString()


    /**
     * Used for testing
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // creating cards
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

        // creating pile
        System.out.println( "Creaing pile:" ) ;
        Pile pile = new Pile() ;
        System.out.println() ;

        // testing add()
        System.out.println( "Testing add():" ) ;
        System.out.println( "Adding redOne" ) ;
        pile.addCard( redOne ) ;
        System.out.println( "Adding anotherRedOne" ) ;
        pile.addCard( anotherRedOne ) ;
        System.out.println( "Adding wildCard" ) ;
        pile.addCard( wildCard ) ;
        System.out.println( String.format( "Cards in pile: %s", pile.toString() ) ) ;
        System.out.println() ;

        // testing remove()
        System.out.println( "Testing remove():" ) ;
        System.out.println( "Removing redOne" ) ;
        pile.removeCard( redOne ) ;
        System.out.println( "Removing anotherRedOne" ) ;
        pile.removeCard( anotherRedOne ) ;
        System.out.println( "Removing wildCard" ) ;
        pile.removeCard( wildCard ) ;
        System.out.println( String.format( "Cards in pile: %s", pile.toString() ) ) ;
        System.out.println() ;

        // testing search()
        System.out.println( "Testing search():" ) ;
        pile.addCard( redOne ) ;
        pile.addCard( wildCard ) ;
        System.out.println( String.format( "Searching for redOne: %s",
                                           pile.search( redOne ) ) ) ;
        System.out.println( String.format( "Searching for wildCard: %s",
                                           pile.search( wildCard ) ) ) ;
        System.out.println() ;

        // testing shuffle()
        System.out.println( "Testing shuffle():" ) ;
        pile.addCard( yellowReverse ) ;
        pile.addCard( blueReverse ) ;
        System.out.println( String.format( "Cards in pile before shuffle: %s",
                                           pile.toString() ) ) ;
        pile.shuffle() ;
        System.out.println( String.format( "Cards in pile after shuffle: %s",
                                           pile.toString() ) ) ;

        // testing sort()
        System.out.println( "Testing sort()" ) ;
        System.out.println( String.format( "Cards in pile before sort: %s: ",
                                           pile.toString() ) ) ;
        pile.sort() ;
        System.out.println( String.format( "Cards in pile after sort: %s",
                                           pile.toString() ) ) ;

        } // end main()

    }
// end class Pile