package com.heliomug.games.space.gui;

import java.awt.FlowLayout;

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
public class PanelHostMyGame extends UpdatingPanel {
	private JTextField nameBox;
	private JSpinner portBox;
	
	public PanelHostMyGame() {
		super(new FlowLayout());
		PanelUtils.addEtch(this, "Host My Game");
		
		setupGUI();
	}
	
	private void setupGUI() {
		add(getOptionsPanel());
		add(getButtonPanel());
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
		portBox = new JSpinner(new SpinnerNumberModel(SpaceFrame.GAME_PORT, 1, 65535, 1));
		portBox.setEditor(new JSpinner.NumberEditor(portBox, "#"));
		panel.add(portBox);
		
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		
		JButton button;
		
		button = new UpdatingButton("Host", () -> SpaceFrame.hasOwnGame() && !SpaceFrame.isServing(), () -> {
			String name = nameBox.getText();
			name = name.length() == 0 ? "[no name]" : name;
			name = name.length() > 20 ? name.substring(0, 20) : name;
			int port = (int) portBox.getValue();
			SpaceFrame.hostMyGame(name, port);
		});
		panel.add(button);

		button = new UpdatingButton("Unhost", () -> SpaceFrame.isServing(), () -> {
			SpaceFrame.deleteHostedGame();
		});
		panel.add(button);
		return panel;
	}
	
	@Override
	public void update() {
		repaint();
	}
}
