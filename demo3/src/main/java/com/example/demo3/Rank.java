package com.example.demo3;

public enum Rank {

	ACE( "A"),
	_2( "2"),
	_3("3"),
	_4("4"),
	_5("5"),
	_6("6"),
	_7("7"),
	_8("8"),
	_9("9"),
	_10("10"),
	JACK("J"),
	QUEEN("Q"),
	KING("K")
	;
	
	String name;
	
	Rank( String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
