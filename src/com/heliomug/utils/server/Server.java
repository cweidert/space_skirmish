package com.heliomug.utils.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

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
		System.out.println("-Started server \n\t" + this); 
	}
	
	public void close() {
		if (isActive) {
			System.out.println("-Stopping server \n\t" + this);
			if (serverThread != null) {
				serverThread.interrupt();
				serverThread = null;
			}
			if (serverSocket != null) {
				try {
					serverSocket.close();
					serverSocket = null;
				} catch (IOException e) {
					System.out.println("couldn't close server socket for \n\t" + this);
					e.printStackTrace();
				}
			}
		}
		isActive = false;
	}
	
	public void run() {
		try {
			try {
				serverSocket = new ServerSocket(port);
				while (!Thread.currentThread().isInterrupted()) {
					Socket incoming = serverSocket.accept();
					ServerPerClient<T> serverPerClient = new ServerPerClient<>(thing, incoming);
					serverPerClient.start();
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						System.out.println("Interruption for server " + Server.this);
						break;
					}
				}
			} finally {
				close();
			}
		} catch (IOException e) {
			System.out.println("IO Exception for server " + Server.this);
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
