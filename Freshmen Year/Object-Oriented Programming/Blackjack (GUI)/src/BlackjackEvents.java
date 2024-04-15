import java.util.Scanner;
import java.util.ArrayList;
/**
 * Events that occur in a round of blackjack
 * 
 * @author Fabio Tran
 */
public class BlackjackEvents {
	
	private Scanner input = new Scanner(System.in);
	DealerHand dealer;
	int players;
	ArrayList<Player> playersArray = new ArrayList<>();
	Deck deck;
	
/*
 * Displays the rules of blackjack
 */
	public String gameInstructions() {
		
		String msg = "Blackjack!" + "\n" + "Rules:" + "\n" 
		+ " - The goal of blackjack is to beat the dealer's hand without going over 21" + "\n"
		+ " - Face cards are worth 10. Aces are worth 1 or 11. Rest of the cards are worth their face value." + "\n"
		+ " - Each player starts with two cards, one of the dealer's cards is hidden until the end." + "\n"
		+ " - To 'Hit' is to ask for another card. To 'Stand' is to hold your total and end your turn." + "\n"
		+ " - If you go over 21 you bust, and the dealer wins regardless of the dealer's hand." + "\n"
		+ " - If you are dealt 21 from the start (Ace & 10), you have blackjack." + "\n"
		+ " - Blackjack means you win 1.5 the amount of your bet." + "\n"
		+ " - Dealer will hit until his/her cards total 17 or higher." + "\n";
		
		return msg;
		
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
	
	public void dealerEvents() {
		
		do {
			
			dealer.addCard(deck.getCard());
			dealer.getTotal();

			if(dealer.finalTotal > 21) {
				
				dealer.bust = true;
				
			}
			
		} while(dealer.total < 17 || dealer.aceTotal < 17 );
		

	}
	
	public void distributeBets() {

		for(int i = 0; i < playersArray.size(); i++) {
			
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
	
	public void displayUpdatedBank() {
		
		for(int i = 0; i < playersArray.size(); i++) {
			
			if(playersArray.get(i).hand.result.equals("win")) {
					
				playersArray.get(i).addMoney(playersArray.get(i).hand.getBet());
				System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
			} else if(playersArray.get(i).hand.result.equals("lose")) {
					
				playersArray.get(i).minusMoney(playersArray.get(i).hand.getBet());
				System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
			} else if(playersArray.get(i).hand.result.equals("draw")) {
					
				System.out.println(playersArray.get(i).name + " has: " + playersArray.get(i).money);
					
			}
				
		}
		
	}
	
	public void clearHand() {
		
		dealer.clearHand();
		
		for(int i = 0; i < playersArray.size(); i++) {
			
			playersArray.get(i).hand.clearHand();
			
		}
		
	}
	
}
