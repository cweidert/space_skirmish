package com.heliomug.utils.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server<T extends Serializable> implements Runnable {
	private static final int SLEEP_TIME = 5;
	
	private Address address;
	private int port;
	
	private Thread serverThread;
	
	private T thing;
	
	private boolean isActive;

	ServerSocket serverSocket;
	
	public Server(T thing, int port) {
		this.thing = thing;
		this.port = port;
		this.address = new Address();
		this.isActive = false;
		this.serverSocket = null;
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
		isActive = true;
		String message = "Started server " + this;
		Logger.getGlobal().log(Level.INFO, message); 
	}
	
	public void close() {
		if (isActive) {
			Logger.getGlobal().log(Level.INFO, "Stopping server " + this);
			if (serverThread != null) {
				serverThread.interrupt();
				serverThread = null;
			}
			if (serverSocket != null) {
				try {
					serverSocket.close();
					serverSocket = null;
				} catch (IOException e) {
					Logger.getGlobal().log(Level.WARNING, "couldn't close server socket for " + this, e);
				}
			}
		}
		isActive = false;
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while (!Thread.currentThread().isInterrupted()) {
				Socket incoming = serverSocket.accept();
				ServerPerClient<T> serverPerClient = new ServerPerClient<>(thing, incoming);
				serverPerClient.start();
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					// it's okay if we're interrupted.  someone's probably just closing the server.  
					//Logger.getGlobal().log(Level.INFO, "Interruption for server " + Server.this);
					break;
				}
			}
		} catch (IOException e) {
			// that's okay.  someone is probably closing this server.  
			//Logger.getGlobal().log(Level.INFO, "IOException for server " + Server.this);
		} finally {
			close();
		}
	}
	
	@Override
	public void finalize() {
		close();
	}
	
	@Override
	public String toString() {
		return String.format("Server on %s (port %d) for %s", address, port, thing);
	}
}
