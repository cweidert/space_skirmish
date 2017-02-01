package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingCheckBox;

public class TabGame extends JPanel { 
	private static final long serialVersionUID = -4501673998714242701L;
	
	private PanelGame board;
	
	public TabGame() {
		super(new BorderLayout());

		board = new PanelGame();
		
		setupGUI();
	}

	public void setupGUI() {
		add(board, BorderLayout.CENTER);
		add(getOptionPanel(), BorderLayout.SOUTH);
	}

	public JPanel getOptionPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));

		JButton button; 
		
		button = new UpdatingButton("Start Local Game!", () -> SpaceFrame.getClient() != null, () -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().start();
			}
		});
		panel.add(button);

		button = new UpdatingButton("Start Round!", () -> SpaceFrame.getServer() != null, () -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().start();
			}
		});
		panel.add(button);
		
		JCheckBox box = new UpdatingCheckBox("Auto-Zoom", (Boolean b) -> {
			board.setAutoZoom(b);
		}, () -> board.isAutoZoom());
		panel.add(box);
		
		return panel;
	}
}
