package com.heliomug.games.space.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.Player;

@SuppressWarnings("serial")
public class PanelPlayerSettings extends JPanel {
	public PanelPlayerSettings(Player player) {
		super();
		setLayout(new GridLayout(1, 0));

		ControlConfig controls = SpaceFrame.getFrame().getControls(player);
		
		
		add(new JLabel(player.getName()));
		add(new JLabel("Left: " + controls.getLeftString()));
		add(new JLabel("Right: " + controls.getRightString()));
		add(new JLabel("Boost: " + controls.getBoostString()));
		add(new JLabel("Fire: " + controls.getFireString()));
	}
}
