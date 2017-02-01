package com.heliomug.game.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public class Client<T extends Serializable> {
	private static final int INCOMING_SLEEP_TIME = 20;

	private SocketChannel socketChannel;
	
	private ObjectOutputStream out;
	
	private Thread clientThread;
	
	private boolean isActive;
	
	private T thing;
	
	public Client(InetAddress address, int port) throws IOException {
		this(SocketChannel.open(new InetSocketAddress(address, port)));
	}
		
	public Client(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		this.isActive = false;
		this.out = null;
		this.thing = null;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public T getThing() {
		return thing;
	}
	
	public void start() {
		clientThread = new Thread(new ClientRunner());
		clientThread.setDaemon(true);
		clientThread.start();
	}
	
	public void stop() {
		if (clientThread != null) {
			clientThread.interrupt();
			clientThread = null;
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendCommand(Consumer<T> command) {
		if (isActive) {
			try {
				out.writeObject(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.err.println("client not active");
		}
	}
	
	private class ClientRunner implements Runnable {
		@SuppressWarnings("unchecked")
		public void run() {
			try {
				try (
						OutputStream outStream = Channels.newOutputStream(socketChannel);
						InputStream inStream = Channels.newInputStream(socketChannel);
						ObjectInputStream in = new ObjectInputStream(inStream);
				) {
					out = new ObjectOutputStream(outStream);
					while (!Thread.currentThread().isInterrupted()) {
						thing = (T) in.readObject();
						try {
							Thread.sleep(INCOMING_SLEEP_TIME);
						} catch (InterruptedException e) {
							System.out.println("Interruption for client " + Client.this);
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stop();
		}
	}
	
	public String toString() {
		return String.format("Client for %s on %s", thing, socketChannel);
	}
}
