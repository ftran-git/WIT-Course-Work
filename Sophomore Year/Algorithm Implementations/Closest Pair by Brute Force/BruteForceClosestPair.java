/**
 * Brute Force Closest Pair Algorithm
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-02-11 Initial implementation
 */
public class BruteForceClosestPair
    {

    /**
     * @return closest distance
     */
    public static double BruteForceClosestPair( Point[] p )
        {
        Integer d = Integer.MAX_VALUE ;

        for ( int i = 0 ; i < ( p.length - 1 ) ; i++ )
            {
            for ( int j = i + 1 ; j < ( p.length ) ; j++ )
                {
                d = Math.min( d,
                              ( ( p[ i ].x - p[ j ].x ) * ( p[ i ].x - p[ j ].x ) ) +
                                 ( ( p[ i ].y - p[ j ].y ) *
                                   ( p[ i ].y - p[ j ].y ) ) ) ;
                } // end for
            
            } // end for
        
        return Math.sqrt( d ) ;
        
        } // end BruteForceClosestPair()


    /**
     * @param args
     */
    public static void main( String[] args )
        {
        Point point1 = new Point( 1, 2 ) ;
        Point point2 = new Point( 3, 7 ) ;
        Point point3 = new Point( 10, 9 ) ;

        Point[] points = { point1, point2, point3 } ;
        System.out.println( BruteForceClosestPair( points ) ) ;

        } // end main()

    } // end class BruteForceClosestPair