package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.CommandPlayer;
import com.heliomug.games.space.CommandShip;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.games.space.server.CommandAddHost;
import com.heliomug.games.space.server.MasterHost;
import com.heliomug.gui.utils.MessageDisplayer;

@SuppressWarnings("serial")
public class SpaceFrame extends JFrame implements MessageDisplayer {
	private static SpaceFrame theFrame;
	
	public static SpaceFrame getFrame() {
		if (theFrame == null) {
			theFrame = new SpaceFrame();
		}
		return theFrame;
	}
	
	public static ThingClient<SpaceGame> getClient() {
		return getFrame().client;
	}

	public static ThingHost<SpaceGame> getServer() {
		return getFrame().server;
	}
	
	private ThingClient<ArrayList<ThingHost<SpaceGame>>> masterClient;
	private ThingHost<SpaceGame> server;
	private ThingClient<SpaceGame> client;
	
	private Map<Player, ControlConfig> controlMap;
	private List<Player> localPlayers;
	
	private JLabel messageLabel;
	private HostPanel hostPanel;
	private HostListPanel hostListPanel;
	private PlayerListPanel playerListPanel;
	
	private SpaceFrame() {
		super("Networked Space Game");
		
		masterClient = new ThingClient<>(MasterHost.MASTER_HOST, MasterHost.MASTER_PORT);
		masterClient.start();
		
		server = null;
		client = null;
		controlMap = new HashMap<>();
		localPlayers = new ArrayList<>();
		
		Timer t = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		t.start();
		
		setupGUI();
	}
	
	private void setupGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		JPanel connectionPanel = new JPanel(new BorderLayout());
		hostPanel = new HostPanel();
		connectionPanel.add(hostPanel, BorderLayout.EAST);
		playerListPanel = new PlayerListPanel();
		connectionPanel.add(playerListPanel, BorderLayout.CENTER);
		hostListPanel = new HostListPanel();
		connectionPanel.add(hostListPanel, BorderLayout.WEST);
		tabbedPane.addTab("Connections", connectionPanel);
		tabbedPane.addTab("Game", new PanelGame());
		panel.add(tabbedPane, BorderLayout.CENTER);
		
		messageLabel = new JLabel("Messages");
		panel.add(messageLabel, BorderLayout.SOUTH);
		
		this.add(panel);
		pack();
	}
	
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	public void addPlayer(Player player) {
		if (client != null) {
			localPlayers.add(player);
			controlMap.put(player, new ControlConfig(player));
			client.sendCommand(new CommandPlayer(player));
		}
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
		if (masterClient == null) {
			System.out.println("null client");
		} else {
			hostPanel.update();
			hostListPanel.update();
			playerListPanel.update();
			repaint();
		}
	}
	
	@Override
	public void accept(String message) {
		this.messageLabel.setText(message);
	}

	private class HostPanel extends JPanel {
		JButton startGameButton;
		
		public HostPanel() {
			super(new BorderLayout());
			startGameButton = new JButton("Start Game!");
			startGameButton.addActionListener((ActionEvent e) -> {
				if (server != null) {
					server.getThing().start();
				}
			});
			add(startGameButton, BorderLayout.SOUTH);
		}
		
		public void update() {
			startGameButton.setEnabled(server != null);
		}
	}
	
	private class PlayerListPanel extends JPanel {
		JButton addPlayerButton;
		JPanel playerList;
		
		public PlayerListPanel() {
			super(new BorderLayout());
			playerList = new JPanel(new GridLayout(0, 1));
			playerList.add(new JLabel("apobine"));
			add(playerList, BorderLayout.CENTER);
			
			addPlayerButton = new JButton("Add Player");
			addPlayerButton.addActionListener((ActionEvent e) -> {
				if (client != null) {
					addPlayer(new Player("Joe"));
				}
			});
			addPlayerButton.setEnabled(false);
			add(addPlayerButton, BorderLayout.SOUTH);
		}
		
		public void update() {
			addPlayerButton.setEnabled(client != null);
			
	        playerList.removeAll();
			if (localPlayers != null & localPlayers.size() > 0) {
				for (Player player : localPlayers) {
			        playerList.add(new JLabel(player.toString()));
				}
			} else {
				playerList.add(new JLabel("no players yet!"));
			}

		}
	}
	
	private class HostListPanel extends JPanel {
		JPanel others;
		JButton hostButton;
		
		public HostListPanel() {
			super(new BorderLayout());

			others = new JPanel(new GridLayout(0, 2));
			add(others, BorderLayout.CENTER);
			
			hostButton = new JButton("Host My Own Game");
			hostButton.addActionListener((ActionEvent e) -> {
				if (server == null) {
					server = new ThingHost<SpaceGame>(new SpaceGame("[nom]"), MasterHost.GAME_PORT);
					server.start();
					masterClient.sendCommand(new CommandAddHost(server));
				}
			});
			add(hostButton, BorderLayout.SOUTH);
		}
		
		public void update() {
			hostButton.setEnabled(server == null);
			
	        others.removeAll();
			List<ThingHost<SpaceGame>> li = masterClient.getThing();
			if (li != null & li.size() > 0) {
				for (ThingHost<SpaceGame> host : li) {
					String gameString = host.getThing().toString();
					String addr = host.getAddress().toString();
					int port = host.getPort();
					String fmt = "<html><table>" + 
					"<tr><td>Game</td><td>%s</td></tr>" +
					"<tr><td>IP</td><td>%s</td></tr>" + 
					"<tr><td>Port</td><td>%d</td></tr></table></html>";
					others.add(new JLabel(String.format(fmt, gameString, addr, port)));
			        JButton button = new JButton("Join Game");
			        button.addActionListener((ActionEvent e) -> {
			        	if (client == null) {
			        		client = new ThingClient<>(host);
			        		client.start();
			        	}
			        });
			        button.setEnabled(client == null);
			        others.add(button);
				}
			}
		}
	}

	
	public static void main(String[] args) throws InterruptedException {
		MasterHost.startMasterServer();
		EventQueue.invokeLater(() -> {
			SpaceFrame frame = SpaceFrame.getFrame();
			frame.setVisible(true);
		});
	}
}
