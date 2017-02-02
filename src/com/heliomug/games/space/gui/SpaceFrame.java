package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
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
import javax.swing.JTabbedPane;

import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.server.CommandServer;
import com.heliomug.games.space.server.GameAddress;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.games.space.server.MasterServer;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame {
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
	

	public static void hostMyGame(String name, int port) {
		setServer(null);
		ownGame.setName(name);
		Server<Game> myServer = new Server<Game>(ownGame, port);
		myServer.start();
		setServer(myServer);
		GameAddress gameAddress = new GameAddress(server);
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
			JOptionPane.showMessageDialog(theFrame, message, "Whoops", JOptionPane.INFORMATION_MESSAGE);
		} else {
			try {
				Client<Game> newClient = address.getClientFor();
				newClient.start();
				setClient(newClient);
			} catch (IOException e) {
				System.err.println("could not connect to game");
				e.printStackTrace();
			}
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
	
	
	private SpaceFrame() {
		super("Networked Space Game");
		
		InetAddress masterAddress;
		try {
			masterAddress = InetAddress.getByName(new URL(MasterServer.MASTER_HOST).getHost());
			masterClient = new MasterClient(masterAddress, MasterServer.MASTER_PORT);
			masterClient.start();
		} catch (IOException e) {
			// that's okay, no master client then
			masterClient = null;
		}
		
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
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		tabbedPane.addTab("Game", new TabGame());
		tabbedPane.addTab("Local Players", new TabPlayers());
		tabbedPane.addTab("Game Options", new PanelOptions());
		tabbedPane.addTab("Internet Games", new TabConnections());
		panel.add(tabbedPane, BorderLayout.CENTER);

		this.add(panel);
		pack();
	}
}
