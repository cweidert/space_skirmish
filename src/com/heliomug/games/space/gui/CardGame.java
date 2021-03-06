package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingCheckBox;

class CardGame extends JPanel { 
	private static final long serialVersionUID = -4501673998714242701L;
	
	private PanelGame board;
	
	public CardGame() {
		super(new BorderLayout());

		board = new PanelGame();
		
		setupGUI();
	}

	public void setupGUI() {
		add(board, BorderLayout.CENTER);
		add(getCardSwitchPanel(), BorderLayout.SOUTH);
		
		setFocusable(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					Session.quit();
				}
				Session.handleKey(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Session.handleKey(e.getKeyCode(), false);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	public void reset() {
		if (Session.hasOwnGame()) {
			Session.getGame().start();
			board.reset();
		}
	}
	
	private JPanel getCardSwitchPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 0));

		JButton button; 
		
		button = new UpdatingButton("Reset Round!", KeyEvent.VK_R, () -> Session.hasOwnGame(), () -> {
			if (Session.hasOwnGame()) {
				Session.getGame().reset();
				reset();
			}
		});
		panel.add(button);
		
		button = new UpdatingButton("Players", KeyEvent.VK_P, () -> {
			Frame.setCard(Frame.PLAYER_CARD);
		});
		panel.add(button);
		
		button = new UpdatingButton("Settings", KeyEvent.VK_S, () -> Session.hasOwnGame(), () -> {
			Frame.setCard(Frame.SETTINGS_CARD);
		});
		panel.add(button);
		
		button = new UpdatingButton("Internet", KeyEvent.VK_I, () -> {
			Frame.setCard(Frame.CONNECTIONS_CARD);
		});
		panel.add(button);
		
		JCheckBox box = new UpdatingCheckBox("Auto-Zoom", KeyEvent.VK_Z, (Boolean b) -> {
			board.setAutoZoom(b);
		}, () -> board.isAutoZoom());
		panel.add(box);
		
		return panel;
	}
}
