package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CardConnections extends JPanel {
	public CardConnections() {
		super(new BorderLayout());

		add(new PanelHostMyGame(), BorderLayout.NORTH);
		add(new PanelListHosts(), BorderLayout.CENTER);
		add(new PanelReturnToGame(), BorderLayout.SOUTH);
	}
}
