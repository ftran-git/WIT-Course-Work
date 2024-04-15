/**
 * @author Fabio Tran
 * @version 1.0.0 2022-01-28 Initial implementation
 */
public class SelectionSortAlgorithm
    {

    public static void SelectionSort( int[] a )
        {
        for ( int i = 0 ; i < ( a.length - 1 ) ; i++ )
            {
            int min = i ;
            for ( int j = i + 1 ; j < a.length ; j++ )
                {
                if ( a[ j ] < a[ min ] )
                    {
                    min = j ;
                    } // end if
                } // end for
            int temp = a[ min ] ;
            a[ min ] = a[ i ] ;
            a[ i ] = temp ;
            } // end for
        } // end SelectionSort()

    /**
     * Testing SelectionSort
     * 
     * @param args
     */
    public static void main( String[] args )
        {
        // creating array
        int[] array = { 2, 7, 4, 1, 5, 3 } ;

        // displaying original array
        for ( int element : array )
            {
            System.out.print( element ) ;
            } // end for
        
        System.out.println() ;
        
        // sorting array
        SelectionSort( array ) ;

        // displaying sorted array
        for ( int element : array )
            {
            System.out.print( element ) ;
            } // end for

        }

    }
// end class SelectionSortAlgorithm