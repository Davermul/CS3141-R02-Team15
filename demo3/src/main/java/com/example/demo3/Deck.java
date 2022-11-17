package com.example.demo3;



import java.util.Random;

public class Deck{
    private Card[] cards = new Card[52];

    public Deck(){stack();}

    public final void stack(){
        int i = 0;
        for (Suit suit : Suit.values()){
            for (Rank rank : Rank.values()){
                cards[i++] = new Card(suit,rank);
            }
        }
    }

    public Card drawCard(){
        Card card = null;
            while (card == null) {
                int index = (int) (Math.random() * cards.length);
                card = cards[index];
                cards[index] = null;
            }
         return card;
        }

    public int searchDealerDraw(){
        Card card = null;
        int index = 9999;
        while (card == null) {
            index = (int) (Math.random() * cards.length);
            card = cards[index];
        }
        return index;
    }

    public Card dealerDraw(int index){
        Card card = cards[index];
        return card;
    }
    }

