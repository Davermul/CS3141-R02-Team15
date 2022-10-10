
public class Blackjack extends Deck {

    public static void main(String[] args) {
        Player player = new Player();
        Player player2= new Player();
        Dealer dealer = new Dealer();
        //Player 1
        System.out.println("Player 1 Original");
        dealer.printDeck(player.deck);
        shuffle(player.deck, 51);
        System.out.println("Player 1 New Shuffle");
        dealer.printDeck(player.deck);
        System.out.println();
        dealer.drawCard(player);
        System.out.println("Hand after 1 card drawn: ");
        player.printHand(player.hand);
        System.out.println("Value of Hand: "+dealer.handValue(player.hand));
        dealer.drawCard(player);
        System.out.println("\nHand after 2 card drawn: ");
        player.printHand(player.hand);
        System.out.println("Value of Hand: "+dealer.handValue(player.hand));
        dealer.drawCard(player);
        System.out.println("\nHand after 3 card drawn: ");
        player.printHand(player.hand);
        System.out.println("Value of Hand: "+dealer.handValue(player.hand));
        System.out.println();
        //Player 2
        System.out.println("Player 2 Original");
        dealer.printDeck(player2.deck);
        shuffle(player2.deck, 51);
        System.out.println("Player 2 New Shuffle");
        dealer.printDeck(player2.deck);
        System.out.println();
        dealer.drawCard(player2);
        System.out.println("Hand after 1 card drawn: ");
        player.printHand(player2.hand);
        System.out.println("Value of Hand: "+dealer.handValue(player2.hand));
        dealer.drawCard(player2);
        System.out.println("\nHand after 2 card drawn: ");
        player.printHand(player2.hand);
        System.out.println("Value of Hand: "+dealer.handValue(player2.hand));
        dealer.drawCard(player2);
        System.out.println("\nHand after 3 card drawn: ");
        player.printHand(player2.hand);
        System.out.println("Value of Hand: "+dealer.handValue(player2.hand));
    }
}
