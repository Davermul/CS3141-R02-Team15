package com.example.demo3;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.example.demo3.CardFaceCreator.createFrontFace;

public class Card extends Parent {
    public final Suit suit;
    public final Rank rank;
    public final int value;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;


        if(rank ==rank.ACE) {this.value=11;}
        else if(rank ==rank._2) {this.value=2;}
        else if(rank ==rank._3) {this.value=3;}
        else if(rank ==rank._4) {this.value=4;}
        else if(rank ==rank._5) {this.value=5;}
        else if(rank ==rank._6) {this.value=6;}
        else if(rank ==rank._7) {this.value=7;}
        else if(rank ==rank._8) {this.value=8;}
        else if(rank ==rank._9) {this.value=9;}
        else if(rank ==rank._10) {this.value=10;}
        else if(rank ==rank.JACK) {this.value=10;}
        else if(rank ==rank.QUEEN) {this.value=10;}
        else if(rank ==rank.KING) {this.value=10;}
        else {this.value=1000;}


        Node image = createFrontFace(suit,rank,120,180);


/*
        Rectangle background = new Rectangle(120,180);


        Text text = new Text(toString());
        text.setWrappingWidth(70);
        getChildren().add(new StackPane(background,text));
        */

        getChildren().add(image);

    }

    public String toString(){
        return rank.toString() + " of " + suit.toString();
    }

}
