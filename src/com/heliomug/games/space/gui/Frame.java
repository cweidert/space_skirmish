package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class Frame extends JFrame {
	public static final int GAME_PORT = 27960;
	
	public static final int SERVER_DELAY = 250; 
	
	private static Frame theFrame;
	
	public static Frame getFrame() {
		if (theFrame == null) {
			theFrame = new Frame();
		}
		return theFrame;
	}
	
	public static final String GAME_TYPE_CARD = "game type card";
	public static final String QUICK_PLAYER_CARD = "quick player card";
	public static final String GAME_CARD = "game card";
	public static final String PLAYER_CARD = "player card";
	public static final String SETTINGS_CARD = "settings card";
	public static final String CONNECTIONS_CARD = "connections card";
	
	private JPanel cardPanel;
	private CardGame gameCard;
	
	Session space;
	
	private Frame() {
		super("Networked Space / Tank Game");
		
		space = Session.getSpace();
		
		setupGUI();
	}
	
	private void setupGUI() {
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					Session.quit();
				}
				Session.handleKey(e.getKeyCode(), true);
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(new PanelWins(), BorderLayout.NORTH);
		
		cardPanel = new JPanel(new CardLayout());
		cardPanel.setFocusable(false);
		gameCard = new CardGame();
		gameCard.reset();
		cardPanel.add(gameCard, GAME_CARD);
		cardPanel.add(new CardQuickPlayers(), QUICK_PLAYER_CARD);
		cardPanel.add(new CardGameType(), GAME_TYPE_CARD);
		cardPanel.add(new CardPlayers(), PLAYER_CARD);
		cardPanel.add(new CardSettings(), SETTINGS_CARD);
		cardPanel.add(new CardConnections(), CONNECTIONS_CARD);
		panel.add(cardPanel, BorderLayout.CENTER);

		add(panel);
		pack();
		
		CardLayout layout = (CardLayout) cardPanel.getLayout();
		layout.show(cardPanel, GAME_TYPE_CARD);
	}
	
	public static void resetGamePanel() {
		getFrame().gameCard.reset();
	}
	
	public static void setCard(String cardName) {
		getFrame().setVisibleCard(cardName);
	}
	
	private void setVisibleCard(String cardName) {
		CardLayout layout = (CardLayout) cardPanel.getLayout();
		layout.show(cardPanel, cardName);
		if (cardName.equals(GAME_CARD)) {
			gameCard.requestFocusInWindow();
		}
	}
}