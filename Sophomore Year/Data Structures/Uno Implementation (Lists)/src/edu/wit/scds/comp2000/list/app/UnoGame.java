
package edu.wit.scds.comp2000.list.app ;

import edu.wit.scds.comp2000.list.app.card.Card ;
import edu.wit.scds.comp2000.list.app.card.CardColor ;
import edu.wit.scds.comp2000.list.app.card.CardType ;
import edu.wit.scds.comp2000.list.app.pile.Deck ;
import edu.wit.scds.comp2000.list.app.pile.DiscardPile ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;
import java.util.Scanner ;

/**
 * Class that contains game logic for UNO
 *
 * @author Audrey Nichols
 * @version 1.0.0 2021-11-30 Initial Implementation
 */
public class UnoGame
    {

    private static List<Player> players = new ArrayList<>() ;
    private static Deck unoDeck = new Deck() ;
    private static DiscardPile discardPile = new DiscardPile() ;
    private static Player currentPlayer ;
    private static int numPlayers ;
    private static boolean turnOrder = true ;
    private static CardColor lastWildColor ;

    private static boolean gameOver = false ;
    private static Player winner ;

    private static boolean cardActive = true ;
    private static boolean handsFull = true ;

    /**
     * @param args
     */
    public static void main( final String[] args )
        {
        Scanner search = new Scanner( System.in ) ;

        getPlayers( search ) ;
        currentPlayer = players.get( 0 ) ;

        initializeGame() ;

        while ( gameOver == false )
            {
            round( search ) ;
            newRound() ;
            }

        System.out.println( String.format( " % has reached 500 points and won! ",
                                           winner ) ) ;

        // TODO Auto-generated method stub

        }


    /*
     * deal initial hands to players
     */
    public static void initializeGame()
        {
        Card playerCard ;

        // shuffles deck
        unoDeck.shuffle() ;

        for ( int i = 0 ; i < 7 ; i++ )
            {
            for ( Player tempPlayer : players )
                {
                playerCard = unoDeck.deal() ;
                tempPlayer.getHand().addCard( playerCard ) ;
                } // end for
            } // end for

        // discards first card of game
        Card firstDiscard = unoDeck.deal() ;
        discardPile.addCard( firstDiscard ) ;
        } // end initializeGame


    /*
     * Takes player name Creates type player with that name, and adds them to players
     * list
     */
    public static void createPlayer( String name )
        {
        Player newPlayer = new Player( name ) ;
        players.add( newPlayer ) ;
        }


    public static void draw( int numCards )
        {
        Card newCard ;

        for ( int i = 0 ; i < numCards ; i++ )
            {
            newCard = unoDeck.deal() ;
            currentPlayer.getHand().addCard( newCard ) ;
            System.out.println( String.format( "%s, your hand contains the following cards: %s",
                                               currentPlayer.getName(),
                                               currentPlayer.getHand() ) ) ;
            }
        }


    /*
     * Asks user for number of players, if too high or too low, user is re-prompted.
     * Once valid number of players is given, players may input their names.
     */
    public static void getPlayers( Scanner search )
        {

        System.out.println( "Input number of players (2-10): " ) ;

        numPlayers = search.nextInt() ;

        search.nextLine() ;

        for ( int i = 1 ; i <= numPlayers ; i++ )
            {
            System.out.println( String.format( "Player %d, input your name: ",
                                               i ) ) ;

            String newName = search.nextLine() ;
            createPlayer( newName ) ;
            } // end for

        }


    /*
     * Takes
     * @return current deck
     */
    public Deck getDeck()
        {
        return UnoGame.unoDeck ;
        }


    /**
     * Return cards from players hand and discard pile back to deck
     */
    public static void returnCardsToDeck()
        {
        // returns cards from each players hand to deck
        for ( Player player : players )
            {
            while ( !player.getHand().isEmpty() )
                {
                unoDeck.addCard( player.getHand().removeCard() ) ;
                }

            }
        // returns discard pile back to deck
        while ( !discardPile.isEmpty() )
            {
            unoDeck.addCard( discardPile.removeCard() ) ;
            }

        }


    /*
     * Player turns occur until one player has no cards in hand
     */
    public static void round( Scanner search )
        {
        Card discard = discardPile.getLastPlayed() ;

        while ( handsFull )
            {
            turn( currentPlayer, search ) ;
            nextPlayer() ;
            checkHands() ;
            discard = discardPile.getLastPlayed() ;
            }

        }


    /*
     * Checks a players hands to see if there are any compatible cards, if not they
     * draw a card
     */
    public static boolean checkCards( Card card,
                                      Card lastPlayed )
        {

        boolean usefulCard = false ;

        if ( ( lastPlayed.getType() == card.getType() ) ||
             ( lastPlayed.getColor() == card.getColor() ) )
            {
            usefulCard = true ;
            } // end if
        if ( ( card.getType() == CardType.WILD ) ||
             ( card.getColor() == CardColor.WILD ) )
            {
            usefulCard = true ;
            } // end if

        if ( !usefulCard )
            {
            System.out.println( "If you have no useful cards, you will have to draw one." ) ;
            } // end if

        return usefulCard ;
        }


    public static void checkHands()
        {

        for ( Player player : players )
            {
            if ( player.getHand().isEmpty() )
                {
                System.out.println( String.format( "Congratulations, %s has won this round. ",
                                                   player.getName() ) ) ;
                int points = calculatePoints() ;
                player.updateScore( points ) ;
                break ;
                }

            }

        }


    public static void nextPlayer()
        {
        Player tempPlayer = currentPlayer ;
        int currentTurn = players.indexOf( tempPlayer ) ;

        currentPlayer = players.get( ( currentTurn + 1 ) % 2 ) ;
        }


    public static void turn( Player player,
                             Scanner search )
        {

        Card lastCard = discardPile.getLastPlayed() ;

        System.out.println( String.format( "%s, your hand contains the following cards: %s",
                                           player.getName(),
                                           player.getHand() ) ) ;
        System.out.println( String.format( "The last card played was %s.",
                                           lastCard.toString() ) ) ;

        if ( lastCard.getType() == CardType.WILD_DRAW_FOUR )
            {
            if ( cardActive )
                {
                System.out.println( "You draw four cards, and your turn is over." ) ;
                draw( 4 ) ;
                cardActive = false ;
                } // end if
            else
                {
                play( lastCard, search ) ;
                } // end else
                  // next turn

            } // end if
        else if ( lastCard.getType() == CardType.DRAW_TWO )
            {
            if ( cardActive )
                {
                System.out.println( "You draw two cards, and your turn is over." ) ;
                draw( 2 ) ;
                cardActive = false ;
                } // end if
            else
                {
                play( lastCard, search ) ;
                } // end else

            // next turn
            } // end else if
        else if ( lastCard.getType() == CardType.SKIP )
            {
            if ( cardActive )
                {
                System.out.println( "Your turn has been skipped." ) ;
                cardActive = false ;
                } // end if

            // next turn
            } // end else if
        else if ( lastCard.getType() == CardType.WILD )
            {
            System.out.println( String.format( "This wild card has been given the color %s.",
                                               lastWildColor ) ) ;

            play( lastCard, search ) ;

            // next turn
            } // end else if
        else if ( lastCard.getType() == CardType.REVERSE )
            {
            if ( cardActive )
                {
                Collections.reverse( players ) ;
                nextPlayer() ;
                System.out.println( String.format( "The current player is now %s.",
                                                   currentPlayer.getName() ) ) ;
                cardActive = false ;
                } // end if
            else
                {
                play( lastCard, search ) ;
                }

            // next turn
            } // end else if
        else
            {
            play( lastCard, search ) ;
            // next turn
            } // end else

        }


    /*
     * Check points of all players to see if one has reached 500 If so, the game is
     * over and this player has won
     */
    public static void checkPoints()
        {
        for ( Player name : players )
            {
            if ( name.getScore() >= 500 )
                {
                gameOver = true ;
                winner = name ;
                break ;
                } // end if
            else
                {
                System.out.println( String.format( "%s has %d points.",
                                                   name.getName(),
                                                   name.getScore() ) ) ;
                } // end else
            } // end for
        } // end checkPoints


    public static int calculatePoints()
        {
        // TODO implement
        int total = 0 ;
        for ( Player player : players )
            {

            if ( player == winner )
                {
                continue ;

                }
            else
                {
                total = total + player.getHand().getPointTotal() ;

                }

            }

        return total ;

        }


    public static void play( Card discard,
                             Scanner search )
        {
        int color ;
        String entry ;
        Card testCard = null ;
        boolean validChoice = false ;

        while ( !validChoice )
            {
            System.out.println( "You must play a card that matches the color or type of the last card played." ) ;
            System.out.println( "What card do you want to play from your hand? Please give the index of the card." ) ;
            System.out.println( "Or if you would like to draw, type D." ) ;

            entry = search.next() ;

            if ( entry.equals( "D" ) )
                {
                draw( 1 ) ;
                // TODO tell the player what card they drew
                continue ;
                }
            else
                {
                int index = Integer.parseInt( entry ) ;
                testCard = currentPlayer.getHand().getCard( index - 1 ) ;
                validChoice = checkCards( testCard, discard ) ;
                }
            if ( !validChoice )
                {
                System.out.println( "Please try again with a new index." ) ;
                }

            }

        currentPlayer.playCard( testCard ) ;
        discardPile.addCard( testCard ) ;

        cardActive = true ;

        System.out.println( String.format( "You have chosen to play %s.",
                                           testCard ) ) ;

        if ( ( testCard.getType() == CardType.WILD ) ||
             ( testCard.getType() == CardType.WILD_DRAW_FOUR ) )
            {
            System.out.println( "What color would you like to designate this wild card?" ) ;
            System.out.println( "Red, Yellow, Green, or Blue? (Type 1-4)." ) ;

            color = search.nextInt() ;

            if ( ( color < 1 ) || ( color > 4 ) )
                {
                System.out.println( "Please try again." ) ;
                color = search.nextInt() ;
                }
            else if ( ( color == 1 ) )
                {
                lastWildColor = CardColor.RED ;
                }
            else if ( ( color == 2 ) )
                {
                lastWildColor = CardColor.YELLOW ;
                }
            else if ( ( color == 3 ) )
                {
                lastWildColor = CardColor.GREEN ;
                }
            else
                {
                lastWildColor = CardColor.BLUE ;
                }

            }

        } // end method play


    /*
     * Initialize next round of the game First check points to make sure no player
     * has won yet Next perform same steps as initializeGame
     */
    public static void newRound()
        {
        checkPoints() ;
        returnCardsToDeck() ; // return player cards to deck
        initializeGame() ;

        } // end method newRound

    }
// end class UnoGame