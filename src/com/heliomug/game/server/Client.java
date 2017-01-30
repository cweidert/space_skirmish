package com.heliomug.game.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class Client<S extends Serializable, D extends MessageDisplayer> {
	private static final int THREAD_SLEEP_TIME = 1;
	
	private S servable;
	
	private ObjectOutputStream commandSender;

	private long timeStarted;
	private int gamesPulled, commandsSent;
	
	public Client() {
		commandSender = null;
		servable = null;
		gamesPulled = commandsSent = 0;
	}

	public S getThing() {
		return servable;
	}
	
	public int getGamesPulled() {
		return gamesPulled;
	}
	
	public int getCommandsSent() {
		return commandsSent;
	}
	
	public double getGamesPulledPerSec() {
		double sec = (System.currentTimeMillis() - timeStarted) / 1000.0;
		return gamesPulled / sec;
	}
	
	public void sendCommand(Consumer<S> command) {
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
	
	@SuppressWarnings("unchecked")
	public void start(String host, int portNumber) {
		if (servable == null) {
			Thread gameReceiver = new Thread(() -> {
				try (Socket socket = new Socket(host, portNumber)) {
					try (
						InputStream inStream = socket.getInputStream();
						ObjectInputStream in = new ObjectInputStream(inStream);
						OutputStream outStream = socket.getOutputStream();
						) {
						timeStarted = System.currentTimeMillis();
						commandSender = new ObjectOutputStream(outStream);
						while (true) {
							try {
								servable = (S) in.readObject();
							} catch (ClassNotFoundException | IOException e1) {
								e1.printStackTrace();
								System.exit(1);
							}
							gamesPulled++;
							try {
								Thread.sleep(THREAD_SLEEP_TIME);
							} catch (InterruptedException e) {
								System.err.println("Game receiver sleep interrupted");
								e.printStackTrace();
							}
						}
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				} catch (UnknownHostException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			});
			gameReceiver.setDaemon(true);
			gameReceiver.start();
		}
	}

}
