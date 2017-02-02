package com.heliomug.utils.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class Client<T extends Serializable> {
	private static final int INCOMING_SLEEP_TIME = 20;

	private Socket socket;
	
	private ObjectOutputStream out;
	
	private Thread clientThread;
	
	private boolean isActive;
	
	private T thing;
	
	public Client(InetAddress address, int port) throws IOException {
		this(new Socket(address, port));
	}
		
	public Client(Socket socket) {
		this.socket = socket;
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
		isActive = true;
		System.out.println("-Started client \n\t" + this);
	}
	
	public void close() {
		if (isActive) {
			System.out.println("-Stopping client \n\t" + this);
			if (clientThread != null) {
				clientThread.interrupt();
				clientThread = null;
			}
			try {
				if (out == null) {
					System.out.println("out was null in \n\t" + this);
				} else {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isActive = false;
	}
	
	public void sendCommand(Consumer<T> command) {
		if (isActive) {
			try {
				if (out != null) { 
					out.writeObject(command);
				} else {
					System.out.println("out is null");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("client not active");
		}
	}
	
	private class ClientRunner implements Runnable {
		@SuppressWarnings("unchecked")
		public void run() {
			try {
				try (
						InputStream inStream = socket.getInputStream();
						ObjectInputStream in = new ObjectInputStream(inStream);
						OutputStream outStream = socket.getOutputStream();
				) {
					out = new ObjectOutputStream(outStream);
					while (!Thread.currentThread().isInterrupted()) {
						thing = (T) in.readObject();
						try {
							Thread.sleep(INCOMING_SLEEP_TIME);
						} catch (InterruptedException e) {
							System.out.println("Interruption for client \n\t" + Client.this);
							break;
						}
					}
				}
			} catch (IOException e) {
				System.out.println("IOException for client \n\t" + Client.this);
			} catch (ClassNotFoundException e) {
				System.out.println("Class not found exception for client \n\t" + Client.this);
			} finally {
				close();
			}
		}
	}
	
	@Override
	public void finalize() {
		close();
	}
	
	@Override
	public String toString() {
		return String.format("Client on %s for %s", socket, thing);
	}
}
