package com.heliomug.games.space.server;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import com.heliomug.games.space.Game;
import com.heliomug.utils.server.Server;

public class CommandAddHost implements Consumer<CopyOnWriteArrayList<Server<Game>>>, Serializable {
	private static final long serialVersionUID = 3971484296007941716L;

	private Server<Game> server;
	
	public CommandAddHost(Server<Game> server) {
		this.server = server;
	}
	
	@Override
	public void accept(CopyOnWriteArrayList<Server<Game>> li) {
		li.add(server);
	}

}
