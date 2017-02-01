package com.heliomug.game.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class ThingClient<T extends Serializable> {
	private static final int THREAD_SLEEP_TIME = 1;
	
	private T servable;
	
	private ObjectOutputStream commandSender;

	private long timeStarted;
	private int thingsPulled, commandsSent;
	
	private InetAddress host;
	private int port;

	/*
	public ThingClient(ThingHost<T> host) {
		this(host.getAddress().getHostAddress(), host.getPort());
	}
	*/
	
	public ThingClient(InetAddress host, int port) {
		this.host = host;
		this.port = port;
		commandSender = null;
		servable = null;
		thingsPulled = commandsSent = 0;
	}

	public T getThing() {
		return servable;
	}
	
	public int getThingsPulled() {
		return thingsPulled;
	}
	
	public int getCommandsSent() {
		return commandsSent;
	}
	
	public double getThingsPulledPerSec() {
		double sec = (System.currentTimeMillis() - timeStarted) / 1000.0;
		return thingsPulled / sec;
	}
	
	public void sendCommand(Consumer<T> command) {
		if (commandSender == null) {
			System.err.println("no command sending connection!");
		} else {
			try {
				commandSender.writeObject(command);
				commandsSent++;
			} catch (IOException e) {
				System.err.println("could not send command for some reason");
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		start((Boolean b) -> {});
	}
	
	@SuppressWarnings("unchecked")
	public void start(Consumer<Boolean> callback) {
		if (servable == null) {
			Thread gameReceiver = new Thread(() -> {
				try (Socket socket = new Socket(host, port)) {
					try (
						InputStream inStream = socket.getInputStream();
						ObjectInputStream in = new ObjectInputStream(inStream);
						OutputStream outStream = socket.getOutputStream();
						) {
						timeStarted = System.currentTimeMillis();
						commandSender = new ObjectOutputStream(outStream);
						callback.accept(true);
						while (true) {
							try {
								servable = (T) in.readObject();
							} catch (ClassNotFoundException | IOException e1) {
								e1.printStackTrace();
								System.exit(1);
							}
							thingsPulled++;
							try {
								Thread.sleep(THREAD_SLEEP_TIME);
							} catch (InterruptedException e) {
								System.err.println("Game receiver sleep interrupted");
								e.printStackTrace();
							}
						}
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				} catch (UnknownHostException e3) {
					callback.accept(false);
					e3.printStackTrace();
				} catch (IOException e3) {
					callback.accept(false);
					e3.printStackTrace();
				}
			});
			gameReceiver.setDaemon(true);
			gameReceiver.start();
		}
	}

	public void kill() {
		
	}
}
