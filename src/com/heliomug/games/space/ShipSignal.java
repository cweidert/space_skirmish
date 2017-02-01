package com.heliomug.games.space;

public enum ShipSignal {
	TURN_LEFT("left"),
	TURN_RIGHT("right"),
	TURN_NONE("straight ahead"),
	ACCEL_ON("boost"),
	ACCEL_OFF("chill"),
	FIRE("fire");
	
	private String name;
	
	private ShipSignal(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
