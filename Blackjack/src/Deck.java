import java.util.Random;

public class Deck{
    public String[] deck;
    public int position;

    public Deck() {
        this.deck = new String[]{"HAce","DAce","SAce","CAce",
                            "HTwo","DTwo","STwo","CTwo",
                            "HThree","DThree","SThree","CThree",
                            "HFour","DFour","SFour","CFour",
                            "HFive","DFive","SFive","CFive",
                            "HSix","DSix","SSix","CSix",
                            "HSeven","DSeven","SSeven","CSeven",
                            "HEight","DEight","SEight","CEight",
                            "HNine","DNine","SNine","CNine",
                            "HTen","DTen","STen","CTen",
                            "HJack","DJack","SJack","CJack",
                            "HQueen","DQueen","SQueen","CQueen",
                            "HKing","DKing","SKing","CKing"};
        this.position=0;
    }

    public String[] getDeck() {
        return deck;
    }

    public static void shuffle(Deck deck, int n) {

        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            // Random for remaining positions.
            int r = i + rand.nextInt(52 - i);

            //swapping the elements
            String temp = deck.getDeck()[r];
            deck.getDeck()[r] = deck.getDeck()[i];
            deck.getDeck()[i] = temp;

        }
    }
}
