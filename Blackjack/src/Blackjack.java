import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack extends Deck {
    static public int handValue(ArrayList<String> hand) {
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

    public static void main(String[] args) {
        int playerCount = 1;//player count
        int totalStands = 0;//counter for how many players left before dealer shows cards and draws cards
        String[] table = new String[playerCount];
        Dealer dealer = new Dealer();//dealer
        Deck deck = new Deck();//deck
        Scanner myObj = new Scanner(System.in);//input

        shuffle(deck, 52);//shuffle deck
        dealer.printDeck(deck);//print shuffled deck
        Player[] players = new Player[playerCount];//create player array
        for (int i = 0; i < playerCount; i++) {//instantiate player objects
            players[i] = new Player();
            table[i] = "wait";
        }
        dealer.drawCard(dealer, deck);//dealer draws card 1
        dealer.drawCard(dealer, deck);//dealer draws card 2
        System.out.println("Dealer's hand: ");
        dealer.printHidden(dealer.hand);//shows first card but not second
        while (totalStands < playerCount && playerCount == 1) {//while not all players have chosen to stand
            for (int i = 0; i < playerCount; i++) {
                if (players[i].hand.size() == 0) {
                    dealer.drawCard(players[i], deck);//inital hand for players
                    dealer.drawCard(players[i], deck);//inital hand for players
                }
                if (players[i].stand == true) {
                    totalStands++;
                } else if (players[i].stand == false) {
                    System.out.print("Player " + (i + 1) + "'s" + " Hand\n");
                    dealer.printHand(players[i].hand);//print players hand
                    System.out.println("Stand or Hit");
                    String input = myObj.nextLine();
                    if (input.compareTo("hit") == 0)
                        dealer.drawCard(players[i], deck);
                    else if (input.compareTo("stand") == 0)
                        players[i].stand = true;
                    if (handValue(players[i].hand) > 21) {
                        players[i].stand = true;
                        table[i] = "Broke";
                    }

                }
            }
            if (totalStands < playerCount)
                totalStands = 0;
        }//end of while
        if (playerCount == 1) {
            if (table[0].compareTo("wait") == 0) {
                if (handValue(dealer.hand) > handValue(players[0].hand))
                    table[0] = "Lost";
                while (handValue(dealer.hand) <= handValue(players[0].hand)) {
                    int value = handValue(players[0].hand);
                    //System.out.println("Dealer Value: " + handValue(dealer.hand) + " Player Value: " + value);

                    if (handValue(dealer.hand) == 21 && value == 21)
                        table[0] = "Tied";
                    else if (handValue(dealer.hand) <= value) {
                        dealer.drawCard(dealer, deck);

                        //System.out.println("After Card Drawn, Dealer Value: " + handValue(dealer.hand) + " Player Value: " + value);
                        if(handValue(dealer.hand)>=17 && handValue(players[0].hand)>handValue(dealer.hand))
                        {
                            table[0]="Won";

                        }
                        if (handValue(dealer.hand) > value && handValue(dealer.hand) <= 21)
                            table[0] = "Lost";
                        else if (handValue(dealer.hand) > value && handValue(dealer.hand) > 21)
                            if (value != 21)
                                table[0] = "Won";
                            else
                                table[0] = "Blackjack";


                    } else if (handValue(dealer.hand) > value && handValue(dealer.hand) <= 21)
                        table[0] = "Lost";
                    else if (handValue(dealer.hand) > 21)
                        table[0] = "Won";
                }
            }
            System.out.println("Dealer's Final Hand");
            dealer.printHand(dealer.hand);
            System.out.println("Dealer's Hand Value: "+ handValue(dealer.hand));

            for (int i = 0; i < playerCount; i++) {
                System.out.println("Player " +(i+1) +"'s Hand");
                dealer.printHand(players[i].hand);
                System.out.println("Player " +(i+1) +"'s Hand Value: "+handValue(players[i].hand));
                System.out.println("Player " + (i + 1) + ":" + table[i]);
            }
        }
    }
}
