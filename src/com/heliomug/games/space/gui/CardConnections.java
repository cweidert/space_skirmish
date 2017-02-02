package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class CardConnections extends JPanel {
	public CardConnections() {
		super(new BorderLayout());

		add(new PanelHostMyGame(), BorderLayout.NORTH);
		add(new PanelListHosts(), BorderLayout.CENTER);
		add(getOptionPanel(), BorderLayout.SOUTH);
	}
	
	public JPanel getOptionPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));

		JButton button; 
		
		button = new UpdatingButton("Return to Game", () -> {
			SpaceFrame.setCard(SpaceFrame.GAME_CARD);
		});
		panel.add(button);
		
		return panel;
	}
	
}
