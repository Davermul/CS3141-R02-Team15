package com.example.demo3;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Player{
    private ObservableList<Node> hand;
    private  int aces = 0;
    private SimpleIntegerProperty handValue = new SimpleIntegerProperty(0);
    private int balance;
    private Integer betAmount;

    private int playerNumber;


    public Player(ObservableList<Node> hand) {
        this.hand = hand;
        this.balance = 0;
        this.balance = 1000;
        this.aces = 0;
        this.betAmount=0;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int money){
        this.balance = money;
    }

    public int getBetAmount() {return betAmount;}

    public void setBetAmount(int betAmount) {this.betAmount=betAmount;}

    public void takeCard(Card card) {
        hand.add(card);

        if (card.rank == Rank.ACE){
            aces++;
        }
        if(handValue.get() + card.value>21 && aces>0)
        {
            handValue.set(handValue.get() + card.value -10);
            aces--;
        }
        else{
            handValue.set(handValue.get()+card.value);
        }
    }

    public void reset()
    {
        hand.clear();
        handValue.set(0);
        aces=0;
    }

    public void setHand(ObservableList<Node> hand){
        this.hand=hand;
    }
    public ObservableList<Node> getHand(){
        return hand;
    }

    public SimpleIntegerProperty getHandValue(){
        return handValue;
    }

    public void setHandValue(SimpleIntegerProperty handValue){this.handValue=handValue;}

    public int getPlayerNumber(){return playerNumber;}
    public void setPlayerNumber(int n){this.playerNumber=n;}
}

