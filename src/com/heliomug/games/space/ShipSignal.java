package com.heliomug.games.space;

import java.util.function.Consumer;

public enum ShipSignal {
	TURN_LEFT("left", (Ship ship) -> {
		ship.setTurnDirection(TurnDirection.LEFT);
	}),
	TURN_RIGHT("right", (Ship ship) -> {
		ship.setTurnDirection(TurnDirection.RIGHT);
	}),
	TURN_NONE("forward", (Ship ship) -> {
		ship.setTurnDirection(TurnDirection.NONE);
	}),
	ACCEL_ON("boost", (Ship ship) -> {
		ship.setAccel(true);
	}),
	ACCEL_OFF("chill", (Ship ship) -> {
		ship.setAccel(false);
	}),
	FIRE("fire", (Ship ship) -> {
		ship.fire();
	});
	
	private Consumer<Ship> consumer;
	private String name;
	
	private ShipSignal(String name, Consumer<Ship> consumer) {
		this.name = name;
		this.consumer = consumer;
	}
	
	public void apply(Ship ship) {
		this.consumer.accept(ship);
	}
	
	public String toString() {
		return name;
	}
}
