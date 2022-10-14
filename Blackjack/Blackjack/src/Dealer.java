import java.util.ArrayList;

public class Dealer extends Player {
    public Deck deck;
    public ArrayList<String> hand = new ArrayList<>();
    public Dealer()
    {
        this.deck=new Deck();
    }
    public void drawCard(Player player,Deck deck)
    {
        String [] newDeck = deck.getDeck();
        player.hand.add(newDeck[deck.position]);
        deck.position++;

    }
    public void drawCard(Dealer dealer,Deck deck)
    {
        String [] newDeck = deck.getDeck();
        dealer.hand.add(newDeck[deck.position]);
        deck.position++;

    }
    public void printHand(ArrayList<String> hand) {
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i) + ",");
        }
        System.out.println();
    }
    public void printHidden(ArrayList<String> hand)
    {
        for (int i = 0; i < hand.size(); i++) {
            if(i==0)
            System.out.print(hand.get(i) + ",");
            else
                System.out.print("hid" + ",");
        }
        System.out.println();
    }

    public void printDeck(Deck deck)
    {
        for (int i = 0; i < 52; i++) {
            System.out.print(deck.getDeck()[i] + " ");
        }
        System.out.println();
    }
}
