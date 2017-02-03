package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class CardConnections extends JPanel {
	public CardConnections() {
		super(new BorderLayout());

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new PanelHostMyGame(), BorderLayout.NORTH);
		panel.add(new PanelClient(), BorderLayout.SOUTH);
		add(panel, BorderLayout.NORTH);
		add(new PanelListHosts(), BorderLayout.CENTER);
		add(new PanelReturnToGame(), BorderLayout.SOUTH);
	}
}
