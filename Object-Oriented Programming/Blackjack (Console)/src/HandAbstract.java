import java.util.ArrayList;

public abstract class HandAbstract {
	ArrayList<Card> hand = new ArrayList<>();
	int total = 0;
	int aceTotal = 0;
	int finalTotal = 0;
	boolean ace = false;
	boolean blackjack = false;
	boolean bust = false;
	String result = null; //win, draw, lose
	
	public void addCard() {
		
	}
	
	public void clearHand() {
		hand.clear();
	}
	public String toString() {
		return "";
	}
	
	abstract public String getTotal();
	
	
	
}
