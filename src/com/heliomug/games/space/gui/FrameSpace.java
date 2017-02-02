package com.heliomug.games.space.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
public class FrameSpace extends JFrame {
	private static FrameSpace theFrame;
	
	public static FrameSpace getFrame() {
		if (theFrame == null) {
			theFrame = new FrameSpace();
		}
		return theFrame;
	}
	
	private FrameSpace() {
		super("Networked Space Game");
		
		setupGUI();
	}
	
	private void setupGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(new PanelWins(), BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		TabGame gameTab = new TabGame();
		tabbedPane.addTab("Game", gameTab);
		tabbedPane.addTab("Local Players", new TabPlayers());
		tabbedPane.addTab("Game Options", new PanelOptions());
		tabbedPane.addTab("Internet Games", new TabConnections());
		panel.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addChangeListener((ChangeEvent e) -> {
			gameTab.requestFocusInWindow();
		});

		this.add(panel);
		pack();
	}
}
