package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.game.server.ThingClient;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.server.MasterHost;
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class PanelJoinCustomGame extends EtchedPanel {
	private JTextField nameBox;
	private JSpinner portBox;
	
	public PanelJoinCustomGame() {
		super("JoinCustomGame", new BorderLayout());
		
		setupGUI();
	}
	
	private void setupGUI() {
		add(getOptionsPanel(), BorderLayout.NORTH);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}

	public JPanel getOptionsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));
		JLabel label;
		label = new JLabel("IP Address: ");
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
		JButton button = new UpdatingButton("Join Custom Game", () -> Frame.getClient() == null, () -> {
			if (Frame.getClient() == null) {
				String host = nameBox.getText();
				int port = (int)portBox.getValue();
				ThingClient<Game> myClient = new ThingClient<Game>(host, port);
				Frame.setClient(myClient);
				myClient.start((Boolean b) -> {});
			}
		});
		panel.add(button);
		return panel;
	}
}
