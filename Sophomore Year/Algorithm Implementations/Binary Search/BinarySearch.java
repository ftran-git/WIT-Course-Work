/**
 * Implementation of Binary Search
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-04-05 Initial implementation
 */
public class BinarySearch
    {

    /**
     * Method that implements binary search
     *
     * @param array
     *     array to search through
     * @param key
     *     value to search for
     */
    public static void binarySearch( int array[],
                                     int key )
        {
        // initializing most left and right indexes
        int l = 0 ;
        int r = array.length - 1 ;

        while ( l <= r )
            {
            int m = ( l + r ) / 2 ;

            // checking if the key is found at m
            if ( array[ m ] == key )
                {
                System.out.println( "Found at " + m ) ;
                return ;
                } // end if

            // checking what half of the array to ignore
            if ( array[ m ] < key )
                {
                l = m + 1 ;
                } // end if
            else
                {
                r = m - 1 ;
                } // end else

            } // end while

        // key not found
        System.out.println( "Key not found" ) ;

        } // end binarySerach()


    /**
     * Testing binarySearch()
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // filling a sorted array
        int exampleArray[] = { 1, 8, 12, 15, 70 } ;

        // testing different key values
        int key1 = 12 ;
        binarySearch( exampleArray, key1 ) ;
        int key2 = 15 ;
        binarySearch( exampleArray, key2 ) ;
        int key3 = 8 ;
        binarySearch( exampleArray, key3 ) ;
        int key4 = -1 ;
        binarySearch( exampleArray, key4 ) ;
        int key5 = 90 ;
        binarySearch( exampleArray, key5 ) ;

        } // end main()

    } // end class BinarySearch