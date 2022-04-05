
package edu.wit.scds.comp2000.list.app.card ;

import java.util.Objects ;

/**
 * Class to create card objects for UNO Game.
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-11-18 Initial implementation
 */
public class Card implements Comparable<Card>
    {

    /** holds card color */
    private final CardColor color ;
    /** holds card type */
    private final CardType type ;

    /**
     * 2-arg constructor that takes in color and type of uno card
     *
     * @param color
     *     color of card
     * @param type
     *     type of card
     */
    public Card( CardColor color, CardType type )
        {
        this.color = color ;
        this.type = type ;

        } // end 2-arg constructor


    /**
     * Retrieves color of card
     *
     * @return color of card
     */
    public CardColor getColor()
        {
        return this.color ;

        } // end getColor()


    /**
     * Retrieves type of card
     *
     * @return type of card
     */
    public CardType getType()
        {
        return this.type ;

        } // end getType()


    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( Card otherCard )
        {
        final int cardOrder = this.color.compareTo( otherCard.color ) ;

        if ( cardOrder == 0 )
            {
            return this.type.compareTo( otherCard.type ) ;
            } // end if

        return cardOrder ;

        } // end compareTo()


    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj )
        {
        // testing if same object
        if ( this == obj )
            {
            return true ;
            } // end if

        // testing if same reference type
        if ( obj instanceof Card )
            {
            // cast to compare
            final Card otherCard = (Card) obj ;

            return ( this.color == otherCard.color ) &&
                   ( this.type == otherCard.type ) ;
            } // end if

        // otherwise false
        return false ;

        } // end equals()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
        {
        return Objects.hash( this.color, this.type ) ;

        } // end hashCode()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
        {
        if ( ( this.type == CardType.WILD ) ||
             ( this.type == CardType.WILD_DRAW_FOUR ) )
            {
            return String.format( "%s", this.type ) ;
            } // end if

        return String.format( "%s %s", this.color, this.type ) ;

        } // end toString()


    /**
     * Used for testing
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // creating instances of Card
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

        // testing getColor()
        System.out.println( "Testing getColor()" ) ;
        System.out.println( String.format( "redOne: %s", redOne.getColor() ) ) ;
        System.out.println( String.format( "blueReverse: %s",
                                           blueReverse.getColor() ) ) ;
        System.out.println( String.format( "wildCard: %s", wildCard.getColor() ) ) ;
        System.out.println() ;

        // testing getType()
        System.out.println( "Testing getType()" ) ;
        System.out.println( String.format( "redOne: %s", redOne.getType() ) ) ;
        System.out.println( String.format( "blueReverse: %s",
                                           blueReverse.getType() ) ) ;
        System.out.println( String.format( "wildCard: %s", wildCard.getType() ) ) ;
        System.out.println() ;

        // testing compareTo()
        System.out.println( "Testing compareTo()" ) ;
        System.out.println( String.format( "redOne compared to anotherRedOne: %s",
                                           redOne.compareTo( anotherRedOne ) ) ) ;
        System.out.println( String.format( "redOne compared to blueReverse: %s",
                                           redOne.compareTo( blueReverse ) ) ) ;
        System.out.println( String.format( "blueReverse compared to yellowReverse: %s",
                                           blueReverse.compareTo( yellowReverse ) ) ) ;
        System.out.println() ;

        // testing equals()
        System.out.println( "Testing equals()" ) ;
        System.out.println( String.format( "redOne equals anotherRedOne: %s",
                                           redOne.equals( anotherRedOne ) ) ) ;
        System.out.println( String.format( "redOne equals blueReverse: %s",
                                           redOne.equals( blueReverse ) ) ) ;
        System.out.println( String.format( "blueReverse equals yellowReverse: %s",
                                           blueReverse.equals( yellowReverse ) ) ) ;
        System.out.println() ;

        // testing hashCode()
        System.out.println( "Testing hashCode()" ) ;
        System.out.println( String.format( "redOne: %s", redOne.hashCode() ) ) ;
        System.out.println( String.format( "anotherRedOne: %s",
                                           anotherRedOne.hashCode() ) ) ;
        System.out.println( String.format( "blueReverse: %s",
                                           blueReverse.hashCode() ) ) ;
        System.out.println( String.format( "yellowReverse: %s",
                                           yellowReverse.hashCode() ) ) ;
        System.out.println( String.format( "wildCard: %s", wildCard.hashCode() ) ) ;
        System.out.println() ;

        // testing toString()
        System.out.println( "Testing toString ()" ) ;
        System.out.println( String.format( "redOne: %s", redOne ) ) ;
        System.out.println( String.format( "anotherRedOne: %s", anotherRedOne ) ) ;
        System.out.println( String.format( "blueReverse: %s", blueReverse ) ) ;
        System.out.println( String.format( "yellowReverse: %s", yellowReverse ) ) ;
        System.out.println( String.format( "wildCard: %s", wildCard ) ) ;
        System.out.println() ;

        } // end main()
    }
// end class Card