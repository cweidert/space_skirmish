package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.games.space.server.MasterHost;
import com.heliomug.utils.gui.MessageDisplayer;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame implements MessageDisplayer {
	private static SpaceFrame theFrame;
	
	public static SpaceFrame getFrame() {
		if (theFrame == null) {
			theFrame = new SpaceFrame();
		}
		return theFrame;
	}

	public static ThingClient<ArrayList<ThingHost<SpaceGame>>> getMasterClient() {
		return getFrame().masterClient;
	}
	
	public static ThingClient<SpaceGame> getClient() {
		return getFrame().client;
	}

	public static void setClient(ThingClient<SpaceGame> client) {
		getFrame().client = client;
	}
	
	public static ThingHost<SpaceGame> getServer() {
		return getFrame().server;
	}
	
	public static void setServer(ThingHost<SpaceGame> server) {
		getFrame().server = server;
	}
	
	public static void addLocalPlayer(Player player) {
		if (getClient() != null) {
			getFrame().localPlayers.add(player);
			getFrame().controlMap.put(player, new ControlConfig(player));
			getClient().sendCommand(new CommandPlayer(player));
		}
	}
	
	public static List<Player> getLocalPlayers() {
		return getFrame().localPlayers;
	}
	
	private ThingClient<ArrayList<ThingHost<SpaceGame>>> masterClient;
	
	private ThingHost<SpaceGame> server;
	private ThingClient<SpaceGame> client;
	
	private Map<Player, ControlConfig> controlMap;
	private List<Player> localPlayers;
	
	private JLabel messageLabel;
	
	private SpaceFrame() {
		super("Networked Space Game");
		
		masterClient = new ThingClient<>(MasterHost.MASTER_HOST, MasterHost.MASTER_PORT);
		masterClient.start((Boolean b) -> {
			if (!b) {
				masterClient = null;
			}
		});
		
		server = null;
		client = null;
		controlMap = new HashMap<>();
		localPlayers = new ArrayList<>();
		
		setupGUI();
	}
	
	private void setupGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		
		tabbedPane.addTab("Connections", new TabConnections());
		tabbedPane.addTab("Players", new TabLocalPlayers());
		tabbedPane.addTab("Game", new TabGame());
		tabbedPane.addTab("Options", new TabOptions());
		panel.add(tabbedPane, BorderLayout.CENTER);

		messageLabel = new JLabel("Messages");
		panel.add(messageLabel, BorderLayout.SOUTH);
		
		this.add(panel);
		pack();
	}
	
	public void handleKey(int key, boolean down) {
		for (Player player : localPlayers) {
			ShipSignal signal = controlMap.get(player).getSignal(key, down);
			if (signal != null) {
				SpaceFrame.getClient().sendCommand(new CommandShip(player, signal));
			}
		}
	}
	
	public ControlConfig getControls(Player player) {
		return controlMap.get(player);
	}
	
	public void update() {
		repaint();
	}
	
	@Override
	public void accept(String message) {
		this.messageLabel.setText(message);
	}
}
