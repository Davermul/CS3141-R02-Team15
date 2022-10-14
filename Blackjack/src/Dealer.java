public class Dealer extends Player {
    public Deck deck;
    public Dealer()
    {
        this.deck=new Deck();
    }
    public void drawCard(Player player)//draws individual cards
    {
        String [] newDeck = player.deck.getDeck();
        player.hand.add(newDeck[player.hand.size()]);

    }
    public void drawStart(Player player)//Draws 2 cards to start
    {
        for(int i=0;i<2;i++)
        {
            drawCard(player);
        }
    }
    public void printDeck(Deck deck)//prints the deck
    {
        for (int i = 0; i < 52; i++) {
            System.out.print(deck.getDeck()[i] + " ");
        }
        System.out.println();
    }

}
