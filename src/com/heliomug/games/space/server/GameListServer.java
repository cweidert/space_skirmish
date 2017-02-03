package com.heliomug.games.space.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.heliomug.utils.server.NetworkUtils;
import com.heliomug.utils.server.Server;

public class GameListServer extends Server<CopyOnWriteArrayList<GameAddress>> {
	public static final String MASTER_SERVER_ADDRESS = "http://home.heliomug.com";
	
	public static final int GAME_LIST_PORT = 27961;

	private GameListServer(CopyOnWriteArrayList<GameAddress> serverList) {
		super(serverList, GAME_LIST_PORT);
	}

	public static GameListClient getClient() {
		GameListClient gameListClient = null;
		InetAddress gameListAddress;

		try {
			Logger.getGlobal().log(Level.INFO, "Trying to reach global game list @ " + MASTER_SERVER_ADDRESS + ".  ");
			gameListClient = tryConnection(InetAddress.getByName(new URL(MASTER_SERVER_ADDRESS).getHost()));
		} catch (IOException e) {
			Logger.getGlobal().log(Level.INFO, "Could not reach global game list.  ");
			try {
				gameListAddress = NetworkUtils.getExternalAddress();
				Logger.getGlobal().log(Level.INFO, "Trying to reach local game list @ " + gameListAddress + ".  ");
				gameListClient = tryConnection(gameListAddress);
			} catch (IOException e1) {
				Logger.getGlobal().log(Level.INFO, "Could not reach local game list.  Starting my own game list server.");
				startServer();
				try {
					gameListAddress = NetworkUtils.getLanAddress();
					Logger.getGlobal().log(Level.INFO, "Trying to connect to my own game list @ " + gameListAddress + ".  ");
					gameListClient = tryConnection(gameListAddress);
				} catch (IOException e2) {
					Logger.getGlobal().log(Level.WARNING, "Could not reach my own game list!", e2);
				}
			}
		}
		Logger.getGlobal().log(Level.INFO, "Connected to game list " + gameListClient);
		return gameListClient;
	}
	
	private static void startServer() {
		CopyOnWriteArrayList<GameAddress> serverList = new CopyOnWriteArrayList<>();
		GameListServer masterHost = new GameListServer(serverList);
		masterHost.start();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			String message = "stall after starting game list server was interrupted somehow";
			Logger.getGlobal().log(Level.WARNING, message, e);
		}
	}
	
	private static GameListClient tryConnection(InetAddress serverAddress) throws IOException {
		GameListClient client = null;
		client = new GameListClient(serverAddress, GAME_LIST_PORT);
		client.start();
		return client;
	}
}
