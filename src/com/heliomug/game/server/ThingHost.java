package com.heliomug.game.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.heliomug.utils.gui.MessageDisplayer;

public class ThingHost<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 5661598726684673199L;

	private static final int THREAD_SLEEP_TIME = 1;
	
	private int port;
	private InetAddress externalAddress;
	private InetAddress lanAddress;
	private T thing;
	private MessageDisplayer displayer; 
	
	private transient List<ThingClientServer<T>> clientServers;
	
	public ThingHost(T thing, int port) {
		this(thing, port, new MessageDisplayer() { 
			private static final long serialVersionUID = -1832854957955320613L;

			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		});
	}
	
	public ThingHost(T thing, int port, MessageDisplayer displayer) {
		this.thing = thing;
		this.displayer = displayer;
		this.port = port;
		clientServers = new ArrayList<>();
		externalAddress = NetworkUtils.getExternalAddress();
		lanAddress = NetworkUtils.getLanAddress();
	}

	public InetAddress getExternalAddress() {
		return externalAddress;
	}
	
	public InetAddress getLanAddress() {
		return lanAddress;
	}
	
	public int getPort() {
		return port;
	}
	
	public T getThing() {
		return thing;
	}
	
	public double getAvgServedPerSec() {
		if (clientServers.size() == 0) {
			return 0;
		}
		
		double tot = 0;
		for (ThingClientServer<T> server : clientServers) {
			tot += server.getServedPerSec();
		}
		return tot / clientServers.size();
	}
	
	public int getTotalCommandsReceived() {
		int tot = 0;
		for (ThingClientServer<T> server : clientServers) {
			tot += server.getCommandsPulled();
		}
		return tot;
	}
	
	public List<ThingClientServer<T>> getConnections() {
		return clientServers;
	}

	public void start() {
		Thread t = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				clientServers = new ArrayList<>();
				displayer.accept("starting " + toString());
				while (true) {
					Socket incoming = serverSocket.accept();
					ThingClientServer<T> clientServer = new ThingClientServer<>(incoming, this.thing);
					clientServer.start();
					clientServers.add(clientServer);
					displayer.accept("accepted new client for " + thing);
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

	public void kill() {
		
	}
	
	public String toString() {
		return String.format("Server of %s @ %s or %s port %d", thing.toString(), lanAddress, externalAddress, port);
	}
}
