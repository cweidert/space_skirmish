package com.heliomug.game.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public class ServerPerClient<T extends Serializable> {
	private static final int OUTGOING_SLEEP_TIME = 25;
	private static final int INCOMING_SLEEP_TIME = 10;
	
	private Thread sendingThread;
	private Thread receivingThread;
	
	private SocketChannel socketChannel;
	
	private T thing;

	private long startTime;
	private int thingsPushed;
	private int consumersPulled;
	
	private boolean isActive;
	
	public ServerPerClient(T thing, SocketChannel incoming) {
		this.sendingThread = null;
		this.receivingThread = null;
		this.thing = thing;
		this.socketChannel = incoming;
		this.thingsPushed = this.consumersPulled = 0;
		this.isActive = false;
	}
	
	public int getConsumersPulled() {
		return consumersPulled;
	}
	
	public double getThingsPushedPerSec() {
		double age = (System.currentTimeMillis() - startTime) / 1000.0;
		return thingsPushed / age;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void start() {
		sendingThread = new Thread(new Sender());
		sendingThread.setDaemon(true);
		sendingThread.start();
		receivingThread = new Thread(new Receiver());
		receivingThread.setDaemon(true);
		receivingThread.start();
		startTime = System.currentTimeMillis();
		isActive = true;
	}
	
	public void stop() {
		if (sendingThread != null && !sendingThread.isInterrupted()) {
			sendingThread.interrupt();
			sendingThread = null;
		}
		if (receivingThread != null && !receivingThread.isInterrupted()) {
			receivingThread.interrupt();
			receivingThread = null;
		}
		try {
			socketChannel.close();
		} catch (IOException e) {
			System.err.println("Couldn't close socket for Server Per Client " + ServerPerClient.this);
			e.printStackTrace();
		}
		isActive = false;
	}
	
	private class Receiver implements Runnable {
		public void run() {
			try {
				try (
					InputStream inStream = Channels.newInputStream(socketChannel);
					ObjectInputStream in = new ObjectInputStream(inStream);
				) {
					while (!Thread.currentThread().isInterrupted()) {
						@SuppressWarnings("unchecked")
						Consumer<T> con = (Consumer<T>) in.readObject();
						con.accept(thing);
						try {
							Thread.sleep(INCOMING_SLEEP_TIME);
						} catch (InterruptedException e) {
							System.out.println("Interruption for incoming receiver for " + ServerPerClient.this);
							break;
						}
					}
				} 
			} catch (IOException e) {
				System.err.println("IO Exception for incoming receiver for " + ServerPerClient.this);
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("Class Not Found Exception for incoming receiver for " + ServerPerClient.this);
				//e.printStackTrace();
			}

			stop();
		}
	}
	
	private class Sender implements Runnable {
		public void run() {
			try {
				try ( 
					OutputStream outStream = Channels.newOutputStream(socketChannel);
					ObjectOutputStream out = new ObjectOutputStream(outStream); 
				) {
					while (!Thread.currentThread().isInterrupted()) {
						out.reset();
						out.writeObject(thing);
						try {
							Thread.sleep(OUTGOING_SLEEP_TIME);
						} catch (InterruptedException e) {
							System.out.println("Interruption for outgoing sender for " + ServerPerClient.this);
							break;
						}
					}
				} 
			} catch (IOException e) {
				System.err.println("IO Exception for outgoing sender for " + ServerPerClient.this);
				//e.printStackTrace();
			}
			
			stop();
		}
	}
	
	public String toString() {
		return String.format("Server Per Client for %s on %s", thing, socketChannel);
	}
}
