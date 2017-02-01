package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.heliomug.game.server.ThingClient;
import com.heliomug.games.space.Game;
import com.heliomug.utils.gui.WeidertPanel;

@SuppressWarnings("serial")
public class Board extends WeidertPanel implements Runnable {
	private static final int BOARD_WIDTH = 640;
	private static final int BOARD_HEIGHT = 480;

	private static final int SLEEP_TIME = 24;
	
	private static final boolean DEFAULT_AUTO_ZOOM = true;

	private boolean isAutoZoom;

	public Board() {
		super(BOARD_WIDTH, BOARD_HEIGHT);
		
		isAutoZoom = DEFAULT_AUTO_ZOOM;
		
		setupGUI();
		
		Thread t = new Thread(this);
		t.start();
	}
	
	private void setupGUI() {
		setBackground(Color.BLACK);
		setFocusable(true);
		setDoubleBuffered(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				Frame.getFrame().handleKey(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Frame.getFrame().handleKey(e.getKeyCode(), false);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}
	
	public void setAutoZoom(boolean b) {
		isAutoZoom = b;
	}
	
	public boolean isAutoZoom() {
		return isAutoZoom;
	}
	
	@Override
	public void paintComponent(Graphics g) {

		ThingClient<Game> client = Frame.getClient();
		if (client != null) {
			Game game = client.getThing();
			if (game != null && game.isActive()) {
				if (isAutoZoom) {
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
