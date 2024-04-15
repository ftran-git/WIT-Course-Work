
/**
 * Class to create point object
 *
 * @author Fabio Tran
 * @version 1.0.0 2022-02-11 Initial implementation
 */
public class Point
    {

    // instance variables
    public int x ;
    public int y ;

    /*
     * 2-arg constructor
     */
    public Point( int x, int y )
        {
        this.x = x ;
        this.y = y ;

        } // end 2-arg constructor


    @Override
    public String toString()
        {
        return "(" + this.x + ", " + this.y + ")" ;
        } // end toString()


    public Boolean equals( Point point )
        {
        return ( this.x == point.x ) && ( this.y == point.y ) ;
        }

    } // end class Point