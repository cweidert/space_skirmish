package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
class PanelGoToGameButton extends JPanel {
	public PanelGoToGameButton() {
		super(new BorderLayout());
		JButton button = new UpdatingButton("Go to Game", () -> {
			Frame.setCard(Frame.GAME_CARD);
		});
		button.setMnemonic(KeyEvent.VK_G);
		add(button);	
	}
}
