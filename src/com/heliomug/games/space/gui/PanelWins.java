package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.heliomug.games.space.Player;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class PanelWins extends UpdatingPanel {
	public PanelWins() {
		super(new GridLayout(1, 0));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	@Override
	public void update() {
        removeAll();
   		List<Player> players = SpaceFrame.getAllPlayers();
    	if (players != null & players.size() > 0) {
			for (Player player : players) {
				JLabel label = new JLabel(player.getWinString(), JLabel.CENTER);
				label.setOpaque(true);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				label.setBackground(player.getColor());
				add(label);
			}
    	} else {
			add(new JLabel("no players!"));
    	}
        revalidate();
	}

}
