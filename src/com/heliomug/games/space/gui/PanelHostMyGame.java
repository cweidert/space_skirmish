package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;

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
		label = new JLabel("Name: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		nameBox = new JTextField("");
		nameBox.grabFocus();
		panel.add(nameBox);
		label = new JLabel("Port: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		portBox = new JSpinner(new SpinnerNumberModel(Manager.GAME_PORT, 1, 65535, 1));
		portBox.setEditor(new JSpinner.NumberEditor(portBox, "#"));
		panel.add(portBox);
		
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		
		JButton button;
		
		button = new UpdatingButton("Host My Game", () -> Manager.hasOwnGame(), () -> {
			String name = nameBox.getText();
			name = name.length() == 0 ? "[no name]" : name;
			int port = (int) portBox.getValue();
			Manager.hostMyGame(name, port);
		});
		panel.add(button);

		button = new UpdatingButton("Remove My Hosted Game", () -> Manager.hasOwnGame(), () -> {
			Manager.deleteHostedGame();
		});
		panel.add(button);
		return panel;
	}
}
