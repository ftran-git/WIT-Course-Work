/**
 * Implementing Quick Select with Lomuto Partition
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-04-05 Initial implementation
 */
public class QuickSelect
    {

    /**
     * Method to find the kth smallest element of an array using Quick Select and
     * Lomuto Partition
     *
     * @param array
     *     array to partition
     * @param l
     *     left position
     * @param r
     *     right position
     * @param k
     *     kth smallest element
     * @return value of the kth smallest element
     */
    public static int quickSelect( int array[],
                                   int l,
                                   int r,
                                   int k )
        {
        // using lomutoPartition() to find s
        int s = lomutoPartition( array, l, r ) ;

        // determining kth smallest element
        if ( ( r - l ) == 0 )
            {
            return array[ l ] ;
            } // end if
        else if ( s == ( k - 1 ) )
            {
            return array[ s ] ;
            } // end else if
        else if ( s > ( k - 1 ) )
            {
            return quickSelect( array, l, s - 1, k ) ;
            } // end else if
        else
            {
            return quickSelect( array, s + 1, r, k ) ;
            } // end else

        } // end QuickSelect()


    /**
     * Partitions sub-array by Lomuto's algorithm using first element as pivot
     *
     * @param array
     *     array to partition
     * @param l
     *     left position
     * @param r
     *     right position
     * @return index
     */
    public static int lomutoPartition( int array[],
                                       int l,
                                       int r )
        {
        // initializing pivot and s
        int pivot = array[ l ] ;
        int s = l ;

        // for loop to partition
        for ( int i = l + 1 ; i <= r ; i++ )
            {
            if ( array[ i ] < pivot )
                {
                s++ ;

                // performing a swap
                int temp = array[ s ] ;
                array[ s ] = array[ i ] ;
                array[ i ] = temp ;

                } // end if

            } // end for

        // performing a swap
        int temp = array[ l ] ;
        array[ l ] = array[ s ] ;
        array[ s ] = temp ;

        // returning result
        return s ;

        } // end LomutoPartition()


    /**
     * Testing quickSelect() and lomutoPartition()
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // filling an unsorted array
        int exampleArray[] = { 20, 8, 2, 3, 70 } ;

        // declaring variable for the kth smallest value
        int k ;

        // testing quickSelect()
        System.out.println( "Expected value: 2" ) ;
        k = 1 ;
        System.out.println( "Outcome: " + quickSelect( exampleArray,
                                                       0,
                                                       exampleArray.length - 1,
                                                       k ) ) ;
        System.out.println( "Expected value: 3" ) ;
        k = 2 ;
        System.out.println( "Outcome: " + quickSelect( exampleArray,
                                                       0,
                                                       exampleArray.length - 1,
                                                       k ) ) ;
        System.out.println( "Expected value: 8" ) ;
        k = 3 ;
        System.out.println( "Outcome: " + quickSelect( exampleArray,
                                                       0,
                                                       exampleArray.length - 1,
                                                       k ) ) ;
        System.out.println( "Expected value: 20" ) ;
        k = 4 ;
        System.out.println( "Outcome: " + quickSelect( exampleArray,
                                                       0,
                                                       exampleArray.length - 1,
                                                       k ) ) ;
        System.out.println( "Expected value: 70" ) ;
        k = 5 ;
        System.out.println( "Outcome: " + quickSelect( exampleArray,
                                                       0,
                                                       exampleArray.length - 1,
                                                       k ) ) ;

        } // end main()

    } // end class QuickSelect