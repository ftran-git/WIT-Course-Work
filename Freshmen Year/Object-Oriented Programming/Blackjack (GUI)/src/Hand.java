import java.util.ArrayList;
import java.lang.StringBuilder;

public class Hand extends HandAbstract {
	
	int total = 0;
	int aceTotal = 0;
	int finalTotal = -1;
	int bet;
	boolean ace = false;
	boolean blackjack = false;
	boolean bust = false;
	String result = null; //win, draw, lose
	
	ArrayList<Card> hand = new ArrayList<>();
	
	public void addCard(Card card) {
		hand.add(card);
	}
	public void clearHand() {
		hand.clear();
		total = 0;
		aceTotal = 0;
		finalTotal = -1;
		ace = false;
		blackjack = false;
		bust = false;
		result = null; //win, draw, lose
	}
	public int getBet() {
		return bet;
	}
	public void bet(int bet) {
		this.bet = bet;
	}
	public void doubleBet() {
		this.bet = this.bet * 2;
	}
	public int getSize() {
		return hand.size();
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < hand.size(); i++) {
			sb.append(hand.get(i));
			if(i != hand.size()-1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	@Override
	public String getTotal() {
		
		total = 0;
		aceTotal = 0;
		
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getValue() == 1) {
				ace = true;
				total = total + 1;
				aceTotal = aceTotal + 11;
			} else if(hand.get(i).getValue() == 2) {
				total = total + 2;
				aceTotal = aceTotal + 2;
			} else if(hand.get(i).getValue() == 3) {
				total = total + 3;
				aceTotal = aceTotal + 3;
			} else if(hand.get(i).getValue() == 4) {
				total = total + 4;
				aceTotal = aceTotal + 4;
			} else if(hand.get(i).getValue() == 5) {
				total = total + 5;
				aceTotal = aceTotal + 5;
			} else if(hand.get(i).getValue() == 6) {
				total = total + 6;
				aceTotal = aceTotal + 6;
			} else if(hand.get(i).getValue() == 7) {
				total = total + 7;
				aceTotal = aceTotal + 7;;
			} else if(hand.get(i).getValue() == 8) {
				total = total + 8;
				aceTotal = aceTotal + 8;
			} else if(hand.get(i).getValue() == 9) {
				total = total + 9;
				aceTotal = aceTotal + 9;
			} else if(hand.get(i).getValue() == 10) {
				total = total + 10;
				aceTotal = aceTotal + 10;
			} else if(hand.get(i).getValue() == 11) {
				total = total + 10;
				aceTotal = aceTotal + 10;
			} else if(hand.get(i).getValue() == 12) {
				total = total + 10;
				aceTotal = aceTotal + 10;
			} else if(hand.get(i).getValue() == 13) {
				total = total + 10;
				aceTotal = aceTotal + 10;
			}
		}	

		//determines what the final total is 
			if((total == 21 || aceTotal == 21) && (hand.size() == 2)) {
				finalTotal = 21;
				blackjack = true;
				return "Total: 21, You have blackjack!";
			} else if (ace && total < 21 && aceTotal < 21) {
				if(total > aceTotal) {
					finalTotal = total;
				} else if(aceTotal > total) {
					finalTotal = aceTotal;
				}
				return "Total: " + Integer.toString(total) + " or " + Integer.toString(aceTotal);
			} else if (ace && total<21 && aceTotal>21) {
				finalTotal = total;
				return ("Total: " + Integer.toString(total));
			} else if(ace && aceTotal<21 && total>21) {
				finalTotal = aceTotal;
				return ("Total: " + Integer.toString(aceTotal));
			}
			else {
				finalTotal = total;
				return "Total: " + Integer.toString(total);
			}
		
	}
	
}
