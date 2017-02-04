package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.function.Consumer;

import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;

public class CommandAddRemovePlayer implements Consumer<Game>, Serializable {
	private static final long serialVersionUID = 8350563567725970336L;

	private Player player;
	private boolean isAdding;
	
	public CommandAddRemovePlayer(Player player) {
		this(player, true);
	}
	
	public CommandAddRemovePlayer(Player player, boolean isAdding) {
		this.player = player;
		this.isAdding = isAdding;
	}
	
	@Override
	public void accept(Game game) {
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
