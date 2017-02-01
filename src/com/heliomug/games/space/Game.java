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

public class Game implements Serializable, ActionListener {
	private static final long serialVersionUID = 1451293503897747642L;

	private static final int FRAME_RATE = 24;

	public static final double START_RAD = 64;
	public static final double START_SPEED = 27;
	
	public static final Color BOUNDS_COLOR = Color.RED;
	public static final Color KILL_ZONE_COLOR = Color.RED;
	
	private String name;
	
	private List<Sprite> sprites; 
	
	private List<Player> players;
	
	private Map<Player, Ship> shipAssignments;
	
	private long lastUpdated;
	private long timeStarted;
	private int updates;

	private boolean isActive;
	private boolean isRoundEnded;

	private GameOptions options;
	
	public Game() {
		this("[no name]");
	}
	
	public Game(String name) {
		options = new GameOptions();
		sprites = new CopyOnWriteArrayList<>();
		players = new CopyOnWriteArrayList<>();
		shipAssignments = new ConcurrentHashMap<>();
		updates = 0;
		this.name = name;
		isActive = false;
		isRoundEnded = true;
		
		Timer timer = new Timer(1000/FRAME_RATE, this);
		timer.start();
	}

	public GameOptions getOptions() {
		return options;
	}
	
	public boolean isActive() {
		return this.isActive;
	}
	
	public String getName() {
		return name;
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
		double minX = options.getLeft();
		double maxX = options.getRight();
		double minY = options.getBottom();
		double maxY = options.getTop();
		for (Ship ship : shipAssignments.values()) {
			if (ship.isAlive()) {
				double x = ship.getPosition().getX();
				double y = ship.getPosition().getY();
				if (x < minX) minX = x;
				if (y < minY) minY = y;
				if (x > maxX) maxX = x;
				if (y > maxY) maxY = y;
			}
		}
		
		double w = options.getBufferWidth();
		double h = options.getBufferHeight();
		
		return new Rectangle2D.Double(minX - w, minY - h, maxX - minX + w * 2, maxY - minY + w * 2);
	}
	
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		shipAssignments.put(player, new Ship(player));
	}

	public void removePlayer(Player player) {
		Ship ship = shipAssignments.remove(player);
		players.remove(player);
		sprites.remove(ship);
	}
	
	public void handleShipSignal(Player player, ShipSignal signal) {
		Ship ship = shipAssignments.get(player);
		if (signal == ShipSignal.TURN_LEFT) {
			ship.setTurnDirection(TurnDirection.LEFT);
		} else if (signal == ShipSignal.TURN_RIGHT) {
			ship.setTurnDirection(TurnDirection.RIGHT);
		} else if (signal == ShipSignal.TURN_NONE) {
			ship.setTurnDirection(TurnDirection.NONE);
		} else if (signal == ShipSignal.FORWARD) {
			ship.setAccel(1);
		} else if (signal == ShipSignal.BACKWARDS) {
			ship.setAccel(-1);
		} else if (signal == ShipSignal.ACCEL_OFF) {
			ship.setAccel(0);
		} else if (signal == ShipSignal.FIRE) {
			Bullet bullet = ship.getBullet();
			if (bullet != null) {
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
		if (options.isPlanet()) {
			sprites.add(new Planet(new Vec(0, 0), options.isPlanetStationary()));
		}
		isActive = true;
		isRoundEnded = false;
		int size = players.size();
		for (int i = 0 ; i < size ; i++) {
			Ship ship = shipAssignments.get(players.get(i));
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
		if (isActive) {
			long now = System.currentTimeMillis();
			double dt = (now - lastUpdated) / 1000.0;
	
			if (options.isWrap()) {
				wrapAll();
			}
			
			if (options.isKillZone()) {
				killZoneAll();
			}
			
			collideAll(dt);
			
			removeDead();
			
			if (options.isGravity()) {
				gravitateAll();
			}
			
			for (Sprite sprite : sprites) {
				sprite.update(dt);
			}
			
			if (isRoundOver()) {
				endRound();
				if (options.isAutoRestart()) {
					isActive = false;
					reset();
				}
			}
			
			lastUpdated = now;
			
			updates++;
		}
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
	
	private void killZoneAll() {
		Rectangle2D killZone = options.getKillBounds();
		for (Sprite sprite : sprites) {
			if (!sprite.getBounds().intersects(killZone)) {
				sprite.die();
			}
		}
	}
	
	private void wrapAll() {
		for (Sprite sprite : sprites) {
			sprite.wrapTo(options.getWrapBounds());
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
	
	private Vec gForce(Sprite a, Sprite b) {
		Vec diff = b.getPosition().sub(a.getPosition());
		double dist = diff.mag();
		double mag = options.getBigG() * a.getMass() * b.getMass() / (dist * dist);
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
		for (Ship ship : shipAssignments.values()) {
			if (ship.isAlive()) {
				tot++;
			}
		}
		return tot;
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
		g.draw(options.getWrapBounds());
		if (options.isKillZone()) {
			g.setColor(KILL_ZONE_COLOR);
			g.draw(options.getKillBounds());
		}
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
