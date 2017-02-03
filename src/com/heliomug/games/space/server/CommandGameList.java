package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class CommandGameList implements Consumer<CopyOnWriteArrayList<GameAddress>>, Serializable {
	private static final long serialVersionUID = 3971484296007941716L;

	private GameAddress gameAddress;
	private boolean isAdd;
	
	
	public CommandGameList(GameAddress gameAddress) {
		this(gameAddress, true);
	}
	
	public CommandGameList(GameAddress gameAddress, boolean isAdd) {
		this.gameAddress = gameAddress;
		this.isAdd = isAdd;
	}
	
	@Override
	public void accept(CopyOnWriteArrayList<GameAddress> li) {
		if (isAdd) {
			li.add(gameAddress);
		} else {
			li.remove(gameAddress);
		}
	}

}
