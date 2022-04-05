
/**
 * @author tranf
 * @version 1.0.0 2022-02-04 Initial implementation
 */
public class SequentialSearchAlgorithm
    {

    /**
     * @param A
     * @param K
     * @return
     */
    public static int SequentialSearch( int[] A,
                                        int K )
        {
        // A[ A.length ] = K ;
        int i = 0 ;
        
        while ( A[ i ] != K )
            {
            i = i + 1 ;
            }

        if ( i < A.length )
            {
            return i ;
            }

        return -1 ;

        }


    /**
     * @param args
     */
    public static void main( String[] args )
        {
        int[] array = { 1, 2, 3, 4 } ;
        System.out.println( SequentialSearch( array, 4 ) ) ;

        }

    }
// end class SequentialSearchAlgorithm