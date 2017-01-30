package com.heliomug.game.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMaster<S extends Serializable> {
	public static final int PORT_NUMBER = 27960; 
	public static final String HOST = "home.heliomug.com";

	private static final int THREAD_SLEEP_TIME = 1;
	
	private S thing;
	
	private List<ServerPerClient<S>> clientServers;
	
	public ServerMaster() {
		clientServers = new ArrayList<>();
		thing = null;
	}

	public S getThing() {
		return thing;
	}
	
	public double getAverageServedGamesPerSec() {
		if (clientServers.size() == 0) {
			return 0;
		}
		
		double tot = 0;
		for (ServerPerClient<S> server : clientServers) {
			tot += server.getGamesServedPerSec();
		}
		return tot / clientServers.size();
	}
	
	public int getTotalCommandsReceived() {
		int tot = 0;
		for (ServerPerClient<S> server : clientServers) {
			tot += server.getCommandsPulled();
		}
		return tot;
	}
	
	public List<ServerPerClient<S>> getConnections() {
		return clientServers;
	}

	public void start(S thing) {
		this.thing = thing;
		Thread t = new Thread(() -> {
			clientServers = new ArrayList<>();
			try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
				while (true) {
					Socket incoming = serverSocket.accept();
					ServerPerClient<S> handler = new ServerPerClient<>(incoming, this.thing);
					handler.start();
					clientServers.add(handler);
					try {
						Thread.sleep(THREAD_SLEEP_TIME);
					} catch (InterruptedException e) {
						System.err.println("Incoming sleep interrupted");
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		t.setDaemon(true);
		t.start();
	}
}
