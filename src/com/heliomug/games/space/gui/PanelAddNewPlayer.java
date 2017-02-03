package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.heliomug.games.space.Player;
import com.heliomug.utils.gui.ColorButton;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class PanelAddNewPlayer extends JPanel {
	private JTextField nameBox;
	private ColorButton colorButton;
	
	public PanelAddNewPlayer() {
		super(new FlowLayout());
		
		PanelUtils.addEtch(this, "Add New Player");

		add(getOptionsPanel());
		add(getButtonPanel());
	}
	
	public JPanel getOptionsPanel() {
		JPanel panel = new JPanel();
		JLabel label;
		label = new JLabel("Name: ");
		label.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(label);
		nameBox = new JTextField(25);
		panel.add(nameBox);
		panel.add(new JLabel("Color: "));
		colorButton = new ColorButton();
		panel.add(colorButton);
		return panel;
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		JButton button = new UpdatingButton("Add Player", KeyEvent.VK_P, () -> true, () -> {
			String name = nameBox.getText();
			name = name.length() == 0 ? "Joe Schmoe" : name;
			Color color = colorButton.getColor();
			SpaceFrame.addLocalPlayer(new Player(name, color));
			colorButton.resetColor();
		});
		panel.add(button);
		return panel;
	}
}
