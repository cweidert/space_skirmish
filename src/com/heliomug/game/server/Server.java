package com.heliomug.game.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server<T extends Serializable> implements Runnable {
	private static final int SLEEP_TIME = 5;
	
	private Thread serverThread;
	
	private ServerSocketChannel serverSocket;
	
	private T thing;
	private int port;
	
	public Server(T thing, int port) {
		this.thing = thing;
		this.port = port;
	}
	
	public void start() {
		serverThread = new Thread(this);
		serverThread.setDaemon(true);
		serverThread.start();
	}
	
	public void stop() {
		if (serverThread != null) {
			serverThread.interrupt();
			serverThread = null;
		}
	}
	
	public void run() {
		try {
			try {
				serverSocket = ServerSocketChannel.open();
				serverSocket.socket().bind(new InetSocketAddress(port));
				while (!Thread.currentThread().isInterrupted()) {
					SocketChannel incoming = serverSocket.accept();
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
				serverSocket.close();
			}
		} catch (IOException e) {
			System.err.println("IO Exception for server " + Server.this);
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return String.format("Server Per Client on %s for %s", serverSocket, thing);
	}
}
