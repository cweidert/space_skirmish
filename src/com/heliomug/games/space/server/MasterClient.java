package com.heliomug.games.space.server;

import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;

// This class exists because I got tired of writing / messing up the generic type
public class MasterClient extends ThingClient<CopyOnWriteArrayList<ThingHost<Game>>> {

	public MasterClient(InetAddress host, int port) {
		super(host, port);
	}
}
