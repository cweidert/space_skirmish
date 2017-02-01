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
import com.heliomug.utils.gui.MessageDisplayer;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame implements MessageDisplayer {
	private static SpaceFrame theFrame;
	
	public static SpaceFrame getFrame() {
		if (theFrame == null) {
			theFrame = new SpaceFrame();
		}
		return theFrame;
	}

	public static MasterClient getMasterClient() {
		return getFrame().masterClient;
	}
	
	public static Client<Game> getClient() {
		return getFrame().client;
	}

	public static void setClient(Client<Game> client) {
		getFrame().client = client;
	}
	
	public static Server<Game> makeMyOwnServer(String name, int port) {
		name = name.length() == 0 ? "[no name]" : name;
		Server<Game> myServer = new Server<Game>(new Game(name), port); 
		SpaceFrame.setServer(myServer);
		myServer.start();
		return myServer;
	}
	
	public static Server<Game> getServer() {
		return getFrame().server;
	}
	
	public static void setServer(Server<Game> server) {
		getFrame().server = server;
	}
	
	public static void addLocalPlayer(Player player) {
		if (getClient() != null) {
			getFrame().localPlayers.add(player);
			getFrame().controlAssignments.put(player, new ControlConfig(player));
			getClient().sendCommand(new CommandPlayer(player));
		}
	}

	public static void removeLocalPlayer(Player player) {
		if (getClient() != null) {
			getFrame().localPlayers.remove(player);
			getFrame().controlAssignments.remove(player);
			getClient().sendCommand(new CommandPlayer(player, false));
		}
	}
	
	public static List<Player> getLocalPlayers() {
		return getFrame().localPlayers;
	}
	
	public static ControlConfig getControlConfig(Player player) {
		return getFrame().controlAssignments.get(player);
	}
	
	public static Game getClientGame() {
		if (getFrame().client != null) {
			return getFrame().client.getThing();
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
	
	@Override
	public void accept(String message) {
		//this.messageLabel.setText(message);
	}
}
