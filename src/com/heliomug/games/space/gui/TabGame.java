package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.heliomug.utils.gui.WeidertButton;

@SuppressWarnings("serial")
public class TabGame extends JPanel {
	public TabGame() {
		super(new BorderLayout());
		
		add(new PanelGame(), BorderLayout.CENTER);
		
		
		JPanel optionPanel = new JPanel(new GridLayout(1, 0)); 
		JButton button;
		
		button = new WeidertButton("Start Game!") {
			@Override
			public void paintComponent(Graphics g) {
				setEnabled(SpaceFrame.getServer() != null);
				super.paintComponent(g);
			}
		};
		button.addActionListener((ActionEvent e) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().start();
			}
		});
		optionPanel.add(button);
		button = new WeidertButton("Reset Game!") {
			@Override
			public void paintComponent(Graphics g) {
				setEnabled(SpaceFrame.getServer() != null);
				super.paintComponent(g);
			}
		};
		button.addActionListener((ActionEvent e) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().reset();
			}
		});
		optionPanel.add(button);

		add(optionPanel, BorderLayout.SOUTH);

	}
}
