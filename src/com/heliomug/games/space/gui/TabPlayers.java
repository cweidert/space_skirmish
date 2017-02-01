package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TabPlayers extends JPanel {
	public TabPlayers() {
		super(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new PanelListExternalPlayers(), BorderLayout.NORTH);
		panel.add(new PanelListLocalPlayers(), BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
		add(new PanelAddNewPlayer(), BorderLayout.SOUTH);
	}
}

