package com.example.demo3;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player{
    private ObservableList<Node> hand;

    private  int aces = 0;

    private SimpleIntegerProperty handValue = new SimpleIntegerProperty(0);
    public int balance;
    private int money;


    public Player(ObservableList<Node> hand) {
        this.hand = hand;
        this.balance = 0;
        this.money = 1000;
        this.aces = 0;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money){
        this.money = money;
    }

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

    public SimpleIntegerProperty getHandValue(){
        return handValue;
    }

}

