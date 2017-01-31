package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.SpaceGame;

public class CommandAddHost implements Consumer<ArrayList<ThingHost<SpaceGame>>>, Serializable {
	private static final long serialVersionUID = 3971484296007941716L;

	private ThingHost<SpaceGame> host;
	
	public CommandAddHost(ThingHost<SpaceGame> host) {
		this.host = host;
	}
	
	@Override
	public void accept(ArrayList<ThingHost<SpaceGame>> li) {
		li.add(host);
	}

}
