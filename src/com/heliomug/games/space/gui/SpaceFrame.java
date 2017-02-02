package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.games.space.server.MasterServer;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame {
	private static SpaceFrame theFrame;
	
	public static SpaceFrame getFrame() {
		if (theFrame == null) {
			theFrame = new SpaceFrame();
		}
		return theFrame;
	}

	public static MasterClient getMasterClient() {
		return theFrame.masterClient;
	}
	
	public static Client<Game> getClient() {
		return theFrame.client;
	}

	public static void setClient(Client<Game> client) {
		if (theFrame.client != null) {
			theFrame.client.close();
		}
		theFrame.client = client;
		for (Player player : getLocalPlayers()) {
			client.sendCommand(new CommandPlayer(player));
		}
	}
	
	public static Server<Game> makeAndSetServer(String name, int port) {
		name = name.length() == 0 ? "[no name]" : name;
		Server<Game> myServer = new Server<Game>(new Game(name), port); 
		SpaceFrame.setServer(myServer);
		myServer.start();
		return myServer;
	}
	
	public static Server<Game> getServer() {
		return theFrame.server;
	}
	
	public static void setServer(Server<Game> server) {
		if (theFrame.server != null) {
			theFrame.server.close();
		}
		theFrame.server = server;
	}
	
	public static void addLocalPlayer(Player player) {
		if (theFrame.client != null) {
			theFrame.localPlayers.add(player);
			theFrame.controlAssignments.put(player, new ControlConfig(player));
			theFrame.client.sendCommand(new CommandPlayer(player));
		}
	}

	public static void removeLocalPlayer(Player player) {
		if (theFrame.client != null) {
			theFrame.localPlayers.remove(player);
			theFrame.controlAssignments.remove(player);
			theFrame.client.sendCommand(new CommandPlayer(player, false));
		}
	}
	
	public static List<Player> getLocalPlayers() {
		return theFrame.localPlayers;
	}
	
	public static ControlConfig getControlConfig(Player player) {
		return theFrame.controlAssignments.get(player);
	}
	
	public static Game getClientGame() {
		if (theFrame.client != null) {
			return theFrame.client.getThing();
		}
		return null;
	}
	
	
	
	private MasterClient masterClient;
	
	private Server<Game> server;
	private Client<Game> client;
	
	private Map<Player, ControlConfig> controlAssignments;
	private List<Player> localPlayers;
	
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
	
	public void handleKey(int key, boolean down) {
		for (Player player : localPlayers) {
			ShipSignal signal = controlAssignments.get(player).getSignal(key, down);
			if (signal != null) {
				SpaceFrame.getClient().sendCommand(new CommandShip(player, signal));
			}
		}
	}
	
	public ControlConfig getControls(Player player) {
		return controlAssignments.get(player);
	}
	
	public void update() {
		repaint();
	}
}
