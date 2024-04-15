
package edu.wit.scds.comp2000.list.app.pile ;

import edu.wit.scds.comp2000.list.app.card.Card ;
import edu.wit.scds.comp2000.list.app.card.CardColor ;
import edu.wit.scds.comp2000.list.app.card.CardType ;

import java.util.ArrayList ;
import java.util.List ;

/**
 * Class to create a Deck of UNO cards
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-11-18 Initial implementation
 */
public class Deck extends Pile
    {

    /**
     * no-arg constructor
     */
    public Deck()
        {
        for ( CardColor color : CardColor.values() )
            {
            if ( color == CardColor.WILD )
                {
                for ( CardType type : CardType.values() )
                    {
                    if ( ( type == CardType.WILD ) ||
                         ( type == CardType.WILD_DRAW_FOUR ) )
                        {
                        for ( int i = 0 ; i < 4 ; i++ )
                            {
                            Card card = new Card( color, type ) ;
                            super.pile.add( card ) ;
                            } // end for

                        } // end if

                    } // end for

                continue ;

                } // end if

            for ( CardType type : CardType.values() )
                {
                if ( type == CardType.ZERO )
                    {
                    Card card = new Card( color, type ) ;
                    super.pile.add( card ) ;
                    continue ;
                    } // end if

                if ( ( type == CardType.WILD ) ||
                     ( type == CardType.WILD_DRAW_FOUR ) )
                    {
                    continue ;
                    } // end if

                for ( int i = 0 ; i < 2 ; i++ )
                    {
                    Card card = new Card( color, type ) ;
                    super.pile.add( card ) ;
                    } // end for

                } // end for

            } // end for

        } // end deck()


    /**
     * Deals the player one card from the deck using unspecified remove from
     * superclass. Renamed just for clarity purposes
     *
     * @return card to be dealt
     */
    public Card deal()
        {
        return super.removeCard() ;

        } // end deal()


    /**
     * Re-shuffles the deck once it runs out of cards with every card from discard
     * pile except top card
     *
     * @param discardPile
     *     discard pile to be re-shuffled into deck
     */
    public void reshuffle( DiscardPile discardPile )
        {
        // stores last played and removes it so it doesn't get shuffle back into deck
        Card lastPlayed = discardPile.getLastPlayed() ;
        discardPile.removeCard( lastPlayed ) ;

        // adds each card from discard pile back into the deck
        while ( !discardPile.isEmpty() )
            {
            super.pile.add( discardPile.removeCard() ) ;
            } // end while

        // returns last played to discard pile
        discardPile.addCard( lastPlayed ) ;

        // shuffle deck
        super.shuffle() ;

        } // end reshuffle()


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

        return "Cards in Deck: " + String.join( ", ", results ) ;

        } // end toString()


    /**
     * Used for testing
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // creating deck
        System.out.println( "Creating Deck of Uno:" ) ;
        Deck unoDeck = new Deck() ;
        System.out.println( unoDeck.toString() ) ;
        System.out.println( String.format( "Number of cards: %s",
                                           unoDeck.pile.size() ) ) ;
        System.out.println() ;

        // testing deal()
        System.out.println( "Testing deal():" ) ;
        System.out.println( "Dealing card" ) ;
        unoDeck.deal() ;
        System.out.println( "Dealing card" ) ;
        unoDeck.deal() ;
        System.out.println( unoDeck.toString() ) ;
        System.out.println( String.format( "Number of cards: %s",
                                           unoDeck.pile.size() ) ) ;
        System.out.println() ;

        // testing reshuffle()
        System.out.println( "Testing reshuffle():" ) ;
        System.out.println( "Creating Discard Pile" ) ;
        DiscardPile discardPile = new DiscardPile() ;

        System.out.println( "Emptying deck" ) ;
        while ( !unoDeck.pile.isEmpty() )
            {
            discardPile.addCard( unoDeck.deal() ) ;
            } // end while
        System.out.println( unoDeck.toString() ) ;
        System.out.println( String.format( "Number of cards in deck: %s",
                                           unoDeck.pile.size() ) ) ;
        System.out.println( discardPile.toString() ) ;
        System.out.println( String.format( "Number of cards in discard pile: %s",
                                           discardPile.pile.size() ) ) ;
        System.out.println( String.format( "Last played card: %s",
                                           discardPile.getLastPlayed() ) ) ;
        System.out.println( "Reshuffling" ) ;
        unoDeck.reshuffle( discardPile ) ;
        System.out.println( unoDeck.toString() ) ;
        System.out.println( String.format( "Number of cards in deck: %s",
                                           unoDeck.pile.size() ) ) ;
        System.out.println( discardPile.toString() ) ;
        System.out.println( String.format( "Number of cards in discard pile: %s",
                                           discardPile.pile.size() ) ) ;
        System.out.println( String.format( "Last played card: %s",
                                           discardPile.getLastPlayed() ) ) ;

        } // end main()

    }
// end class Deck