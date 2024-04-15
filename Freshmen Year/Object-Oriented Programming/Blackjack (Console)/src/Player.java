
public class Player {
	String name;
	int money = 1000;
	Hand hand = new Hand();
	Boolean splitHand = false;
	Hand splitHand1 = new Hand();
	Hand splitHand2 =  new Hand();
	
	
	public Player(String name) {
		this.name = name;
	}
	public void addMoney(int bet) {
		money = money + bet;
	}
	public void minusMoney(int bet) {
		money = money - bet;
	}
	public void blackjackBonusMoney(int bet) {
		money = (money) + ((bet * (3/2)));
	}
	public int getMoney() {
		return money;
	}
	
	public void getHand(Card card) {
		hand.addCard(card);
	}
	
	public String showHand() {
		return name + " has: " + hand.toString();
	}
	public String showSplitHand1() {
		return name + " has: " + splitHand1.toString();
	}
	public String showSplitHand2() {
		return name + " has: " + splitHand2.toString();
	}
	
	public Card firstCard() {
		return hand.hand.get(0);
	}
	
	public Card secondCard() {
		return hand.hand.get(1);
	}

	
	
}
