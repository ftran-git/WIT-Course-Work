
package edu.wit.scds.comp2000.list.app.pile ;

import edu.wit.scds.comp2000.list.app.card.Card ;
import edu.wit.scds.comp2000.list.app.card.CardColor ;
import edu.wit.scds.comp2000.list.app.card.CardType ;

/**
 * @author Tim Magee
 * @version 1.0.0 2021-11-30 Initial implementation
 */
public class Hand extends Pile
    {

    /**
     * no-arg constructor a players hand at the start of the game will always be
     * zero, since cards have not been dealt
     */
    public Hand()
        {
        super() ;
        }


    /**
     * Returns card at a given index in pile
     *
     * @param index
     *     index of card in array-list
     * @return card
     */
    public Card getCard( int index )
        {
        return super.pile.get( index ) ;

        } // end getCard()


    /**
     * Calculates the total points of the cards in this hand
     *
     * @return
     */
    public int getPointTotal()
        {
        int total = 0 ;
        for ( Card card : super.pile )
            {
            if ( card.getType() == CardType.ZERO )
                {
                continue ;
                }
            if ( card.getType() == CardType.ONE )
                {
                total = +1 ;
                }
            if ( card.getType() == CardType.TWO )
                {
                total = +2 ;
                }
            if ( card.getType() == CardType.THREE )
                {
                total = +3 ;
                }
            if ( card.getType() == CardType.FOUR )
                {
                total = +4 ;
                }
            if ( card.getType() == CardType.FIVE )
                {
                total = +5 ;
                }
            if ( card.getType() == CardType.SIX )
                {
                total = +6 ;
                }
            if ( card.getType() == CardType.SEVEN )
                {
                total = +7 ;
                }
            if ( card.getType() == CardType.EIGHT )
                {
                total = +8 ;
                }
            if ( card.getType() == CardType.NINE )
                {
                total = +9 ;
                }
            if ( ( card.getType() == CardType.DRAW_TWO ) ||
                 ( card.getType() == CardType.REVERSE ) ||
                 ( card.getType() == CardType.SKIP ) )
                {
                total = +20 ;
                }
            if ( ( card.getType() == CardType.WILD ) ||
                 ( card.getType() == CardType.WILD_DRAW_FOUR ) )
                {
                total = +50 ;
                }
            }
        return total ;

        }


    /**
     * returns the hand as a string in format "[x, y, z]
     */
    @Override
    public String toString()
        {
        String ret = "[" ;
        for ( int i = 0 ; i < ( this.pile.size() - 1 ) ; i++ )
            {
            ret += this.pile.get( i ) + ", " ;
            }
        return ret + this.pile.get( this.pile.size() - 1 ) + "]" ;

        }


    /**
     * used for testing
     *
     * @param args
     */
    public static void main( String[] args )
        {
        // tests to see if initial hand size is zero
        System.out.println( "Creating hand..." ) ;
        Hand testHand = new Hand() ;
        System.out.println( "Number of cards in hand: " + testHand.pile.size() +
                            "\n" ) ;

        // creates cards that will be added to hand
        System.out.println( "Creating cards..." ) ;
        Card redOne = new Card( CardColor.RED, CardType.ONE ) ;
        Card anotherRedOne = new Card( CardColor.RED, CardType.ONE ) ;
        Card blueReverse = new Card( CardColor.BLUE, CardType.REVERSE ) ;
        Card yellowReverse = new Card( CardColor.YELLOW, CardType.REVERSE ) ;
        Card wildCard = new Card( CardColor.WILD, CardType.WILD ) ;
        System.out.println() ;

        // testing for every time a card is added to testHand
        System.out.println( "Adding cards to testHand..." ) ;
        System.out.print( "---Adding a Red-1 to testHand...\n---" ) ;
        testHand.addCard( redOne ) ;
        System.out.println( testHand.toString() ) ;
        System.out.print( "---Adding a Blue-Reverse to testHand...\n---" ) ;
        testHand.addCard( blueReverse ) ;
        System.out.println( testHand.toString() ) ;
        System.out.print( "---Adding a Yellow-Reverse to testHand...\n---" ) ;
        testHand.addCard( yellowReverse ) ;
        System.out.println( testHand.toString() ) ;
        System.out.print( "---Adding a Red-1 to testHand...\n---" ) ;
        testHand.addCard( anotherRedOne ) ;
        System.out.println( testHand.toString() ) ;
        System.out.print( "---Adding a Wild Card to testHand...\n---" ) ;
        testHand.addCard( wildCard ) ;
        System.out.println( testHand.toString() ) ;
        System.out.println() ;

        System.out.println( "Testing Complete." ) ;

        }

    }
// end class Hand