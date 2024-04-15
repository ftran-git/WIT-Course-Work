
package edu.wit.scds.comp2000.list.app.card ;

/**
 * Enumeration for card types in UNO. Used as a parameter to create a card object
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-11-15 Initial implementation
 */
public enum CardType
    {

    // Element, Display Name
    /** number zero */
    ZERO ( "Zero" ),
    /** number one */
    ONE ( "One" ),
    /** number two */
    TWO ( "Two" ),
    /** number three */
    THREE ( "Three" ),
    /** number four */
    FOUR ( "Four" ),
    /** number five */
    FIVE ( "Five" ),
    /** number six */
    SIX ( "Six" ),
    /** number seven */
    SEVEN ( "Seven" ),
    /** number eight */
    EIGHT ( "Eight" ),
    /** number nine */
    NINE ( "Nine" ),
    /** draw-two card */
    DRAW_TWO ( "Draw-Two" ),
    /** reverse card */
    REVERSE ( "Reverse" ),
    /** skip card */
    SKIP ( "Skip" ),
    /** wild card */
    WILD ( "Wild" ),
    /** draw-four wild card */
    WILD_DRAW_FOUR ( "Wild Draw-Four" );

    /** instance variable to hold formatted display name */
    public final String typeDisplayName ;

    /**
     * @param typeDisplayName
     *     formatted name for display
     */
    private CardType( final String typeDisplayName )
        {
        this.typeDisplayName = typeDisplayName ;

        } // end 1-arg constructor


    @Override
    public String toString()
        {
        return this.typeDisplayName ;

        }   // end method toString()


    /**
     * Used for testing
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // testing toString()
        System.out.println( "Testing toString():" ) ;
        System.out.println( String.format( "ZERO: %s", CardType.ZERO ) ) ;
        System.out.println( String.format( "ONE: %s", CardType.ONE ) ) ;
        System.out.println( String.format( "TWO: %s", CardType.TWO ) ) ;
        System.out.println( String.format( "THREE: %s", CardType.THREE ) ) ;
        System.out.println( String.format( "FOUR: %s", CardType.FOUR ) ) ;
        System.out.println( String.format( "FIVE: %s", CardType.FIVE ) ) ;
        System.out.println( String.format( "SIX: %s", CardType.SIX ) ) ;
        System.out.println( String.format( "SEVEN: %s", CardType.SEVEN ) ) ;
        System.out.println( String.format( "EIGHT: %s", CardType.EIGHT ) ) ;
        System.out.println( String.format( "NINE: %s", CardType.NINE ) ) ;
        System.out.println( String.format( "DRAW_TWO: %s", CardType.DRAW_TWO ) ) ;
        System.out.println( String.format( "REVERSE: %s", CardType.REVERSE ) ) ;
        System.out.println( String.format( "SKIP: %s", CardType.SKIP ) ) ;
        System.out.println( String.format( "WILD: %s", CardType.WILD ) ) ;
        System.out.println( String.format( "WILD_DRAW_FOUR: %s",
                                           CardType.WILD_DRAW_FOUR ) ) ;

        } // end main()
    }
// end enum CardType