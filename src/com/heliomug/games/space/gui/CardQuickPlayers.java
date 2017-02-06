package com.heliomug.games.space.gui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
class CardQuickPlayers extends JPanel {
	public CardQuickPlayers() {
		super(new GridLayout(6, 1));
		
		for (int i = 1 ; i <= 6 ; i++) {
			add(new PlayerButton(i));
		}
	}
	
	private String playerString(int i) {
		if (i == 1) {
			return "1 player";
		} else {
			return i + " players";
		}
	}

	private class PlayerButton extends UpdatingButton {
		public PlayerButton(int i) {
			// VK_0 + i is a kludge
			super(playerString(i), KeyEvent.VK_0 + i, () -> {
				Session.addLocalPlayers(i);
				Frame.setCard(Frame.GAME_CARD);
			});
		}
	}
}
