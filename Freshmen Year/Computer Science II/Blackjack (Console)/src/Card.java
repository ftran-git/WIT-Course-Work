/**
 * Class representing a card from a deck
 * 
 * @author Fabio Tran
 */

public class Card {

	private String suit;
	private int value;
	
	public Card(int value, String suit) {
		this.suit = suit;
		this.value = value;
	}
	
	public String toString() {
		return getValueName() + " of " + suit;
	}
	
	public String getSuitName() {
		return this.suit;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getValueName() {
		String valueName;
		
		if(value == 1) {
			valueName = "Ace";
		} else if (value == 2) {
			valueName = "Two";
		} else if (value == 3) {
			valueName = "Three";
		} else if (value == 4) {
			valueName = "Four";
		} else if (value == 5) {
			valueName = "Five";
		} else if (value == 6) {
			valueName = "Six";
		} else if (value == 7) {
			valueName = "Seven";
		} else if (value == 8) {
			valueName = "Eight";
		} else if (value == 9) {
			valueName = "Nine";
		} else if (value == 10) {
			valueName = "Ten";
		} else if (value == 11) {
			valueName = "Jack";
		} else if (value == 12) {
			valueName = "Queen";
		} else {
			valueName = "King";
		}
		return valueName;
	}
	

}
