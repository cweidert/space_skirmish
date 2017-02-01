package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TabLocalPlayers extends JPanel {
	public TabLocalPlayers() {
		super(new BorderLayout());
		add(new PanelListLocalPlayer(), BorderLayout.CENTER);
		add(new PanelAddNewPlayer(), BorderLayout.SOUTH);
	}
}

