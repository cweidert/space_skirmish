package com.heliomug.games.space.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

import com.heliomug.games.space.Game;
import com.heliomug.utils.server.Address;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

public class GameAddress implements Serializable {
	private static final long serialVersionUID = 3952841765629415992L;
	
	private Address address;
	private int port;
	private String name;
	
	
	
	public GameAddress(Server<Game> server) {
		this(server, "");
	}
	
	public GameAddress(Server<Game> server, String name) {
		this(server.getAddress(), server.getPort(), name);
	}
	
	public GameAddress(Address address, int port, String name) {
		this.address = address;
		this.port = port;
		this.name = name;
	}

	public GameAddress(InetAddress address, int port) {
		this(new Address(address), port, "");
	}
	
	public boolean isLocal() {
		return address.isLocal();
	}
	
	public String getName() {
		return name;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(address, port);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other instanceof GameAddress) {
			GameAddress ga = (GameAddress) other;
			return ga.address.equals(address) && ga.port == port;
		}
		return false;
	}
	
	public Client<Game> getClientFor() throws IOException {
		Client<Game> client = new Client<>(address.getIP(), port);
		return client;
	}
	
	public String toString() {
		return String.format("%s @ %s : %s", name, address, port);
	}
}
