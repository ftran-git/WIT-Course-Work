import java.util.ArrayList ;

/**
 * Brute Force Algorithm for Convex-Hull
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-02-22 Initial implementation
 */
public class ConvexHull
    {

    /**
     * Convex-Hull by Brute Force
     */
    public static ArrayList convexHull( Point[] points )
        {
        // create empty set of line segments L
        ArrayList<Segment> L = new ArrayList<>() ;

        for ( int i = 0 ; i < points.length ; i++ )
            {
            for ( int j = i + 1 ; j < points.length ; j++ )
                {
                int a = points[ j ].y - points[ i ].y ;
                int b = points[ i ].x - points[ j ].x ;
                int c = ( points[ i ].x * points[ j ].y ) -
                        ( points[ i ].y * points[ j ].x ) ;

                boolean foundProblem = false ;

                for ( int k = 0 ; k < points.length ; k++ )
                    {
                    if ( ( k == i ) || ( k == j ) )
                        {
                        continue ;
                        } // end if
                    int check = ( ( a * points[ k ].x ) + ( b * points[ k ].y ) ) -
                                c ;
                    if ( check < 0 )
                        {
                        foundProblem = true ;
                        break ;
                        } // end if

                    } // end for loop

                if ( !foundProblem )
                    {
                    Segment segment = new Segment( points[ i ], points[ j ] ) ;
                    L.add( segment ) ;

                    } // end if

                } // end for loop

            } // end for loop

        ArrayList<Point> newPoints = new ArrayList<>() ;

        for ( Segment segment : L )
            {
            if ( !newPoints.contains( segment.firstPoint ) )
                {
                newPoints.add( segment.firstPoint ) ;
                }
            if ( !newPoints.contains( segment.secondPoint ) )
                {
                newPoints.add( segment.secondPoint ) ;
                }
            } // end for

        return newPoints ;

        } // end convexHull()


    /**
     * Testing
     *
     * @param args
     */
    public static void main( String[] args )
        {
        Point point1 = new Point( 0, 3 ) ;
        Point point2 = new Point( 2, 2 ) ;
        Point point3 = new Point( 1, 1 ) ;
        Point point4 = new Point( 2, 1 ) ;
        Point point5 = new Point( 3, 0 ) ;
        Point point6 = new Point( 0, 0 ) ;
        Point point7 = new Point( 3, 3 ) ;

        Point[] points = { point1, point2, point3, point4, point5, point6, point7 } ;

        ArrayList<Point> newPoints = convexHull( points ) ;

        for ( Point point : newPoints )
            {
            System.out.println( point ) ;
            }

        } // end main()

    }
// end class ConvexHull