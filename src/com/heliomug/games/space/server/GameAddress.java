package com.heliomug.games.space.server;

import java.io.Serializable;

import com.heliomug.games.space.Game;
import com.heliomug.utils.server.Address;
import com.heliomug.utils.server.Client;

public class GameAddress implements Serializable {
	private static final long serialVersionUID = 3952841765629415992L;
	
	private Address address;
	private int port;
	private String name;
	
	public GameAddress(Address address, int port, String name) {
		this.address = address;
		this.port = port;
		this.name = name;
	}

	public Client<Game> getClientFor() {
		
	}
	
}
