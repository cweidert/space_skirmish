package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.ControlConfig;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.server.CommandServer;
import com.heliomug.games.space.server.GameAddress;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.NetworkUtils;
import com.heliomug.utils.server.Server;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame {
	public static final String MASTER_HOST_HOME = "http://home.heliomug.com";
	public static final int MASTER_PORT = 27961;
	public static final int GAME_PORT = 27960;
	
	public static final int SERVER_DELAY = 250; 
	
	private static SpaceFrame theFrame;
	
	private static MasterClient masterClient;
	
	private static Server<Game> server;
	private static Client<Game> client;
	
	private static Map<Player, ControlConfig> controlAssignments;
	private static List<Player> localPlayers;
	
	private static Game ownGame;
	
	public static SpaceFrame getFrame() {
		if (theFrame == null) {
			theFrame = new SpaceFrame();
		}
		return theFrame;
	}

	
	public static Game getGame() {
		if (ownGame != null) {
			return ownGame;
		} else if (client != null) {
			return client.getThing();
		}
		System.out.println("and game is " + getGame());
		return null;
	}
	
	public static boolean hasOwnGame() {
		return ownGame != null;
	}
	

	public static boolean isServing() {
		return server != null;
	}
	
	public static void hostMyGame(String name, int port) {
		setServer(null);
		Server<Game> myServer = new Server<Game>(ownGame, port);
		myServer.start();
		setServer(myServer);
		GameAddress gameAddress = new GameAddress(server, name);
		if (masterClient != null) {
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(SERVER_DELAY);
					masterClient.sendCommand(new CommandServer(gameAddress));
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
		if (address.isLocal() && server != null && address.getPort() == server.getPort()) {
			String message = "That's your local game, dude.  You're hosting on that bad boy.";
			JOptionPane.showMessageDialog(theFrame, message, "Whoops", JOptionPane.WARNING_MESSAGE);
		} else {
			Thread t = new Thread(() -> {
				try {
					Client<Game> newClient = address.getClientFor();
					newClient.start();
					setClient(newClient);
				} catch (IOException e) {
					String message = "Couldn't connect to that game.";
					JOptionPane.showMessageDialog(theFrame, message, "Whoops", JOptionPane.WARNING_MESSAGE);
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
		return masterClient != null;
	}
	
	public static List<GameAddress> getGameAddressList() {
		if (isConnectedToMasterHost()) {
			return masterClient.getThing();
		} else {
			return new ArrayList<>();
		}
	}
	
	
	public static void handleKey(int key, boolean down) {
		for (Player player : localPlayers) {
			ShipSignal signal = controlAssignments.get(player).getSignal(key, down);
			if (signal != null) {
				sendCommand(new CommandShip(player, signal));
			}
		}
	}
	
	public static void sendCommand(Consumer<Game> com) {
		if (client == null) {
			com.accept(ownGame);
		} else {
			client.sendCommand(com);
		}
	}
	
	
	public static void addLocalPlayer(Player player) {
		localPlayers.add(player);
		controlAssignments.put(player, new ControlConfig(player));
		sendCommand(new CommandPlayer(player));
	}

	public static void removeLocalPlayer(Player player) {
		localPlayers.remove(player);
		controlAssignments.remove(player);
		sendCommand(new CommandPlayer(player, false));
	}

	public static List<Player> getAllPlayers() {
		return getGame().getPlayers();
	}
	
	public static List<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	public static List<Player> getExternalPlayers() {
		if (client == null) {
			return new ArrayList<>();
		} else {
			List<Player> all = getAllPlayers();
			all.removeAll(localPlayers);
			return all;
		}
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
	
	
	private static void setServer(Server<Game> server) {
		if (SpaceFrame.server != null && masterClient != null) {
			GameAddress gameAddress = new GameAddress(SpaceFrame.server);
			masterClient.sendCommand(new CommandServer(gameAddress, false));
		}
		if (SpaceFrame.server != null) {
			SpaceFrame.server.close();
		}
		SpaceFrame.server = server;
	}
	
	private static void setClient(Client<Game> newClient) {
		try {
			Thread.sleep(SERVER_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (client != null) {
			client.close();
		}
		client = newClient;

		List<Player> playersToAdd = getLocalPlayers();
		playersToAdd.removeAll(client.getThing().getPlayers());
		for (Player player : playersToAdd) {
			client.sendCommand(new CommandPlayer(player));
		}
		if (client == null) {
			ownGame = new Game();
		} else {
			ownGame = null;
		}
	}
	
	
	public static final String GAME_CARD = "game card";
	public static final String PLAYER_CARD = "player card";
	public static final String SETTINGS_CARD = "settings card";
	public static final String CONNECTIONS_CARD = "connections card";
	
	private JPanel cardPanel;
	private JPanel gameCard;
	
	private SpaceFrame() {
		super("Networked Space Game");
		
		Thread t = new Thread(() -> {
			InetAddress masterAddress;
			try {
				masterAddress = InetAddress.getByName(new URL(MASTER_HOST_HOME).getHost());
				masterClient = new MasterClient(masterAddress, MASTER_PORT);
				masterClient.start();
			} catch (IOException e) {
				try {
					masterAddress = InetAddress.getByName(NetworkUtils.getExternalAddress().getHostAddress());
					masterClient = new MasterClient(masterAddress, MASTER_PORT);
					masterClient.start();
				} catch (IOException e1) {
					masterClient = null;
				}
			}
		});
		t.start();
		
		server = null;
		client = null;
		controlAssignments = new HashMap<>();
		localPlayers = new ArrayList<>();
		ownGame = new Game();
		
		setupGUI();
	}
	
	private void setupGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(new PanelWins(), BorderLayout.NORTH);
		
		cardPanel = new JPanel(new CardLayout());
		cardPanel.setFocusable(false);
		gameCard = new CardGame(); 
		cardPanel.add(gameCard, GAME_CARD);
		cardPanel.add(new CardPlayers(), PLAYER_CARD);
		cardPanel.add(new CardSettings(), SETTINGS_CARD);
		cardPanel.add(new CardConnections(), CONNECTIONS_CARD);
		panel.add(cardPanel, BorderLayout.CENTER);

		this.add(panel);
		pack();
	}
	
	public static void setCard(String cardName) {
		getFrame().setVisibleCard(cardName);
	}
	
	private void setVisibleCard(String cardName) {
		CardLayout layout = (CardLayout) cardPanel.getLayout();
		layout.show(cardPanel, cardName);
		if (cardName.equals(GAME_CARD)) {
			gameCard.requestFocusInWindow();
		}
	}
}