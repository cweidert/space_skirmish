package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.heliomug.games.space.Game;
import com.heliomug.games.space.server.GameAddress;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.server.Client;
import com.heliomug.utils.server.Server;

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
		
		button = new UpdatingButton("Create Local Game!", () -> SpaceFrame.getClient() == null, () -> {
			if (SpaceFrame.getServer() == null) {
				Server<Game> server = SpaceFrame.makeMyOwnServer("[no name]", 27960);
				try {
					Client<Game> client = new GameAddress(server).getClientFor();
					client.start();
					SpaceFrame.setClient(client);
				} catch (IOException e) {
					System.err.println("couldn't set client to own local game");
					e.printStackTrace();
				}
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
