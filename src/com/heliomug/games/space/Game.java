package com.heliomug.games.space;

import java.awt.BasicStroke;
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

public class Game implements Serializable, ActionListener {
	private static final long serialVersionUID = 1451293503897747642L;

	private static final int FRAME_RATE = 24;

	public static final double START_RAD = 64;
	public static final double START_SPEED = 27;
	
	public static final Color BOUNDS_COLOR = Color.RED;
	public static final Color SAFE_ZONE_COLOR = Color.RED;
	public static final int SAFE_ZONE_BORDER_WIDTH = 10;
	
	private List<Sprite> sprites; 
	
	private List<Player> players;
	
	private Map<Player, Vehicle> shipAssignments;
	
	private long lastUpdated;
	private long timeStarted;
	private int updates;

	private boolean isActive;
	private boolean isRoundEnded;

	private GameSettings settings;
	
	public Game() {
		settings = new GameSettings();
		sprites = new CopyOnWriteArrayList<>();
		players = new CopyOnWriteArrayList<>();
		shipAssignments = new ConcurrentHashMap<>();
		updates = 0;
		isActive = false;
		isRoundEnded = true;
		
		Timer timer = new Timer(1000/FRAME_RATE, this);
		timer.start();
	}

	public GameSettings getSettings() {
		return settings;
	}
	
	public boolean isActive() {
		return this.isActive;
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
		double minX = settings.getLeft();
		double maxX = settings.getRight();
		double minY = settings.getBottom();
		double maxY = settings.getTop();
		for (Vehicle ship : shipAssignments.values()) {
			if (ship.isAlive()) {
				double x = ship.getPosition().getX();
				double y = ship.getPosition().getY();
				if (x < minX) minX = x;
				if (y < minY) minY = y;
				if (x > maxX) maxX = x;
				if (y > maxY) maxY = y;
			}
		}
		
		double w = settings.getBufferWidth();
		double h = settings.getBufferHeight();
		
		return new Rectangle2D.Double(minX - w, minY - h, maxX - minX + w * 2, maxY - minY + w * 2);
	}
	
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		if (!players.contains(player)) {
			players.add(player);
			shipAssignments.put(player, new Vehicle(player));
			reset();
		}
	}

	public void removePlayer(Player player) {
		if (players.contains(player)) {
			Vehicle ship = shipAssignments.remove(player);
			players.remove(player);
			sprites.remove(ship);
			reset();
		}
	}
	
	public void handleShipSignal(Player player, VehicleSignal signal) {
		Vehicle ship = shipAssignments.get(player);
		if (signal == VehicleSignal.TURN_LEFT) {
			ship.setTurnDirection(TurnDirection.LEFT);
		} else if (signal == VehicleSignal.TURN_RIGHT) {
			ship.setTurnDirection(TurnDirection.RIGHT);
		} else if (signal == VehicleSignal.TURN_NONE) {
			ship.setTurnDirection(TurnDirection.NONE);
		} else if (signal == VehicleSignal.FORWARD) {
			ship.setAccel(1);
		} else if (signal == VehicleSignal.BACKWARDS) {
			ship.setAccel(-1);
		} else if (signal == VehicleSignal.ACCEL_OFF) {
			ship.setAccel(0);
		} else if (signal == VehicleSignal.FIRE) {
			Bullet bullet = ship.getBullet();
			if (bullet != null) {
				if (settings.isBulletAgeLimit()) {
					bullet.setAgeLimit(settings.getBulletAgeLimit());
				}
				sprites.add(bullet);
			}
		}
	}
	
	
	public void start() {
		reset();
	}
	
	public void reset() {
		isActive = true;
		
		sprites.clear();
		if (settings.isPlanet()) {
			sprites.add(new Planet(new Vec(0, 0), settings.isPlanetStationary()));
		}
		isActive = true;
		isRoundEnded = false;
		int size = players.size();
		for (int i = 0 ; i < size ; i++) {
			Vehicle ship = shipAssignments.get(players.get(i));
			double theta = Math.PI * 2 * i / size;
			Vec position = new Vec(theta).mult(START_RAD);
			double theta2 = theta + Math.PI / 2;
			Vec velocity = new Vec(theta2).mult(START_SPEED);
			ship.reset(position, velocity, theta + Math.PI / 2, settings.isTankMode());
			sprites.add(ship);
		}
		lastUpdated = timeStarted = System.currentTimeMillis();
	}
	
	private void endRound() {
		if (!isRoundEnded) {
			Player winner = getWinner();
			if (winner != null) {
				winner.addWin();
			}
			isRoundEnded = true;
		}
	}
	
	
	public void update() {
		if (isActive) {
			long now = System.currentTimeMillis();
			double dt = (now - lastUpdated) / 1000.0;
	
			if (settings.isWrap()) {
				wrapAll();
			}
			
			if (settings.isSafeZone()) {
				safeZoneAll();
			}
			
			collideAll(dt);
			
			removeDead();
			
			if (settings.isGravity()) {
				gravitateAll();
			}
			
			for (Sprite sprite : sprites) {
				sprite.update(dt);
			}
			
			if (isRoundOver()) {
				endRound();
				if (settings.isAutoRestart()) {
					isActive = false;
					reset();
				}
			}
			
			lastUpdated = now;
			
			updates++;
		}
	}

	
	private void safeZoneAll() {
		double safeRadius = settings.getSafeZoneRadius();
		for (Sprite sprite : sprites) {
			// center at zero;
			if (sprite.getPosition().mag() > safeRadius) {
				sprite.setSafe(false);
			} else {
				sprite.setSafe(true);
			}
		}
	}
	
	private void wrapAll() {
		for (Sprite sprite : sprites) {
			sprite.wrapTo(settings.getWrapBounds());
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
		for (Sprite a : sprites) {
			for (Sprite b : sprites) {
				if (a != b) {
					a.addForce(gForce(a, b));
				}
			}
		}
	}
	
	private void removeDead() {
		for (int i = sprites.size() - 1 ; i >= 0 ; i--) {
			Sprite sprite = sprites.get(i);
			if (!sprite.isAlive()) {
				sprites.remove(sprite);
			}
		}
	}
	
	private Player getWinner() {
		if (players.size() < 2) {
			return null;
		}
		
		int count = 0;
		Player winner = null;
		for (Player player : players) {
			if (shipAssignments.get(player).isAlive()) {
				count++;
				winner = player;
			}
		}
		if (count == 1) {
			return winner;
		} 
		
		return null;
	}
	
	public boolean allDead() {
		return numberOfLivingPlayers() == 0;
	}
	
	
	public int numberOfPlayers() {
		return players.size();
	}

	
	private int numberOfLivingPlayers() {
		int tot = 0;
		for (Vehicle ship : shipAssignments.values()) {
			if (ship.isAlive()) {
				tot++;
			}
		}
		return tot;
	}
	
	private Vec gForce(Sprite a, Sprite b) {
		Vec diff = b.getPosition().sub(a.getPosition());
		double dist = diff.mag();
		double mag = settings.getBigG() * a.getMass() * b.getMass() / (dist * dist);
		return diff.norm().mult(mag);
	}
	
	private boolean isRoundOver() {
		if (players.size() == 0) {
			return false;
		} else if (players.size() == 1) {
			return numberOfLivingPlayers() == 0;
		} else {
			return numberOfLivingPlayers() < 2;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		update();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(BOUNDS_COLOR);
		g.draw(settings.getWrapBounds());

		for (Sprite sprite : sprites) {
			sprite.draw(g);
		}

		if (settings.isSafeZone()) {
			g.setStroke(new BasicStroke(SAFE_ZONE_BORDER_WIDTH));
			g.setColor(SAFE_ZONE_COLOR);
			g.draw(settings.getSafeZoneShape());
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
		return "Game with " + players.size() + " players";
	}
}
