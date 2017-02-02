package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.heliomug.games.space.Game;
import com.heliomug.utils.gui.WeidertPanel;

@SuppressWarnings("serial")
public class PanelGame extends WeidertPanel implements Runnable {
	private static final int BOARD_WIDTH = 640;
	private static final int BOARD_HEIGHT = 480;

	private static final int SLEEP_TIME = 1;
	
	private static final boolean DEFAULT_AUTO_ZOOM = true;

	private boolean isAutoZoom;

	public PanelGame() {
		super(BOARD_WIDTH, BOARD_HEIGHT);
		
		isAutoZoom = DEFAULT_AUTO_ZOOM;
		
		setupGUI();
		
		Thread t = new Thread(this);
		t.start();
	}
	
	private void setupGUI() {
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
	}
	
	public void setAutoZoom(boolean b) {
		isAutoZoom = b;
	}
	
	public boolean isAutoZoom() {
		return isAutoZoom;
	}
	
	@Override
	public void paintComponent(Graphics g) {

		Game game = SpaceFrame.getGame();
		if (game != null && game.isActive()) {
			if (isAutoZoom && (game.numberOfPlayers() == 0 || !game.allDead())) {
				if (game.getOptions().isWrap()) {
					setScreenBounds(game.getOptions().getWrapBounds());
				} else { 
					setScreenBounds(game.getBounds());
				}
			}
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			game.draw(g2);
		} 
	}
	
	public void run() {
		while(true) {
			repaint();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				System.out.println("Thread Interrupted");
				Thread.currentThread().interrupt();
			}
		}
	}
}
