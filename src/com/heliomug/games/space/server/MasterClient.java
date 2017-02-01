package com.heliomug.games.space.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import com.heliomug.games.space.Game;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

// This class exists because I got tired of writing / messing up the generic type
public class MasterClient extends Client<CopyOnWriteArrayList<Server<Game>>> {

	public MasterClient(InetAddress host, int port) throws IOException {
		super(host, port);
	}
}
