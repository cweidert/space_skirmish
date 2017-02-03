package com.heliomug.games.space;

enum TurnDirection {
	RIGHT(-1),
	NONE(0),
	LEFT(1);
	
	private int value;
	
	private TurnDirection(int val) {
		value = val;
	}
	
	public int getValue() {
		return value;
	}
}
