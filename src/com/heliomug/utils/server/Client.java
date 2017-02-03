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
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public int getPort() {
		return socket.getPort();
	}
	
	public InetAddress getAddress() {
		return socket.getInetAddress();
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
		Logger.getGlobal().log(Level.INFO, "Started client " + this);
	}
	
	public void close() {
		if (isActive) {
			Logger.getGlobal().log(Level.INFO, "Stopping client " + this);
			if (clientThread != null) {
				clientThread.interrupt();
				clientThread = null;
			}
			try {
				if (out == null) {
					Logger.getGlobal().log(Level.WARNING, "out was null in " + this);
				} else {
					out.close();
				}
			} catch (IOException e) {
				Logger.getGlobal().log(Level.WARNING, "could not close out in " + this, e);
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
					Logger.getGlobal().log(Level.WARNING, "out is null");
				}
			} catch (IOException e) {
				Logger.getGlobal().log(Level.WARNING, "couldn't write command in " + this, e);
				e.printStackTrace();
			}
		} else {
			Logger.getGlobal().log(Level.WARNING, "client client is not active somehow");
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
							// that's okay.  someone is probably trying to close us.  
							//Logger.getGlobal().log(Level.INFO, "Interruption for client " + Client.this);
							break;
						}
					}
				}
			} catch (IOException e) {
				// that's okay.  someone probably closed the server on us
				//Logger.getGlobal().log(Level.INFO, "IOException for client " + Client.this);
			} catch (ClassNotFoundException e) {
				// that's weird
				//Logger.getGlobal().log(Level.WARNING, "Class not found exception for client " + Client.this, e);
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
