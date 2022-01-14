
package edu.wit.scds.comp2000.list.app.card ;

/**
 * Enumeration for card colors in UNO. Used in this program as a parameter to create
 * a card object
 *
 * @author Fabio Tran
 * @version 1.0.0 2021-11-15 Initial implementation
 */
public enum CardColor
    {

    // Color, Display Name
    /** color red */
    RED ( "Red" ),
    /** color yellow */
    YELLOW ( "Yellow" ),
    /** color green */
    GREEN ( "Green" ),
    /** color blue */
    BLUE ( "Blue" ),
    /** wild which means any color from red, yellow, green, or blue */
    WILD ( "Wild" );

    /** instance variable to hold formatted display name */
    public final String colorDisplayName ;

    /**
     * @param colorDisplayName
     *     formatted name for display
     */
    private CardColor( final String colorDisplayName )
        {
        this.colorDisplayName = colorDisplayName ;

        } // end 1-arg constructor


    @Override
    public String toString()
        {
        return this.colorDisplayName ;

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
        System.out.println( String.format( "RED: %s", CardColor.RED ) ) ;
        System.out.println( String.format( "YELLOW: %s", CardColor.YELLOW ) ) ;
        System.out.println( String.format( "GREEN: %s", CardColor.RED ) ) ;
        System.out.println( String.format( "BLUE: %s", CardColor.BLUE ) ) ;
        System.out.println( String.format( "WILD: %s", CardColor.WILD ) ) ;

        } // end main()

    }
// end enum CardColor