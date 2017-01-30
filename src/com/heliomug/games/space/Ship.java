package com.heliomug.games.space;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.io.Serializable;

public class Ship extends Sprite implements Serializable {
	private static final long serialVersionUID = 317323665656103453L;

	private static final double ACCEL = 10.0;
	private static final double MAX_SPEED = 100.0;
	private static final double TURN_SPEED = 2.0;
	
	private static final double TUR_RAD = 5;
	private static final double SHIP_RAD = 2;
	private static final Color TUR_COLOR = Color.YELLOW;
	private static final double BULLET_SPEED = 25;
	private static final double BOOST_RAD = 5;
	private static final Color BOOST_COLOR = new Color(255, 127, 0);
	private static final Color DEFAULT_COLOR = Color.BLUE;
	
	private static final double STARTING_HEALTH = 100;
	
	private TurnDirection turnDirection;
	private double heading;
	
	private boolean accelerating; 

	private double health;
	private Color color;
	
	private SpaceGame game;
	
	public Ship(SpaceGame game) {
		this(game, DEFAULT_COLOR);
	}
	
	public Ship(SpaceGame game, Color color) {
		super(0, 0, 0, 0, SHIP_RAD);
		this.color = color;
		turnDirection = TurnDirection.NONE;
		heading = 0;
		accelerating = false;
		health = STARTING_HEALTH;
		this.game = game;
	}
	
	public double getHeading() {
		return heading;
	}

	public void fire() {
		double bx = getX() + Math.cos(heading) * (SHIP_RAD + TUR_RAD);
		double by = getY() + Math.sin(heading) * (SHIP_RAD + TUR_RAD);
		double dx = getDX() + Math.cos(heading) * BULLET_SPEED;
		double dy = getDY() + Math.sin(heading) * BULLET_SPEED;
		Bullet bullet = new Bullet(bx, by, dx, dy);
		
		game.addBullet(bullet);
	}
	
	public void setTurnDirection(TurnDirection dir) {
		turnDirection = dir;
	}
	
	public void setAccel(boolean b) {
		accelerating = b;
	}

	public void update(double dt) {
		heading += turnDirection.getValue() * TURN_SPEED * dt;
		if (accelerating) {
			double ddx = Math.cos(heading) * ACCEL * dt;
			double ddy = Math.sin(heading) * ACCEL * dt;
			accelerate(ddx, ddy);
		}
		if (getSpeed() > MAX_SPEED) {
			setSpeed(MAX_SPEED);
		}
		super.update(dt);
	}

	private Color getColor() {
		double h = health / STARTING_HEALTH;
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return new Color((int)((1 - h) * (255 - r) + r), g, (int)(h * (255 - b) + b));
	}
	
	public void draw(java.awt.Graphics2D g) {
		double x = getX();
		double y = getY();
		g.setColor(TUR_COLOR);
		g.draw(new Line2D.Double(x, y, x + Math.cos(heading) * TUR_RAD, y + Math.sin(heading) * TUR_RAD));
		if (accelerating) {
			g.setColor(BOOST_COLOR);
			g.draw(new Line2D.Double(x, y, x - Math.cos(heading) * BOOST_RAD, y - Math.sin(heading) * BOOST_RAD));
		}
		super.draw(g, getColor());
	}
}
