import java.util.Scanner;
import java.util.ArrayList;
/**
 * Events that occur in a round of blackjack
 * 
 * @author Fabio Tran
 */
public class BlackjackEvents {
	private Scanner input = new Scanner(System.in);
	private DealerHand dealer;
	private int players;
	private ArrayList<Player> playersArray = new ArrayList<>();
	private Deck deck;
	
	
/*
 * Displays the rules of blackjack
 */
	public void gameInstructions() {
		System.out.println("Blackjack!");
		System.out.println("Rules:");
		System.out.println(" - The goal of blackjack is to beat the dealer's hand without going over 21");
		System.out.println(" - Face cards are worth 10. Aces are worth 1 or 11. Rest of the cards are worth their face value.");
		System.out.println(" - Each player starts with two cards, one of the dealer's cards is hidden until the end.");
		System.out.println(" - To 'Hit' is to ask for another card. To 'Stand' is to hold your total and end your turn.");
		System.out.println(" - If you go over 21 you bust, and the dealer wins regardless of the dealer's hand.");
		System.out.println(" - If you are dealt 21 from the start (Ace & 10), you got a blackjack.");
		System.out.println(" - Blackjack usually means you win 1.5 the amount of your bet.");
		System.out.println(" - Dealer will hit until his/her cards total 17 or higher.");
		System.out.println(" - Doubling is like a hit, only the bet is doubled and you only get one more card.");
		System.out.println(" - Split can be done when you have two of the same card - the pair is split into two hands.");
		System.out.println(" - Splitting also doubles the bet, because each new hand is worth the original bet.");
		System.out.println(" - You can only double/split on the first move, or first move of a hand created by a split.");
		System.out.println(" - You cannot play on two aces after they are split.");
		System.out.println(" - You can double on a hand resulting from a split, tripling or quadrupling you bet.");
	}
/*
 * Initializes a deck using the deck class and shuffles the content
 */		
	public void initializeDeckAndShuffle() {
	
		this.deck = new Deck();
		deck.shuffle();
		
		dealer = new DealerHand();
			
	}
/*
 * Gets amount of players, there names, and creates objects for each player
 */
	public void getPlayersAndNames() {
		
		
			System.out.println("How many players (Up to 6)? ");
			this.players = input.nextInt();
			
			if(players > 6 || players < 1) {
				
				System.out.println("Not a valid amount of players!");
				System.exit(0);
				
			} if(players<=6 && players>=1) {
				
					for(int i = 1; i <= players; i++) {
					
					System.out.println("What is player " + i + " name?");
					String name = input.next();
					playersArray.add(new Player(name));
					}
					
			}
			
	
		
	}
/*
 * Grabs the amount each player wants to bet 
 */
	public void getBets() {
		
		for(int i = 0; i < playersArray.size() ; i++) {
			
		
				System.out.println(playersArray.get(i).name + ", how much do you want to bet? You have " + playersArray.get(i).money + " credits");
				int bet = input.nextInt();
				if(bet>0 && bet<=1000) {
					playersArray.get(i).hand.bet(bet);	
				} else {
					System.out.println("Invalid bet amount!");
					System.exit(1);
				}
				
		
				
		}
		
	}

/*
 * Assigns two cards to each player's hand 
 */
	public void dealCards() {
		
		for(int a = 0; a < 2; a++) {
			dealer.addCard(deck.getCard());
			for(int i = 0; i < playersArray.size(); i++) {
				playersArray.get(i).getHand(deck.getCard());
		}		
			
		}
		
	}
/*
 * Prints the cards in a players hand and Total
 */
	public void displayCards() {
			
		System.out.println("The dealer's face up card is " + dealer.hand.get(0));
	
		for(int i = 0; i < playersArray.size(); i++) {
			System.out.println(playersArray.get(i).showHand());
			System.out.println(playersArray.get(i).hand.getTotal());
			if(playersArray.get(i).hand.finalTotal == 21) {
				System.out.println(playersArray.get(i).name + " has blackjack!");
			}
		
		}
	}
	
/*
 * Checks if a player can split. 
 */
	public void checksForSplit() {
	
		for(int i = 0; i < playersArray.size(); i++) {
			if((playersArray.get(i).firstCard().getValue()>=10) && (playersArray.get(i).secondCard().getValue()>=10)) {
				playersArray.get(i).hand.canSplit = true;
			} else if(playersArray.get(i).firstCard().getValue()==playersArray.get(i).secondCard().getValue()) {
				playersArray.get(i).hand.canSplit = true;
			}	
		}
		split();
			
	}

/*
 * Splits doubles if player decides to and establishes the bet for each hand equal to the original
 */
	public void split() {
		
		String answer = "";
		
		for(int i = 0; i < playersArray.size(); i++) {
			if(playersArray.get(i).hand.canSplit && !playersArray.get(i).hand.blackjack) {
				System.out.println(playersArray.get(i).name + ", do you want to split?");
				answer = input.next();
				
			}
			if(answer.equals("Yes")||answer.equals("yes")) {
				
				playersArray.get(i).splitHand = true;
				
				playersArray.get(i).splitHand1.addCard(playersArray.get(i).firstCard());
				playersArray.get(i).splitHand1.addCard(deck.getCard());
				playersArray.get(i).splitHand1.bet(playersArray.get(i).hand.getBet());
				
				playersArray.get(i).splitHand2.addCard(playersArray.get(i).secondCard());
				playersArray.get(i).splitHand2.addCard(deck.getCard());
				playersArray.get(i).splitHand2.bet(playersArray.get(i).hand.getBet());
	
			}
		}	
	}
	/*
	 * Doubles players bet on a hand
	 */
		public boolean doubleDownValid() {
		
			for(int i = 0; i < playersArray.size(); i++) {
				
				if(playersArray.get(i).splitHand == true) {
					
					if(playersArray.get(i).splitHand1.getBet() + playersArray.get(i).splitHand2.getBet() <= playersArray.get(i).getMoney()/2) {
						return true;
					}
						
				} else {
					if(playersArray.get(i).hand.getBet() <= playersArray.get(i).getMoney()/2) {
						return true;
				}
					
				} 
			} return false;

		}
	public void doubleDown() {
		
		String answer = "";
		
		for(int i = 0; i < playersArray.size(); i++) {
			
			if(doubleDownValid() && !playersArray.get(i).hand.blackjack) {
				
				if(playersArray.get(i).splitHand == true) {
						
					if(!playersArray.get(i).splitHand1.blackjack) {
						
						System.out.println("First hand of " + playersArray.get(i).showSplitHand1());
						System.out.println(playersArray.get(i).splitHand1.getTotal());
						System.out.println(playersArray.get(i).name + ", do you want to double down for hand 1?");
						answer = input.next();
						
						if(answer.equals("Yes") || answer.equals("yes")) {
							playersArray.get(i).splitHand1.doubleDown = true;
							playersArray.get(i).splitHand1.doubleBet();
							System.out.println("Bet raised to: " + playersArray.get(i).splitHand1.getBet());
						}
					}
					
					if(!playersArray.get(i).splitHand1.blackjack) {
						
						System.out.println("Second hand of " + playersArray.get(i).showSplitHand2());
						System.out.println(playersArray.get(i).splitHand2.getTotal());
						System.out.println(playersArray.get(i).name + ", do you want to double down for hand 2?");
						answer = input.next();
						
						
						if(answer.equals("Yes") || answer.equals("yes")) {
							playersArray.get(i).splitHand2.doubleDown = true;
							playersArray.get(i).splitHand2.doubleBet();
							System.out.println("Bet raised to: " + playersArray.get(i).splitHand2.getBet());
						}
					}
						
						
				} else {

						System.out.println(playersArray.get(i).name + ", do you want to double down?");
						answer = input.next();
					
						if(answer.equals("Yes") || answer.equals("yes")) {
							playersArray.get(i).hand.doubleDown = true;
							playersArray.get(i).hand.doubleBet();
							System.out.println("Bet raised to: " + playersArray.get(i).hand.getBet());
							
						}
					}
				} 
			}
	}
	
	public void hitOrStand() {
		String answer;
		
		for(int i = 0; i < playersArray.size(); i++) {
			
			if(!playersArray.get(i).hand.blackjack) {
				
				if(playersArray.get(i).splitHand) {
					
					if(playersArray.get(i).splitHand1.doubleDown) {
						
							System.out.println(playersArray.get(i).name + " gets one more card.");
							playersArray.get(i).splitHand1.addCard(deck.getCard());
							System.out.println(playersArray.get(i).showSplitHand1());
							System.out.println(playersArray.get(i).splitHand1.getTotal());
						
						if(playersArray.get(i).splitHand1.finalTotal > 21) {
							
							playersArray.get(i).splitHand1.bust = true;
							
						}
						
					} else {
						
						do {
							
							System.out.println(playersArray.get(i).showSplitHand1());
							System.out.println(playersArray.get(i).name + ", do you want to Hit (H) or Stand (S) for your first hand?");
							answer = input.next();
						
							if(answer.equals("H") || answer.equals("h")) {
							
								playersArray.get(i).splitHand1.addCard(deck.getCard());
								System.out.println(playersArray.get(i).showSplitHand1());
								System.out.println(playersArray.get(i).splitHand1.getTotal());
				
							} else if(answer.equals("S") || answer.equals("s")) {
					
								break;

							} 
							
							if(playersArray.get(i).splitHand1.finalTotal > 21) {
								
								playersArray.get(i).splitHand1.bust = true;
								
							}
							
							
						} while(playersArray.get(i).splitHand1.finalTotal < 21);
					}
					
					if(playersArray.get(i).splitHand2.doubleDown) {
						
			
							System.out.println(playersArray.get(i).name + " gets one more card.");
							playersArray.get(i).splitHand2.addCard(deck.getCard());
							System.out.println(playersArray.get(i).showSplitHand2());
							System.out.println(playersArray.get(i).splitHand2.getTotal());
						
						if(playersArray.get(i).splitHand2.finalTotal > 21) {
							
							playersArray.get(i).splitHand2.bust = true;
							
						}
						
					} else {
						
						do {
							
							System.out.println(playersArray.get(i).showSplitHand2());
							System.out.println(playersArray.get(i).name + ", do you want to Hit (H) or Stand (S) for your second hand?");
							answer = input.next();
						
							if(answer.equals("H") || answer.equals("h")) {
							
								playersArray.get(i).splitHand2.addCard(deck.getCard());
								System.out.println(playersArray.get(i).showSplitHand2());
								System.out.println(playersArray.get(i).splitHand2.getTotal());
				
							} else if(answer.equals("S") || answer.equals("s")) {
					
								break;

							} 
							
							if(playersArray.get(i).splitHand2.finalTotal > 21) {
								
								playersArray.get(i).splitHand2.bust = true;
								
							}
							
							
						} while(playersArray.get(i).splitHand2.finalTotal < 21);
						
					} 
				} else { 
					
					if(playersArray.get(i).hand.doubleDown) {
						
						System.out.println(playersArray.get(i).name + " gets one more card.");
						playersArray.get(i).hand.addCard(deck.getCard());
						System.out.println(playersArray.get(i).showHand());
						System.out.println(playersArray.get(i).hand.getTotal());
						
							if(playersArray.get(i).hand.finalTotal > 21) {
							
							playersArray.get(i).hand.bust = true;
							
						}
					} else {
						
						do {
							
						//	System.out.println(playersArray.get(i).showHand());
							System.out.println(playersArray.get(i).name + ", do you want to Hit (H) or Stand (S)?");
							answer = input.next();
				
							if(answer.equals("H") || answer.equals("h")) {
					
								playersArray.get(i).hand.addCard(deck.getCard());
								System.out.println(playersArray.get(i).showHand());
								System.out.println(playersArray.get(i).hand.getTotal());
					
							} else if(answer.equals("S") || answer.equals("s")) {
						
								break;

							} 
							
							if(playersArray.get(i).hand.finalTotal > 21) {
								
								playersArray.get(i).hand.bust = true;
								
							}
				
						} while(playersArray.get(i).hand.finalTotal < 21);
					}
							
				}
					
			}	
				
		}			
					
			
		
	}
	
	
	public void dealerEvents() {
		
		do {
			
			dealer.addCard(deck.getCard());
			dealer.getTotal();

			if(dealer.finalTotal > 21) {
				
				dealer.bust = true;
				
			}
			
		} while(dealer.total < 17 || dealer.aceTotal < 17 );
		
		System.out.println("Dealer has: " + dealer.toString());
		System.out.println(dealer.finalTotal);
	
	}
	
	public void distrubuteBets() {

		for(int i = 0; i < playersArray.size(); i++) {
			
			
			if(playersArray.get(i).splitHand) {
				
				if(playersArray.get(i).splitHand1.bust == true && dealer.finalTotal <= 21) {
					
					playersArray.get(i).splitHand1.result = "lose";
					
				} else if(dealer.bust == true && playersArray.get(i).splitHand1.finalTotal <= 21) {
					
					playersArray.get(i).splitHand1.result = "win";
					
				} else if(dealer.bust == true && playersArray.get(i).splitHand1.bust == true) {
					
					playersArray.get(i).splitHand1.result = "lose";
					
				} else if(playersArray.get(i).splitHand1.finalTotal == 21 && dealer.finalTotal == 21) {
					
					playersArray.get(i).splitHand1.result = "draw";
					
				} else if(dealer.finalTotal <= 21 && dealer.finalTotal > playersArray.get(i).splitHand1.finalTotal) {
					
					playersArray.get(i).splitHand1.result = "lose";
					
				} else if(playersArray.get(i).splitHand1.finalTotal <= 21 && playersArray.get(i).splitHand1.finalTotal > dealer.finalTotal) {
					
					playersArray.get(i).splitHand1.result = "win";
					
				} else if(playersArray.get(i).splitHand1.finalTotal == dealer.finalTotal) {
					
					playersArray.get(i).splitHand1.result = "draw";
						
				}
				
				if(playersArray.get(i).splitHand2.bust == true && dealer.finalTotal <= 21) {
					
					playersArray.get(i).splitHand2.result = "lose";
					
				} else if(dealer.bust == true && playersArray.get(i).splitHand2.finalTotal <= 21) {
					
					playersArray.get(i).splitHand2.result = "win";
					
				} else if(dealer.bust == true && playersArray.get(i).splitHand2.bust == true) {
					
					playersArray.get(i).splitHand2.result = "lose";
					
				} else if(playersArray.get(i).splitHand2.finalTotal == 21 && dealer.finalTotal == 21) {
					
					playersArray.get(i).splitHand2.result = "draw";
					
				} else if(dealer.finalTotal <= 21 && dealer.finalTotal > playersArray.get(i).splitHand2.finalTotal) {
					
					playersArray.get(i).splitHand2.result = "lose";
					
				} else if(playersArray.get(i).splitHand2.finalTotal <= 21 && playersArray.get(i).splitHand2.finalTotal > dealer.finalTotal) {
					
					playersArray.get(i).splitHand2.result = "win";
					
				} else if(playersArray.get(i).splitHand2.finalTotal == dealer.finalTotal) {
					
					playersArray.get(i).splitHand2.result = "draw";
					
				}	
				
			} else {
				
				if(playersArray.get(i).hand.bust == true && dealer.finalTotal <= 21) {
					
					playersArray.get(i).hand.result = "lose";
					
				} else if(dealer.bust == true && playersArray.get(i).hand.finalTotal <= 21) {
					
					playersArray.get(i).hand.result = "win";
					
				} else if(dealer.bust == true && playersArray.get(i).hand.bust == true) {
					
					playersArray.get(i).hand.result = "lose";
					
				} else if(playersArray.get(i).hand.finalTotal == 21 && dealer.finalTotal == 21) {
					
					playersArray.get(i).hand.result = "draw";
					
				} else if(dealer.finalTotal <= 21 && dealer.finalTotal > playersArray.get(i).hand.finalTotal) {
					
					playersArray.get(i).hand.result = "lose";
					
				} else if(playersArray.get(i).hand.finalTotal <= 21 && playersArray.get(i).hand.finalTotal > dealer.finalTotal) {
					
					playersArray.get(i).hand.result = "win";
					
				} else if(playersArray.get(i).hand.finalTotal == dealer.finalTotal) {
					
					playersArray.get(i).hand.result = "draw";
					
				}
	
			}
			
		
		}
		
		
	}
	
	public void displayUpdatedBank() {
		
		for(int i = 0; i < playersArray.size(); i++) {
			
			
			if(playersArray.get(i).splitHand) {
				
				
				if(playersArray.get(i).splitHand1.result.equals("win") && playersArray.get(i).hand.blackjack && playersArray.get(i).splitHand == false ) {
					
					playersArray.get(i).blackjackBonusMoney(playersArray.get(i).hand.getBet());
					
				} else if(playersArray.get(i).splitHand1.result.equals("win")) {
					
					playersArray.get(i).addMoney(playersArray.get(i).hand.getBet());
					
				} 
				
				else if(playersArray.get(i).splitHand1.result.equals("lose")) {
					
					playersArray.get(i).minusMoney(playersArray.get(i).hand.getBet());
					
				} else if(playersArray.get(i).splitHand1.result.equals("draw")) {
					
				}
				
				if(playersArray.get(i).splitHand2.result.equals("win") && playersArray.get(i).hand.blackjack && playersArray.get(i).splitHand == false ) {
					
					playersArray.get(i).blackjackBonusMoney(playersArray.get(i).hand.getBet());
					
				} else if(playersArray.get(i).splitHand2.result.equals("win")) {
					
					playersArray.get(i).addMoney(playersArray.get(i).hand.getBet());
					
				} 
				
				else if(playersArray.get(i).splitHand2.result.equals("lose")) {
					
					playersArray.get(i).minusMoney(playersArray.get(i).hand.getBet());
					
				} else if(playersArray.get(i).splitHand2.result.equals("draw")) {
					
				}
				
				System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
				
				
				
				
			} else {
				
				if(playersArray.get(i).hand.result.equals("win") && playersArray.get(i).hand.blackjack && playersArray.get(i).splitHand == false ) {
					
					playersArray.get(i).blackjackBonusMoney(playersArray.get(i).hand.getBet());
					System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
				} else if(playersArray.get(i).hand.result.equals("win")) {
					
					playersArray.get(i).addMoney(playersArray.get(i).hand.getBet());
					System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
				} 
				
				
				else if(playersArray.get(i).hand.result.equals("lose")) {
					
					playersArray.get(i).minusMoney(playersArray.get(i).hand.getBet());
					System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
				} else if(playersArray.get(i).hand.result.equals("draw")) {
					
					System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
				}
				
			}
			
		}
		
	}
	
	public void clearHand() {
		
		dealer.clearHand();
		for(int i = 0; i < playersArray.size(); i++) {
			playersArray.get(i).hand.clearHand();
		}
		
	}
	
	public boolean newRound() {
		String answer;
		
		boolean newRound = false;
		
		for(int i = 0; i < playersArray.size(); i++) {
			
			if(playersArray.get(i).money>0) {
				
				System.out.println(playersArray.get(i).name + ", do you want to continue playing?");
				answer = input.next();
				
				if(answer.equals("Yes")||answer.equals("yes")) {
					
				} else {
				
					System.out.println(playersArray.get(i).name + ", thanks for playing!");
					playersArray.remove(i);
					i = i -1;
					
				}
				
			} else {
				
				System.out.println(playersArray.get(i).name + " you have no more money, game over!");
				playersArray.remove(i);
				i = i - 1;
				
			}
		}
		
		if(playersArray.size()!=0) {
			newRound = true;
			for(int i = 0; i< playersArray.size();i++) {
				playersArray.get(i).hand.clearHand();
				playersArray.get(i).splitHand1.clearHand();
				playersArray.get(i).splitHand2.clearHand();
				playersArray.get(i).splitHand = false;
			}
		
		}
		
		return newRound;
		
	}
	
	public void endGame() {
		
		if(playersArray.size() == 0) {
			System.out.println("No more players left");
			System.exit(1);
		}
		
		
	}
}
