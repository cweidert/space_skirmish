package com.heliomug.games.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.Timer;

public class SpaceGame implements Serializable, ActionListener {
	private static final long serialVersionUID = 1451293503897747642L;
	
	private static final int FRAME_RATE = 24;

	public static final double WIDTH = 128;
	public static final double HEIGHT = 96;
	
	public static final double BUFFER_WIDTH = 6;
	public static final Color BOUNDS_COLOR = Color.RED;
	public static final Rectangle2D ORIGINAL_BOUNDS = new Rectangle2D.Double(- WIDTH / 2, - HEIGHT / 2,	WIDTH, HEIGHT);
	
	private List<Sprite> sprites; 
	
	private List<Player> players;
	
	private Map<Player, Ship> playerAssignments;
	
	private long lastUpdated;
	private long timeStarted;
	private int updates;
	
	public SpaceGame() {
		sprites = new CopyOnWriteArrayList<>();
		players = new CopyOnWriteArrayList<>();
		playerAssignments = new ConcurrentHashMap<>();
		updates = 0;
	}

	public void start() {
		lastUpdated = timeStarted = System.currentTimeMillis();
		Timer timer = new Timer(1000/FRAME_RATE, this);
		timer.start();
	}
	
	public int getUpdates() {
		return updates;
	}
	
	public double getAge() {
		return (System.currentTimeMillis() - timeStarted) / 1000.0;
	}
	
	public double getUpdatesPerSec() {
		return getUpdates() / getAge();
	}
	
	public void addBullet(Bullet bullet) {
		sprites.add(bullet);
	}
	
	public Rectangle2D getBounds() {
		double minX = - WIDTH / 2;
		double minY = - HEIGHT / 2;
		double maxX = WIDTH / 2;
		double maxY = HEIGHT / 2;
		for (Ship ship : playerAssignments.values()) {
			double x = ship.getX();
			double y = ship.getY();
			if (x < minX) minX = x;
			if (y < minY) minY = y;
			if (x > maxX) maxX = x;
			if (y > maxY) maxY = y;
		}
		
		double buf = BUFFER_WIDTH;
		double rat = WIDTH / HEIGHT;
		
		return new Rectangle2D.Double(minX - buf * rat, minY - buf, maxX - minX + buf * 2 * rat, maxY - minY + buf * 2);
	}
	
	public void addPlayer(Player player) {
		Ship ship = new Ship(this);
		sprites.add(ship);
		players.add(player);
		playerAssignments.put(player, ship);
	}

	public void removePlayer(Player player) {
		Ship ship = playerAssignments.remove(player);
		players.remove(player);
		sprites.remove(ship);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Ship getShip(Player player) {
		return playerAssignments.get(player);
	}
	
	public void actionPerformed(ActionEvent e) {
		update();
	}
	
	public void update() {
		long now = System.currentTimeMillis();
		double dt = (now - lastUpdated) / 1000.0;
		for (Sprite sprite : sprites) {
			sprite.update(dt);
		}
		lastUpdated = now;
		updates++;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(BOUNDS_COLOR);
		g.draw(ORIGINAL_BOUNDS);
		for (Sprite sprite : sprites) {
			sprite.draw(g);
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Game (%.3f)\n", getAge()));
		sb.append("----Players----\n");
		for (Player player : players) {
			sb.append(player.toString() + "\n");
		}
		sb.append("----Sprites----\n");
		for (Sprite sprite : sprites) {
			sb.append(sprite.toString() + "\n");
		}
		return sb.toString();
	}
}
