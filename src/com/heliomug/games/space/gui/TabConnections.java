package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class TabConnections extends JPanel {
	private PanelHostList panelHostList;
	private PanelLocalPlayers panelLocalPlayers;
	private PanelHost panelHost;
	
	public TabConnections() {
		super(new BorderLayout());
		
		panelHostList = new PanelHostList();
		panelLocalPlayers = new PanelLocalPlayers();
		panelHost = new PanelHost();
		
		add(panelHostList, BorderLayout.WEST);
		add(panelLocalPlayers, BorderLayout.CENTER);
		add(new PanelHost(), BorderLayout.EAST);

		Timer t = new Timer(500, (ActionEvent e) -> {
			panelHostList.update();
			panelLocalPlayers.update();
			panelHost.update();
			repaint();
		});
		t.start();
	}
}
