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

import com.heliomug.utils.games.QuadTree;

public class SpaceGame implements Serializable, ActionListener {
	private static final long serialVersionUID = 1451293503897747642L;

	private static final boolean DEFAULT_IS_GRAVITY = true;
	
	private static final int FRAME_RATE = 24;

	public static final double WIDTH = 192;
	public static final double HEIGHT = 144;
	public static final double START_RAD = 64;
	public static final double START_SPEED = 27;
	
	public static final double BUFFER_WIDTH = 6;
	public static final Color BOUNDS_COLOR = Color.RED;
	public static final Rectangle2D ORIGINAL_BOUNDS = new Rectangle2D.Double(- WIDTH / 2, - HEIGHT / 2,	WIDTH, HEIGHT);
	
	public static final double DEFAULT_BIG_G = 10000;
	public static final boolean DEFAULT_IS_WRAP = true;
	public static final boolean DEFAULT_IS_PLANET = true; 
	
	
	private List<Sprite> sprites; 
	
	private List<Player> players;
	
	private Map<Player, Ship> playerAssignments;
	
	private String name;
	
	private long lastUpdated;
	private long timeStarted;
	private int updates;

	private double bigG;
	private boolean isGravity;
	private boolean isActive;
	private boolean isWrap;
	private boolean isPlanet;
	
	public SpaceGame() {
		this("[no name]");
	}
	
	public SpaceGame(String name) {
		sprites = new CopyOnWriteArrayList<>();
		players = new CopyOnWriteArrayList<>();
		playerAssignments = new ConcurrentHashMap<>();
		isGravity = DEFAULT_IS_GRAVITY;
		isWrap = DEFAULT_IS_WRAP;
		isPlanet = DEFAULT_IS_PLANET;
		isActive = false;
		bigG = DEFAULT_BIG_G;
		updates = 0;
		this.name = name;
	}

	public boolean isActive() {
		return this.isActive;
	}
	
	
	public boolean isWrap() {
		return this.isWrap;
	}
	
	public void setWrap(boolean b) {
		isWrap = b;
	}
	
	
	public boolean isPlanet() {
		return isPlanet;
	}
	
	public void setPlanet(boolean b) {
		isPlanet = b;
	}
	
	public boolean isGravity() {
		return isGravity;
	}
	
	public void setGravity(boolean b) {
		isGravity = b;
	}
	
	
	public void setBigG(double g) {
		bigG = g;
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
	
	
	public Rectangle2D getBounds() {
		double minX = - WIDTH / 2;
		double minY = - HEIGHT / 2;
		double maxX = WIDTH / 2;
		double maxY = HEIGHT / 2;
		for (Ship ship : playerAssignments.values()) {
			double x = ship.getPosition().getX();
			double y = ship.getPosition().getY();
			if (x < minX) minX = x;
			if (y < minY) minY = y;
			if (x > maxX) maxX = x;
			if (y > maxY) maxY = y;
		}
		
		double buf = BUFFER_WIDTH;
		double rat = WIDTH / HEIGHT;
		
		return new Rectangle2D.Double(minX - buf * rat, minY - buf, maxX - minX + buf * 2 * rat, maxY - minY + buf * 2);
	}
	
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		playerAssignments.put(player, new Ship());
	}

	public void removePlayer(Player player) {
		Ship ship = playerAssignments.remove(player);
		players.remove(player);
		sprites.remove(ship);
	}
	
	
	public void handleShipSignal(Player player, ShipSignal signal) {
		Ship ship = playerAssignments.get(player);
		if (signal == ShipSignal.TURN_LEFT) {
			ship.setTurnDirection(TurnDirection.LEFT);
		} else if (signal == ShipSignal.TURN_RIGHT) {
			ship.setTurnDirection(TurnDirection.RIGHT);
		} else if (signal == ShipSignal.TURN_NONE) {
			ship.setTurnDirection(TurnDirection.NONE);
		} else if (signal == ShipSignal.ACCEL_ON) {
			ship.setBoostOn(true);
		} else if (signal == ShipSignal.ACCEL_OFF) {
			ship.setBoostOn(false);
		} else if (signal == ShipSignal.FIRE) {
			Bullet bullet = ship.getBullet();
			sprites.add(bullet);
		}
	}
	
	
	public void start() {
		Timer timer = new Timer(1000/FRAME_RATE, this);
		timer.start();
		reset();
	}
	
	public void reset() {
		sprites.clear();
		if (isPlanet) {
			sprites.add(new Planet(new Vec(0, 0)));
		}
		isActive = true;
		int size = players.size();
		for (int i = 0 ; i < size ; i++) {
			Ship ship = playerAssignments.get(players.get(i));
			double theta = Math.PI * 2 * i / size;
			Vec position = new Vec(theta).mult(START_RAD);
			double theta2 = theta + Math.PI / 2;
			Vec velocity = new Vec(theta2).mult(START_SPEED);
			ship.reset(position, velocity, theta + Math.PI / 2);
			sprites.add(ship);
		}
		lastUpdated = timeStarted = System.currentTimeMillis();
	}
	
	
	public void update() {
		long now = System.currentTimeMillis();
		double dt = (now - lastUpdated) / 1000.0;

		if (isWrap) wrapAll();
		collideAll(dt);
		removeDead();
		gravitateAll();

		for (Sprite sprite : sprites) {
			sprite.update(dt);
		}
		lastUpdated = now;
		
		updates++;
	}
	
	private void wrapAll() {
		for (Sprite sprite : sprites) {
			sprite.wrapTo(ORIGINAL_BOUNDS);
		}
	}
	
	private void collideAll(double dt) {
		QuadTree<Sprite> tree = QuadTree.<Sprite>getTreeOf(sprites);
		for (Sprite sprite : sprites) {
			List<Sprite> overlappers = tree.getOverlappers(sprite);
			for (Sprite s : overlappers) {
				if (sprite.intersects(s)) {
					sprite.getHitBy(s, dt);
				}
			}
		}
		
	}
	
	private void gravitateAll() {
		if (isGravity) {
			for (Sprite a : sprites) {
				for (Sprite b : sprites) {
					if (a != b) {
						a.addForce(gForce(a, b));
					}
				}
			}
		}
	}
	
	private Vec gForce(Sprite a, Sprite b) {
		Vec diff = b.getPosition().sub(a.getPosition());
		double dist = diff.mag();
		double mag = bigG * a.getMass() * b.getMass() / (dist * dist);
		return diff.norm().mult(mag);
	}
	
	private void removeDead() {
		for (int i = sprites.size() - 1 ; i >= 0 ; i--) {
			Sprite sprite = sprites.get(i);
			if (!sprite.isAlive()) {
				sprites.remove(sprite);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		update();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(BOUNDS_COLOR);
		g.draw(ORIGINAL_BOUNDS);
		for (Sprite sprite : sprites) {
			sprite.draw(g);
		}
	}
	
	public String longString() {
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
	
	@Override
	public String toString() {
		return String.format("Game %s", name);
	}
}
