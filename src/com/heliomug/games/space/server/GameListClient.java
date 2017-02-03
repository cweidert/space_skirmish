package com.heliomug.games.space.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import com.heliomug.utils.server.Client;

// This class exists because I got tired of writing / messing up the generic type
public class GameListClient extends Client<CopyOnWriteArrayList<GameAddress>> {

	public GameListClient(InetAddress host, int port) throws IOException {
		super(host, port);
	}
}
