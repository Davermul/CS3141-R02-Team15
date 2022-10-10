import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    public int balance;
    public Deck deck;
    public ArrayList<String> hand = new ArrayList<>();

    public Player() {
        this.deck = new Deck();
        this.balance = 0;
    }

    public void printHand(ArrayList<String> hand) {
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i) + ",");
        }
        System.out.println();
    }

    public int handValue(ArrayList<String> hand) {
        int value = 0;
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).endsWith("Ace")) {
                if (value + 11 > 21)
                    value++;
                else
                    value += 11;
            }
            if (hand.get(i).endsWith("Two"))
                value += 2;
            if (hand.get(i).endsWith("Three"))
                value += 3;
            if (hand.get(i).endsWith("Four"))
                value += 4;
            if (hand.get(i).endsWith("Five"))
                value += 5;
            if (hand.get(i).endsWith("Six"))
                value += 6;
            if (hand.get(i).endsWith("Seven"))
                value += 7;
            if (hand.get(i).endsWith("Eight"))
                value += 8;
            if (hand.get(i).endsWith("Nine"))
                value += 9;
            if (hand.get(i).endsWith("Ten"))
                value += 10;
            if (hand.get(i).endsWith("Jack"))
                value += 10;
            if (hand.get(i).endsWith("Queen"))
                value += 10;
            if (hand.get(i).endsWith("King"))
                value += 10;

        }
        return value;
    }
}
