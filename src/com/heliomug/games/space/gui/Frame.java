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
import com.heliomug.game.server.ServerMaster;
import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.gui.utils.UpdatingLabel;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private static Frame theFrame;
	
	public static Frame getFrame() {
		if (theFrame == null) {
			theFrame = new Frame();
		}
		return theFrame;
	}
	

	private ServerMaster<SpaceGame> server;
	private Client<SpaceGame> client;
	
	private Map<Player, ControlConfig> controlMap;
	private List<Player> localPlayers;
	
	private JTabbedPane tabbedPane; 
	
	private String message;
	
	private Frame() {
		super("Networked Space Game");
		
		server = new ServerMaster<>();
		client = new Client<>();
		controlMap = new HashMap<>();
		localPlayers = new ArrayList<>();
		
		message = " ";
		
		setupGUI();
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
		
		JLabel label = new UpdatingLabel(":", () -> message);
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
	
	public Client<SpaceGame> getClient() {
		return client;
	}
	
	public ServerMaster<SpaceGame> getServer() {
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
			controlMap.get(player).handleKey(key, down);
		}
	}
	
	public ControlConfig getControls(Player player) {
		return controlMap.get(player);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Frame frame = new Frame();
			frame.setVisible(true);
		});
	}
}
