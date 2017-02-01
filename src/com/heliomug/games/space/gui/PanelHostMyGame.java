package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.games.space.Game;
import com.heliomug.games.space.server.CommandAddHost;
import com.heliomug.games.space.server.CommandRemoveHost;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.games.space.server.MasterServer;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

@SuppressWarnings("serial")
public class PanelHostMyGame extends JPanel {
	private JTextField nameBox;
	private JSpinner portBox;
	
	public PanelHostMyGame() {
		super(new BorderLayout());
		PanelUtils.addEtch(this, "Host My Game");
		
		setupGUI();
	}
	
	private void setupGUI() {
		add(getOptionsPanel(), BorderLayout.NORTH);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}

	public JPanel getOptionsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));
		JLabel label;
		label = new JLabel("Game Name: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		nameBox = new JTextField("");
		panel.add(nameBox);
		label = new JLabel("Port: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		portBox = new JSpinner(new SpinnerNumberModel(MasterServer.GAME_PORT, 1, 65535, 1));
		portBox.setEditor(new JSpinner.NumberEditor(portBox, "#"));
		panel.add(portBox);
		
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		
		JButton button;
		
		button = new UpdatingButton("Host My Own Game", () -> SpaceFrame.getServer() == null, () -> {
			MasterClient masterClient = SpaceFrame.getMasterClient(); 
			Server<Game> server = SpaceFrame.getServer(); 
			Client<Game> client = SpaceFrame.getClient(); 
			if (server == null && client == null) {
				String name = nameBox.getText();
				name = name.length() == 0 ? "[no name]" : name;
				int port = (int) portBox.getValue();
				Server<Game> myServer = new Server<Game>(new Game(name), port); 
				SpaceFrame.setServer(myServer);
				myServer.start();
				if (masterClient != null) {
					masterClient.sendCommand(new CommandAddHost(myServer));
				}
				InetAddress address = InetAddress.getLoopbackAddress();
				Client<Game> myClient;
				try {
					myClient = new Client<Game>(address, port);
					SpaceFrame.setClient(myClient);
					myClient.start();
				} catch (IOException e) {
					System.err.println("could not connect to my own game");
					e.printStackTrace();
				}
			}
		});
		panel.add(button);

		button = new UpdatingButton("Remove My Hosted Game", () -> SpaceFrame.getServer() != null, () -> {
			MasterClient masterClient = SpaceFrame.getMasterClient(); 
			Server<Game> server = SpaceFrame.getServer(); 
			if (server != null) {
				server.stop();
				SpaceFrame.setServer(null);
				if (masterClient != null) {
					masterClient.sendCommand(new CommandRemoveHost(server));
				}
			}
			Client<Game> client = SpaceFrame.getClient(); 
			if (client != null) {
				client.stop();
				SpaceFrame.setClient(null);
			}
		});
		panel.add(button);
		return panel;
	}
}
