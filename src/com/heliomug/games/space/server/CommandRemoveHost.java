package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;

public class CommandRemoveHost implements Consumer<CopyOnWriteArrayList<ThingHost<Game>>>, Serializable {
	private static final long serialVersionUID = 3971484296007941716L;

	private ThingHost<Game> host;
	
	public CommandRemoveHost(ThingHost<Game> host) {
		this.host = host;
	}
	
	@Override
	public void accept(CopyOnWriteArrayList<ThingHost<Game>> li) {
		li.remove(host);
	}

}
