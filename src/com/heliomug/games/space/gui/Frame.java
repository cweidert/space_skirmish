package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.games.space.server.MasterHost;
import com.heliomug.utils.gui.MessageDisplayer;

@SuppressWarnings("serial")
public class Frame extends JFrame implements MessageDisplayer {
	private static Frame theFrame;
	
	public static Frame getFrame() {
		if (theFrame == null) {
			theFrame = new Frame();
		}
		return theFrame;
	}

	public static MasterClient getMasterClient() {
		return getFrame().masterClient;
	}
	
	public static ThingClient<Game> getClient() {
		return getFrame().client;
	}

	public static void setClient(ThingClient<Game> client) {
		getFrame().client = client;
	}
	
	public static ThingHost<Game> getServer() {
		return getFrame().server;
	}
	
	public static void setServer(ThingHost<Game> server) {
		getFrame().server = server;
	}
	
	public static void addLocalPlayer(Player player) {
		if (getClient() != null) {
			getFrame().localPlayers.add(player);
			getFrame().controlAssignments.put(player, new ControlConfig(player));
			getClient().sendCommand(new CommandPlayer(player));
		}
	}
	
	public static List<Player> getLocalPlayers() {
		return getFrame().localPlayers;
	}
	
	public static ControlConfig getControlConfig(Player player) {
		return getFrame().controlAssignments.get(player);
	}
	
	private MasterClient masterClient;
	
	private ThingHost<Game> server;
	private ThingClient<Game> client;
	
	private Map<Player, ControlConfig> controlAssignments;
	private List<Player> localPlayers;
	
	private Frame() {
		super("Networked Space Game");
		
		masterClient = new MasterClient(MasterHost.MASTER_HOST, MasterHost.MASTER_PORT);
		masterClient.start((Boolean b) -> {
			if (!b) {
				masterClient = null;
			}
		});
		
		server = null;
		client = null;
		controlAssignments = new HashMap<>();
		localPlayers = new ArrayList<>();
		
		setupGUI();
	}
	
	private void setupGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		tabbedPane.addTab("Game", new TabGame());
		tabbedPane.addTab("Local Players", new TabLocalPlayers());
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
				Frame.getClient().sendCommand(new CommandShip(player, signal));
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
