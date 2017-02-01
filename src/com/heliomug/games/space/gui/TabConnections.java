package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TabConnections extends JPanel {
	public TabConnections() {
		super(new BorderLayout());

		add(new PanelListHost(), BorderLayout.NORTH);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new PanelJoinCustomGame(), BorderLayout.NORTH);
		panel.add(new PanelHostMyGame(), BorderLayout.SOUTH);
		
		add(panel, BorderLayout.SOUTH);
	}
	
}
