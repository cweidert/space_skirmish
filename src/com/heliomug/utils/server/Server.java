package com.heliomug.utils.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Server<T extends Serializable> implements Runnable, Serializable {
	private static final long serialVersionUID = -1495399281290298844L;

	private static final int SLEEP_TIME = 5;
	
	private Address address;
	private int port;
	
	transient private Thread serverThread;
	
	private T thing;

	public Server(T thing, int port) {
		this.thing = thing;
		this.port = port;
		this.address = new Address();
	}
	
	public T getThing() {
		return thing;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public void start() {
		serverThread = new Thread(this);
		serverThread.setDaemon(true);
		serverThread.start();
		System.out.println("-Started server \n\t" + this); 
	}
	
	public void stop() {
		System.out.println("-Stopping server \n\t" + this);
		if (serverThread != null) {
			serverThread.interrupt();
			serverThread = null;
		}
	}
	
	public void run() {
		try {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				while (!Thread.currentThread().isInterrupted()) {
					Socket incoming = serverSocket.accept();
					ServerPerClient<T> serverPerClient = new ServerPerClient<>(thing, incoming);
					serverPerClient.start();
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						System.err.println("Interruption for server " + Server.this);
						break;
					}
				}
			} finally {
				stop();
			}
		} catch (IOException e) {
			System.err.println("IO Exception for server " + Server.this);
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return String.format("Server on %s (port %d) for %s", address, port, thing);
	}
}
