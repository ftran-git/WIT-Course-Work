
/**
 * Implementing Russian Peasant Multiplication
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-04-05 Initial implementation
 */
public class RussianPeasantMultiplication
    {

    /**
     * Method that implements Russian Peasant Multiplication using bitwise shift
     * operators
     *
     * @param firstInt
     *     first integer
     * @param secondInt
     *     second integer
     * @return multiplication result
     */
    public static int russianPeasantMultiplication( int firstInt,
                                                    int secondInt )
        {
        // initializing result to zero
        int result = 0 ;

        // checking second integer does not become 1
        while ( secondInt > 0 )
            {
            // add first number to result if second integer becomes odd
            if ( ( secondInt & 1 ) != 0 )
                {
                result = result + firstInt ;
                } // end if

            // double first number and half second number
            firstInt = firstInt << 1 ;
            secondInt = secondInt >> 1 ;
            } // end while

        // returns the result of the multiplication
        return result ;

        } // end russianPeasantMultiplication()


    /**
     * Testing russainPeasantMultiplication()
     *
     * @param args
     *     unused
     */
    public static void main( String[] args )
        {
        // assigning integers to multiply
        int firstInt = 2 ;
        int secondInt = 9 ;

        // testing russainPeasantMultiplication()
        System.out.println( russianPeasantMultiplication( firstInt, secondInt ) ) ;

        } // end main()

    } // end class RussianPeasantMultiplication