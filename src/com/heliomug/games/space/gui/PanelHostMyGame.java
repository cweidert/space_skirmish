package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
class PanelHostMyGame extends UpdatingPanel {
	private JTextField nameBox;
	private JSpinner portBox;
	
	public PanelHostMyGame() {
		super(new BorderLayout());
		PanelUtils.addEtch(this, "Host My Game");
		
		setupGUI();
	}
	
	private void setupGUI() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(getOptionsPanel());
		panel.add(getButtonPanel());
		add(panel, BorderLayout.CENTER);
		String message = "to host a game on the internet, you'll have to enable port forwarding on your router";
		add(new JLabel(message, JLabel.CENTER), BorderLayout.SOUTH);
	}

	public JPanel getOptionsPanel() {
		JPanel panel = new JPanel();
		JLabel label;
		label = new JLabel("Name: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		nameBox = new JTextField(20);
		nameBox.grabFocus();
		panel.add(nameBox);
		label = new JLabel("Port: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		portBox = new JSpinner(new SpinnerNumberModel(Session.GAME_PORT, 1, 65535, 1));
		portBox.setEditor(new JSpinner.NumberEditor(portBox, "#"));
		panel.add(portBox);
		
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		
		JButton button;
		
		button = new UpdatingButton("Host", KeyEvent.VK_H, () -> Session.hasOwnGame() && !Session.isServing(), () -> {
			String name = nameBox.getText();
			name = name.length() == 0 ? "[no name]" : name;
			name = name.length() > 20 ? name.substring(0, 20) : name;
			int port = (int) portBox.getValue();
			Session.hostMyGame(name, port);
		});
		panel.add(button);

		button = new UpdatingButton("Unhost", KeyEvent.VK_U, () -> Session.isServing(), () -> {
			Session.deleteHostedGame();
		});
		panel.add(button);
		return panel;
	}
	
	@Override
	public void update() {
		repaint();
	}
}
