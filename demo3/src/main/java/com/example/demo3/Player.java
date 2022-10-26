package com.example.demo3;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
    public int balance;

    private int money;
    public boolean stand;
    public Deck deck;
    public ArrayList<String> hand = new ArrayList<>();

    public Player() {
        this.deck = new Deck();
        this.balance = 0;
        this.money = 1000;
        this.stand=false;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money){
        this.money = money;
    }



    public void printHand(ArrayList<String> hand) {
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i) + ",");
        }
        System.out.println();
    }


}

