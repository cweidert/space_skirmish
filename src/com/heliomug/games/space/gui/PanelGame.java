package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.heliomug.game.server.ThingClient;
import com.heliomug.games.space.Game;
import com.heliomug.utils.gui.WeidertPanel;

public class PanelGame extends WeidertPanel implements Runnable {
	private static final long serialVersionUID = -4501673998714242701L;
	private static final int SLEEP_TIME = 1;
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	private static final boolean DEFAULT_AUTO_ZOOM = true;
	
	boolean isAutoZoom;
	
	public PanelGame() {
		super(WIDTH, HEIGHT);

		this.isAutoZoom = DEFAULT_AUTO_ZOOM;
		
		setupGUI();
		
		Thread t = new Thread(this);
		t.start();
	}

	public boolean isAutoZoom() {
		return this.isAutoZoom;
	}
	
	public void setAutoZoom(boolean b) {
		this.isAutoZoom = b;
	}
	
	public void setupGUI() {
		setFocusable(true);
		setDoubleBuffered(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				SpaceFrame.getFrame().handleKey(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				SpaceFrame.getFrame().handleKey(e.getKeyCode(), false);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
			
		});
		setBackground(Color.BLACK);
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
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		ThingClient<Game> client = SpaceFrame.getClient();
		if (client != null) {
			Game game = client.getThing();
			if (game != null && game.isActive()) {
				Graphics2D g2 = (Graphics2D)g;
				if (game.getOptions().isWrap()) {
					setScreenBounds(game.getOptions().getOriginalBounds());
				} else if (isAutoZoom) {
					setScreenBounds(game.getBounds());
				}
				game.draw(g2);
			} 
		} 
	}
}
