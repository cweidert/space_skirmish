package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;

@SuppressWarnings("serial")
class CardGameType extends JPanel {
	public CardGameType() {
		super(new BorderLayout());
		JLabel label = new JLabel("Welcome to the Space / Tank Game", JLabel.CENTER);
		label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		add(label, BorderLayout.NORTH);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}
	
	public JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		UpdatingButton button;
		button = new UpdatingButton("Tank Game", KeyEvent.VK_T, () -> {
			Session.getGame().getSettings().setTankMode(true);
			Frame.setCard(Frame.QUICK_PLAYER_CARD);
			Frame.resetGamePanel();
		});
		panel.add(button);
		button = new UpdatingButton("Space Game", KeyEvent.VK_S, () -> {
			Session.getGame().getSettings().setTankMode(false);
			Frame.setCard(Frame.QUICK_PLAYER_CARD);
			Frame.resetGamePanel();
		});
		panel.add(button);
		return panel;
	}
}
