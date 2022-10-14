import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    public int balance;
    public ArrayList<String> hand = new ArrayList<>();
    public boolean stand;

    public Player() {
        this.balance = 0;
        stand=false;
    }

    public void printHand(ArrayList<String> hand) {
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i) + ",");
        }
        System.out.println();
    }


}
