package com.example.demo3;

import javafx.scene.paint.Color;

/**
 * http://en.wikipedia.org/wiki/Playing_cards_in_Unicode
 * 
 * black fill:
 *   spade: \u2660
 *   heart: \u2665
 *   diamond: \u2666
 *   club: \u2663
 * 
 * black stroke, transparent fill:
 *   spade: \u2664
 *   heart: \u2661
 *   diamond: \u2662
 *   club: \u2667
 *
 * others:
 *   joker: \uD83C\uDCCF
 *   white chess king: \u2654
 *   black chess king: \u265A
 *   white chess queen: \u2655
 *   black chess queen: \u265B
 */
public enum Suit {

	SPADES(Color.BLACK, "\u2660"),
	HEARTS(Color.RED, "\u2665"),
	CLUBS(Color.BLACK, "\u2663"),
	DIAMONDS(Color.RED, "\u2666"),
	;
	
	Color color;
	String name;
	
	Suit( Color color, String name) {
		this.color = color;
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}
}
