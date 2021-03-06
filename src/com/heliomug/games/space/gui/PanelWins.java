package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.heliomug.games.space.Player;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
class PanelWins extends UpdatingPanel {
	public PanelWins() {
		super(new GridLayout(1, 0));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.BLACK);
		JLabel label = new JLabel("no players yet", JLabel.CENTER);
		label.setBackground(Color.GRAY);
		label.setOpaque(true);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(label);
	}
	
	@Override
	public void update() {
        removeAll();
   		List<Player> players = Session.getAllPlayers();
    	if (players != null & players.size() > 0) {
			for (Player player : players) {
				String winString = players.size() > 8 ? String.valueOf(player.getWins()) : player.getWinString();
				JLabel label = new JLabel(winString, JLabel.CENTER);
				label.setOpaque(true);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				label.setBackground(player.getColor());
				add(label);
			}
    	} else {
			JLabel label = new JLabel("no players yet", JLabel.CENTER);
			label.setBackground(Color.GRAY);
			label.setOpaque(true);
			label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			add(label);
    	}
        revalidate();
        repaint();
	}

}
