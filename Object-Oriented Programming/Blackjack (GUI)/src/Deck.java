import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
/**
 * Class representing a deck using card class
 * 
 * @author Fabio Tran
 */
public class Deck {
	
	Card[] deck = new Card[52];
	Random random = new Random();
	private int counter = 0;
/*
 * Creates deck object with 52 unique cards	
 */
	public Deck() {
		int count = 0;
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card(i,"Hearts");
		}
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card(i,"Diamonds");
		}
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card(i,"Spades");
		}
		for (int i = 1; i <= 13; i++) {
			deck[count++] = new Card(i,"Clubs");
		}
	}
/*
 * toString method for the class
 */
	public String toString() {
		StringBuilder str = new StringBuilder();
	
		for(int i = 0; i < 52; i++) {
			str.append(deck[i]);
			str.append("\n");
		}
		return str.toString();
	}
/*
 * Shuffles order of the cards
 */	
	public void shuffle() {
		
		List<Card> list = Arrays.asList(deck);
		Collections.shuffle(list);
		
		for(int i = 0; i < list.size(); i ++) {
			list.get(i);
		}
		for(int i = 0; i < 52; i++) {
			deck[i] = list.get(i);
		}
		
	}
	public Card getCard() {
		Card card = deck[counter];
		counter++;
		return card;
	}
	
}
