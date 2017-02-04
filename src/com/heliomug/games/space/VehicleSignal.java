package com.heliomug.games.space;

public enum VehicleSignal {
	TURN_LEFT("left"),
	TURN_RIGHT("right"),
	TURN_NONE("straight ahead"),
	FORWARD("forward"),
	ACCEL_OFF("chill"),
	BACKWARDS("back"),
	FIRE("fire");
	
	private String name;
	
	private VehicleSignal(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
