package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.heliomug.games.space.Player;
import com.heliomug.utils.gui.ColorButton;
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class PanelAddNewPlayer extends JPanel {
	private JTextField nameBox;
	private ColorButton colorButton;
	
	public PanelAddNewPlayer() {
		super(new BorderLayout());
		
		EtchedPanel.addEtch(this, "Add New Player");

		add(getOptionsPanel(), BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}
	
	public JPanel getOptionsPanel() {
		JPanel panel = new JPanel();
		JLabel label;
		label = new JLabel("Player Name: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		nameBox = new JTextField(30);
		panel.add(nameBox);
		panel.add(new JLabel("Player Color: "));
		colorButton = new ColorButton();
		panel.add(colorButton);
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		JButton button = new UpdatingButton("Add Player", () -> Frame.getClient() != null, () -> {
			if (Frame.getClient() != null) {
				String name = nameBox.getText();
				name = name.length() == 0 ? "Joe Schmoe" : name;
				Color color = colorButton.getColor();
				Frame.addLocalPlayer(new Player(name, color));
				colorButton.resetColor();
			}
		});
		panel.add(button);
		return panel;
	}
}
