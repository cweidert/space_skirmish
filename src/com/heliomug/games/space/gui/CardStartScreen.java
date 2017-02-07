package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingRadioButton;

@SuppressWarnings("serial")
class CardStartScreen extends JPanel {
	private int numPlayers;
	private boolean isTankMode;
	
	public CardStartScreen() {
		super(new BorderLayout());
		numPlayers = 1;
		isTankMode = false;

		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Welcome to the Space / Tank Game", JLabel.CENTER);
		label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		add(label, BorderLayout.NORTH);
		panel.add(getTypePanel(), BorderLayout.WEST);
		panel.add(getNumberOfPlayersPanel(), BorderLayout.EAST);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);

		JPanel superPanel = new JPanel();
		superPanel.add(panel);
		add(superPanel, BorderLayout.CENTER);
	}
	
	public JPanel getNumberOfPlayersPanel() {
		JPanel panel = new JPanel(new GridLayout(6, 1));
		ButtonGroup group = new ButtonGroup();
		
		for (int i = 1 ; i <= 5 ; i++) {
			panel.add(new PlayerButton(i, group));
		}
	
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		panel.add(new UpdatingButton("Play!", KeyEvent.VK_P, () -> {
			Session.getGame().getSettings().setTankMode(isTankMode);
			Session.addLocalPlayers(numPlayers);
			Frame.resetGamePanel();
			Frame.setCard(Frame.GAME_CARD);
		}));
		
		return panel;
	}
	
	private String playerString(int i) {
		if (i == 1) {
			return "1 player";
		} else {
			return i + " players";
		}
	}
	
	public JPanel getTypePanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		ButtonGroup group = new ButtonGroup();
		UpdatingRadioButton button;
		button = new UpdatingRadioButton("Space Game", KeyEvent.VK_S, group, () -> {
			isTankMode = false;
		});
		button.setSelected(true);
		panel.add(button);
		button = new UpdatingRadioButton("Tank Game", KeyEvent.VK_T, group, () -> {
			isTankMode = true;
		});
		panel.add(button);
		return panel;
	}

	private class PlayerButton extends UpdatingRadioButton {
		public PlayerButton(int i, ButtonGroup group) {
			// VK_0 + i is a kludge
			super(playerString(i), KeyEvent.VK_0 + i, group, () -> {});
			if (i == 2) {
				setSelected(true);
				numPlayers = i;
			}
			addActionListener((ActionEvent e) -> {
				numPlayers = i;
			});
		}
	}

}
