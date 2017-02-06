package com.heliomug.games.space.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import com.heliomug.games.space.ControlConfig;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.VehicleSignal;
import com.heliomug.games.space.server.CommandAddRemoveGame;
import com.heliomug.games.space.server.CommandAddRemovePlayer;
import com.heliomug.games.space.server.CommandShipSignal;
import com.heliomug.games.space.server.GameAddress;
import com.heliomug.games.space.server.GameListClient;
import com.heliomug.games.space.server.GameListServer;
import com.heliomug.utils.server.Address;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

class Session {
	public static final int GAME_PORT = 27960;
	
	public static final int SERVER_DELAY = 250; 
	
	private static Session theSpace;
	
	private static GameListClient gameListClient;
	
	private static Server<Game> gameServer;
	private static Client<Game> gameClient;
	
	private static Map<Player, ControlConfig> controlAssignments;
	private static List<Player> localPlayers;
	
	private static Game ownGame;
	
	public static Session getSpace() {
		if (theSpace == null) {
			theSpace = new Session();
		}
		return theSpace;
	}

	
	public static Game getGame() {
		if (gameClient != null) {
			if (gameClient.isActive()) {
				return gameClient.getThing();
			} else {
				setClient(null);
			}
		}
		return ownGame;
	}
	
	public static boolean hasOwnGame() {
		return ownGame != null;
	}
	

	public static boolean isServing() {
		return gameServer != null;
	}
	
	public static boolean isClient() {
		return gameClient != null;
	}
	
	public static GameAddress getClientAddress() {
		return new GameAddress(gameClient);
	}
	
	public static void hostMyGame(String name, int port) {
		setServer(null);
		Server<Game> myServer = new Server<Game>(ownGame, port);
		myServer.start();
		setServer(myServer);
		GameAddress gameAddress = new GameAddress(gameServer, name);
		if (gameListClient != null) {
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(SERVER_DELAY);
					gameListClient.sendCommand(new CommandAddRemoveGame(gameAddress));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			t.start();
		}
	}
	
	public static void deleteHostedGame() {
		setServer(null);
	}
	
	public static void joinGame(GameAddress address) {
		if (address.isLocal() && gameServer != null && address.getPort() == gameServer.getPort()) {
			String message = "That's your local game, dude.  You're hosting on that bad boy.";
			JOptionPane.showMessageDialog(Frame.getFrame(), message, "Whoops", JOptionPane.WARNING_MESSAGE);
		} else {
			Thread t = new Thread(() -> {
				try {
					Client<Game> newClient = address.getClientFor();
					newClient.start();
					setClient(newClient);
				} catch (IOException e) {
					String message = "Couldn't connect to that game.";
					JOptionPane.showMessageDialog(Frame.getFrame(), message, "Whoops", JOptionPane.WARNING_MESSAGE);
				}
			});
			t.start();
		}
	}
	
	public static void leaveGame() {
		setClient(null);
		if (ownGame == null) {
			ownGame = new Game();
		}
	}

	
	public static boolean isConnectedToMasterHost() {
		return gameListClient != null;
	}
	
	public static List<GameAddress> getGameAddressList() {
		if (isConnectedToMasterHost()) {
			return gameListClient.getThing();
		} else {
			return new ArrayList<>();
		}
	}
	
	
	public static void handleKey(int key, boolean down) {
		for (Player player : localPlayers) {
			VehicleSignal signal = controlAssignments.get(player).getSignal(key, down);
			if (signal != null) {
				sendCommand(new CommandShipSignal(player, signal));
			}
		}
	}
	
	public static void sendCommand(Consumer<Game> com) {
		if (gameClient == null) {
			com.accept(ownGame);
		} else {
			gameClient.sendCommand(com);
		}
	}
	
	public static void addLocalPlayers(int n) {
		while (localPlayers.size() < n) {
			int i = localPlayers.size() + 1; 
			addLocalPlayer(new Player(i, ButtonColor.getOkayColor()));
		}
	}
	
	public static void addLocalPlayer(Player player) {
		localPlayers.add(player);
		controlAssignments.put(player, new ControlConfig(player));
		sendCommand(new CommandAddRemovePlayer(player));
	}

	public static void removeLocalPlayer(Player player) {
		localPlayers.remove(player);
		controlAssignments.remove(player);
		sendCommand(new CommandAddRemovePlayer(player, false));
	}

	public static void removePlayersWithAddress(Address address) {
		getGame().removePlayersWithAddress(address);
	}
	
	public static List<Player> getAllPlayers() {
		return getGame().getPlayers();
	}
	
	public static List<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	public static List<Player> getExternalPlayers() {
		List<Player> all = new ArrayList<>();
		all.addAll(getAllPlayers());
		all.removeAll(localPlayers);
		return all;
	}
	
	public static List<Color> getPlayerColors() {
		List<Color> colors = new ArrayList<>();
		for (Player player : getAllPlayers()) {
			colors.add(player.getColor());
		}
		return colors;
	}
	
	public static ControlConfig getControlConfig(Player player) {
		return controlAssignments.get(player);
	}
	
	
	public static void quit() {
		if (gameServer != null) {
			gameServer.close();
		}
		if (gameClient != null) {
			gameClient.close();
		}
		System.exit(0);
	}
	
	private static void setServer(Server<Game> server) {
		if (Session.gameServer != null && gameListClient != null) {
			GameAddress gameAddress = new GameAddress(Session.gameServer);
			gameListClient.sendCommand(new CommandAddRemoveGame(gameAddress, false));
		}
		if (Session.gameServer != null) {
			Session.gameServer.close();
		}
		Session.gameServer = server;
	}
	
	private static void setClient(Client<Game> newClient) {
		try {
			Thread.sleep(SERVER_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (gameClient != null) {
			gameClient.close();
		}
		gameClient = newClient;

		if (gameClient == null) {
			ownGame = new Game();
		} else {
			ownGame = null;
		}
		addAllPlayersToGame();
	}
	
	private static void addAllPlayersToGame() {
		List<Player> playersToAdd = getLocalPlayers();
		if (ownGame == null) {
			for (Player player : playersToAdd) {
				gameClient.sendCommand(new CommandAddRemovePlayer(player));
			}
		} else {
			for (Player player : playersToAdd) {
				new CommandAddRemovePlayer(player).accept(ownGame);
			}
		}
	}
	
	
	private Session() {
		Thread t = new Thread(() -> {
			gameListClient = GameListServer.getClient();
		});
		t.start();
		
		gameServer = null;
		gameClient = null;
		controlAssignments = new HashMap<>();
		localPlayers = new ArrayList<>();
		ownGame = new Game();
	}
}