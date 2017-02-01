package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.server.CommandAddHost;
import com.heliomug.games.space.server.CommandRemoveHost;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.games.space.server.MasterHost;
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class PanelHostMyGame extends EtchedPanel {
	private JTextField nameBox;
	private JSpinner portBox;
	
	public PanelHostMyGame() {
		super("Host My Own Game", new BorderLayout());
		
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
		portBox = new JSpinner(new SpinnerNumberModel(MasterHost.GAME_PORT, 1, 65535, 1));
		portBox.setEditor(new JSpinner.NumberEditor(portBox, "#"));
		panel.add(portBox);
		
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		
		JButton button;
		
		button = new UpdatingButton("Host My Own Game", () -> Frame.getServer() == null, () -> {
			MasterClient masterClient = Frame.getMasterClient(); 
			ThingHost<Game> server = Frame.getServer(); 
			ThingClient<Game> client = Frame.getClient(); 
			if (server == null && client == null) {
				String name = nameBox.getText();
				name = name.length() == 0 ? "[no name]" : name;
				int port = (int) portBox.getValue();
				ThingHost<Game> myServer = new ThingHost<Game>(new Game(name), port); 
				Frame.setServer(myServer);
				myServer.start();
				if (masterClient != null) {
					masterClient.sendCommand(new CommandAddHost(myServer));
				}
				InetAddress address = InetAddress.getLoopbackAddress();
				ThingClient<Game> myClient = new ThingClient<Game>(address.getHostAddress(), port);
				Frame.setClient(myClient);
				myClient.start((Boolean b) -> {});
			}
		});
		panel.add(button);

		button = new UpdatingButton("Remove My Hosted Game", () -> Frame.getServer() != null, () -> {
			MasterClient masterClient = Frame.getMasterClient(); 
			ThingHost<Game> server = Frame.getServer(); 
			ThingClient<Game> client = Frame.getClient(); 
			if (server != null) {
				server.kill();
				client.kill();
				if (masterClient != null) {
					masterClient.sendCommand(new CommandRemoveHost(server));
				}
				Frame.setServer(null);
				Frame.setClient(null);
			}
		});
		panel.add(button);
		return panel;
	}
}
