package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.heliomug.game.server.Client;
import com.heliomug.game.server.MessageDisplayer;
import com.heliomug.game.server.GameServer;
import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.gui.utils.UpdatingLabel;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame implements MessageDisplayer {

	private static SpaceFrame theFrame;
	
	public static SpaceFrame getFrame() {
		if (theFrame == null) {
			theFrame = new SpaceFrame();
		}
		return theFrame;
	}
	
	public static Client<SpaceGame, SpaceFrame> getClient() {
		return theFrame.client;
	}
	
	
	private GameServer<SpaceGame, SpaceFrame> server;
	private Client<SpaceGame, SpaceFrame> client;
	
	private Map<Player, ControlConfig> controlMap;
	private List<Player> localPlayers;
	
	private JTabbedPane tabbedPane; 
	
	private String message;
	
	private SpaceFrame() {
		super("Networked Space Game");
		
		server = new GameServer<>(this);
		client = new Client<>();
		controlMap = new HashMap<>();
		localPlayers = new ArrayList<>();
		
		message = "none";
		
		setupGUI();
	}
	
	private String getMessage() {
		return message;
	}
	
	private void setupGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		tabbedPane.addTab("Connections", new PanelConnection());
		tabbedPane.addTab("Game", new PanelGame());
		
		panel.add(tabbedPane, BorderLayout.CENTER);
		
		JLabel label = new UpdatingLabel("Messages", () -> getMessage());
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(label, BorderLayout.SOUTH);
		
		this.add(panel);
		pack();
	}
	
	public void setStatus(String message) {
		this.message = message;
	}
	
	public void setPane(int i) {
		if (i >= 0 || i < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(i);
		}
	}
	
	public GameServer<SpaceGame, SpaceFrame> getServer() {
		return server;
	}
	
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	public void addPlayer(Player player) {
		localPlayers.add(player);
		client.sendCommand(new CommandPlayer(player));
		controlMap.put(player, new ControlConfig(player));
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
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			SpaceFrame frame = new SpaceFrame();
			frame.setVisible(true);
		});
	}

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
		this.message = message;
		repaint();
	}
}
