package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TabConnections extends JPanel {
	public TabConnections() {
		super(new BorderLayout());

		add(new PanelListHosts(), BorderLayout.NORTH);
		add(new PanelHostMyGame(), BorderLayout.SOUTH);
	}
	
}
