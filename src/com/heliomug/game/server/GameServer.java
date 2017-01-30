package com.heliomug.game.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer<S extends Serializable, D extends MessageDisplayer> {
	public static final int DEFAULT_PORT_NUMBER = 27960; 
	public static final String HOST = "home.heliomug.com";

	private static final int THREAD_SLEEP_TIME = 1;
	
	private S thing;
	private D displayer; 
	
	private List<GuestServer<S, D>> clientServers;
	
	public GameServer(D displayer) {
		clientServers = new ArrayList<>();
		thing = null;
		this.displayer = displayer;
	}

	public S getThing() {
		return thing;
	}
	
	public double getAvgServedPerSec() {
		if (clientServers.size() == 0) {
			return 0;
		}
		
		double tot = 0;
		for (GuestServer<S, D> server : clientServers) {
			tot += server.getServedPerSec();
		}
		return tot / clientServers.size();
	}
	
	public int getTotalCommandsReceived() {
		int tot = 0;
		for (GuestServer<S, D> server : clientServers) {
			tot += server.getCommandsPulled();
		}
		return tot;
	}
	
	public List<GuestServer<S, D>> getConnections() {
		return clientServers;
	}

	public void start(int port, S thing) {
		this.thing = thing;
		Thread t = new Thread(() -> {
			clientServers = new ArrayList<>();
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				displayer.displayMessage("starting server");
				while (true) {
					Socket incoming = serverSocket.accept();
					GuestServer<S, D> clientServer = new GuestServer<>(incoming, this.thing);
					clientServer.start();
					clientServers.add(clientServer);
					displayer.displayMessage("accepted new client");
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
