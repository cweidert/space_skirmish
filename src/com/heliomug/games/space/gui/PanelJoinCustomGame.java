package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.games.space.server.GameAddress;
import com.heliomug.games.space.server.MasterServer;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class PanelJoinCustomGame extends JPanel {
	private JTextField nameBox;
	private JSpinner portBox;
	
	public PanelJoinCustomGame() {
		super(new BorderLayout());
		PanelUtils.addEtch(this, "Join Unlisted Game");

		setupGUI();
	}
	
	private void setupGUI() {
		add(getOptionsPanel(), BorderLayout.NORTH);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}

	public JPanel getOptionsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));
		JLabel label;
		label = new JLabel("Host Name / IP Address: ");
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
		JButton button = new UpdatingButton("Join Custom Game", () -> true, () -> {
			InetAddress address;
			try {
				address = InetAddress.getByName(new URL("http://" + nameBox.getText()).getHost());
				int port = (int)portBox.getValue();
				GameAddress gameAddress = new GameAddress(address, port); 
				SpaceFrame.joinGame(gameAddress);
			} catch (UnknownHostException | MalformedURLException e) {
				System.err.println("unable to connect to game");
				e.printStackTrace();
			}
		});
		panel.add(button);
		return panel;
	}
}
