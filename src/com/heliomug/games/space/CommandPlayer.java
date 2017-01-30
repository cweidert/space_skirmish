package com.heliomug.games.space;

import java.io.Serializable;
import java.util.function.Consumer;

public class CommandPlayer implements Consumer<SpaceGame>, Serializable {
	private static final long serialVersionUID = 8350563567725970336L;

	private Player player;
	private boolean isAdding;
	
	public CommandPlayer(Player player) {
		this(player, true);
	}
	
	public CommandPlayer(Player player, boolean isAdding) {
		this.player = player;
		this.isAdding = isAdding;
	}
	
	@Override
	public void accept(SpaceGame game) {
		if (isAdding) {
			game.addPlayer(player);
		} else {
			game.removePlayer(player);
		}
	}
	
	@Override
	public String toString() {
		if (isAdding) {
			return "add " + player;
		} else {
			return "del " + player;
		}
	}
}
