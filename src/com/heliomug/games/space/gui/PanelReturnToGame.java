package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
class PanelReturnToGame extends JPanel {
	public PanelReturnToGame() {
		super(new BorderLayout());
		JButton button = new UpdatingButton("Return to Game", () -> {
			SpaceFrame.setCard(SpaceFrame.GAME_CARD);
		});
		button.setMnemonic(KeyEvent.VK_G);
		add(button);	
	}
}
