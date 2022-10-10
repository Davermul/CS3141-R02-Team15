public class Dealer extends Player {
    public Deck deck;
    public Dealer()
    {
        this.deck=new Deck();
    }
    public void drawCard(Player player)
    {
        String [] newDeck = player.deck.getDeck();
        player.hand.add(newDeck[player.hand.size()]);

    }
    public void printDeck(Deck deck)
    {
        for (int i = 0; i < 52; i++) {
            System.out.print(deck.getDeck()[i] + " ");
        }
        System.out.println();
    }
}
