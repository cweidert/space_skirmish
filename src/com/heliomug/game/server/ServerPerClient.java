package com.heliomug.game.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerPerClient<S extends Serializable> {
	private static final int THREAD_SLEEP_TIME = 1;
	private static final int GAME_SEND_SLEEP_TIME = 42;

	private long timeStarted;
	private int gamesSent, commandsPulled;
	
	private Socket incoming;
	
	private S servable;
	
	public ServerPerClient(Socket incoming, S thing) {
		this.incoming = incoming;
		gamesSent = commandsPulled = 0;
		this.servable = thing;
	}

	public int getGamesSent() {
		return gamesSent;
	}
	
	public int getCommandsPulled() {
		return commandsPulled;
	}
	
	public double getGamesServedPerSec() {
		double time = System.currentTimeMillis() - timeStarted;
		return gamesSent * 1000 / time;
	}
	
	@SuppressWarnings("unchecked")
	public void start() {
		timeStarted = System.currentTimeMillis();
		Thread gameSender = new Thread(() -> {
			try (
					OutputStream outStream = incoming.getOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(outStream);
					) {
				while (true) {
				    out.reset(); // jeez, don't forget this
					out.writeObject(servable);
					gamesSent++;
					try {
						Thread.sleep(GAME_SEND_SLEEP_TIME);
					} catch (InterruptedException e) {
						System.err.println("game sender sleep interrupted!");
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		gameSender.setDaemon(true);
		gameSender.start();
		
		Thread commandReceiver = new Thread(() -> {
			try (
					InputStream inStream = incoming.getInputStream();
					ObjectInputStream in = new ObjectInputStream(inStream);
					) {
				while (true) {
					try {
						Consumer<S>command = (Consumer<S>) in.readObject();
						command.accept(servable);
						commandsPulled++;
						Thread.sleep(THREAD_SLEEP_TIME);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						System.err.println("incoming command sleep interrupted");
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		commandReceiver.setDaemon(true);
		commandReceiver.start();
	}
}
