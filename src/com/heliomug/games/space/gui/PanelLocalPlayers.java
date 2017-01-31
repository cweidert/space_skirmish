package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.Player;

@SuppressWarnings("serial")
public class PanelLocalPlayers extends JPanel {
	JButton addPlayerButton;
	JPanel playerList;
	
	public PanelLocalPlayers() {
		super(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		playerList = new JPanel(new GridLayout(0, 1));
		playerList.add(new JLabel("apobine"));
		add(playerList, BorderLayout.CENTER);
		
		addPlayerButton = new JButton("Add Player");
		addPlayerButton.addActionListener((ActionEvent e) -> {
			if (SpaceFrame.getClient() != null) {
				SpaceFrame.addLocalPlayer(new Player("Joe"));
			}
		});
		addPlayerButton.setEnabled(false);
		add(addPlayerButton, BorderLayout.SOUTH);
	}
	
	public void update() {
		addPlayerButton.setEnabled(SpaceFrame.getClient() != null);
		
        playerList.removeAll();
		List<Player> players = SpaceFrame.getLocalPlayers();
        if (players != null & players.size() > 0) {
			for (Player player : players) {
		        playerList.add(new JLabel(player.toString()));
			}
		} else {
			playerList.add(new JLabel("no players yet!"));
		}
        revalidate();
	}
}


