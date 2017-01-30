package com.heliomug.games.space;

import java.io.Serializable;
import java.util.function.Consumer;

public class CommandShip implements Consumer<SpaceGame>, Serializable {
	private static final long serialVersionUID = -1189353728178342673L;

	private Player player;
	private ShipSignal signal;
	
	public CommandShip(Player player, ShipSignal signal) {
		this.player = player;
		this.signal = signal;
	}
	
	@Override
	public void accept(SpaceGame game) {
		SpaceGame spaceGame = (SpaceGame) game;
		Ship ship = spaceGame.getShip(player);
		if (ship != null) {
			signal.apply(ship);
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", player, signal);
	}
}
