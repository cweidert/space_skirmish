package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;

public class CommandAddHost implements Consumer<ArrayList<ThingHost<Game>>>, Serializable {
	private static final long serialVersionUID = 3971484296007941716L;

	private ThingHost<Game> host;
	
	public CommandAddHost(ThingHost<Game> host) {
		this.host = host;
	}
	
	@Override
	public void accept(ArrayList<ThingHost<Game>> li) {
		li.add(host);
	}

}
