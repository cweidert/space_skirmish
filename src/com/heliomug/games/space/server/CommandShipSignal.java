package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.function.Consumer;

import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.VehicleSignal;

public class CommandShipSignal implements Consumer<Game>, Serializable {
	private static final long serialVersionUID = -1189353728178342673L;

	private Player player;
	private VehicleSignal signal;
	
	public CommandShipSignal(Player player, VehicleSignal signal) {
		this.player = player;
		this.signal = signal;
	}
	
	@Override
	public void accept(Game game) {
		game.handleShipSignal(player, signal);
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", player, signal);
	}
}
