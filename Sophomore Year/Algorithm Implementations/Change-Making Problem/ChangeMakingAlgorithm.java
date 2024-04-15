/**
 * Implementing Change-Making Algorithm to solve Change-Making Problem
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-04-13 Initial implementation
 */
public class ChangeMakingAlgorithm
    {

    /**
     * Method for Change-Making
     *
     * @param D
     *     array of coin denominations
     * @param n
     *     total value
     * @return minimum number of coins needed to make total value
     */
    public static int ChangeMaking( int[] D,
                                    int n )
        {
        int[] F = new int[ n + 1 ] ;
        F[ 0 ] = 0 ;

        for ( int i = 1 ; i <= n ; i++ )
            {
            int temp = Integer.MAX_VALUE ;
            int j = 0 ;

            do
                {
                temp = Math.min( F[ i - D[ j ] ], temp ) ;
                j++ ;
                } // end do while
            while ( ( j < D.length ) && ( i >= D[ j ] ) ) ;

            F[ i ] = temp + 1 ;

            } // end for

        return F[ n ] ;

        } // end ChangeMaking()


    /**
     * Testing ChangeMaking()
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // creating array of coin denominations
        int[] test = { 1, 3, 4 } ;

        // testing ChangeMaking()
        System.out.println( ChangeMaking( test, 6 ) ) ;

        } // end main

    } // end class ChangeMakingAlgorithm