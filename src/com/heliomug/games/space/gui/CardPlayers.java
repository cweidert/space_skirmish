package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
public class CardPlayers extends JPanel {
	public CardPlayers() {
		super(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new PanelListExternalPlayers(), BorderLayout.NORTH);
		panel.add(new PanelListLocalPlayers(), BorderLayout.CENTER);
		panel.add(new PanelAddNewPlayer(), BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
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

