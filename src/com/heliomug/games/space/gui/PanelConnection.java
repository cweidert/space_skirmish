package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.heliomug.game.server.ServerMaster;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.gui.utils.UpdatingLabel;

@SuppressWarnings("serial")
public class PanelConnection extends JPanel implements ActionListener {
	
	public PanelConnection() {
		super();
		
		setupGUI();
		Timer t = new Timer(1000, this);
		t.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void setupGUI() {
		setLayout(new BorderLayout());
		
		// server panel
		add(getServerPanel(), BorderLayout.WEST);
		add(getClientPanel(), BorderLayout.EAST);
		add(new PlayerPanel(), BorderLayout.CENTER);
	}
	
	public JPanel getClientPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
		JButton button;
		// client panel
		JTextField host = new JTextField(ServerMaster.HOST);
		panel.add(host, gbc);
		JTextField port = new JTextField(String.valueOf(ServerMaster.DEFAULT_PORT_NUMBER));
		panel.add(port, gbc);
		button = new JButton("Start Client");
		button.addActionListener((ActionEvent e) -> {
			try {
				SpaceFrame.getClient().start(host.getText(), Integer.parseInt(port.getText()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} 
		});
		panel.add(button, gbc);
		button = new JButton("Add Player");
		button.addActionListener((ActionEvent e) -> {
			//String name = (String)JOptionPane.showInputDialog(SpaceFrame.getFrame(), "New name?", "Player");
			String name = "Joe";
			Player player = new Player(name);
			SpaceFrame.getFrame().addPlayer(player);
		});
		panel.add(button, gbc);
		panel.add(new UpdatingLabel("Pulled/s", () -> {
			double gps = SpaceFrame.getClient().getGamesPulledPerSec();
			return String.format("Pulled/s: %.3f", gps);
		}), gbc);

		return panel;
	}
	
	public JPanel getServerPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		JButton button;

		JTextField port = new JTextField(String.valueOf(ServerMaster.DEFAULT_PORT_NUMBER));
		panel.add(port, gbc);
		button = new JButton("Start Server");
		button.addActionListener((ActionEvent e) -> {
			SpaceFrame.getFrame().getServer().start(Integer.parseInt(port.getText()), new SpaceGame());
		});
		panel.add(button, gbc);
		button = new JButton("Start Game");
		button.addActionListener((ActionEvent e) -> {
			SpaceFrame.getFrame().getServer().getThing().start();
		});
		panel.add(button, gbc);
		panel.add(new UpdatingLabel("Served/s", () -> {
			double gps = SpaceFrame.getFrame().getServer().getAvgServedPerSec();
			return String.format("Served/s: %.3f", gps);
		}), gbc);
		panel.add(new UpdatingLabel("Updates/s", () -> {
			SpaceGame game = SpaceFrame.getFrame().getServer().getThing(); 
			if (game != null) {
				double gps = game.getUpdatesPerSec();
				return String.format("Updates/s: %.3f", gps);
			} else {
				return "Updates/s";
			}
		}), gbc);

		return panel;
	}
	
	private class PlayerPanel extends JPanel {
		public PlayerPanel() {
			super();
			setLayout(new GridLayout(0, 1));
			add(new JLabel("No players yet!"));
		}

		@Override
		public void paint(Graphics g) {
			this.removeAll();
			List<Player> players = SpaceFrame.getFrame().getLocalPlayers();
			if (players.size() == 0) {
				add(new JLabel("No players yet!"));
			} else {
				for (Player player : players) {
					add(new PanelPlayerSettings(player));
				}
			}
			revalidate();
			super.paint(g);
		}
	}
}
