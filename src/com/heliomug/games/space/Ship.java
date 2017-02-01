package com.heliomug.games.space;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.Serializable;

public class Ship extends Sprite implements Serializable {
	private static final long serialVersionUID = 317323665656103453L;

	private static final double MAX_SPEED = 150.0;
	private static final double TURN_SPEED = 2.0;
	
	private static final double TUR_RAD = 5;
	private static final double SHIP_RAD = 3;
	//private static final Color TUR_COLOR = Color.YELLOW;
	private static final double BOOST_FORCE = 100;
	private static final double BOOST_RAD = 3;
	private static final Color BOOST_COLOR = new Color(255, 127, 0);
	
	private static final double BULLET_SPEED = 75;

	private static final double STARTING_HEALTH = 100;
	private static final long BLINK_CYCLE = 1000;
	private static final int BLINK_THRESHOLD = 25;
	
	private static final double DEFAULT_HEADING = 0;
	private static final Vec DEFAULT_POSITION = new Vec();
	private static final Vec DEFAULT_VELOCITY = new Vec();
	
	private TurnDirection turnDirection;
	private double heading;
	
	private boolean accelerating; 

	private double health;
	private Color color;
	
	
	public Ship(Player player) {
		super(SHIP_RAD);
		heading = DEFAULT_HEADING;
		this.color = player.getColor();
		reset(DEFAULT_POSITION, heading);
	}

	public void reset(Vec pos, double heading) {
		reset(pos, DEFAULT_VELOCITY, heading);
	}
	
	public void reset(Vec pos, Vec velo, double heading) {
		setPosition(pos);
		setVelocity(velo);
		setAlive(true);
		this.heading = heading;
		turnDirection = TurnDirection.NONE;
		accelerating = false;
		health = STARTING_HEALTH;
	}
	
	public double getHeading() {
		return heading;
	}

	public Bullet getBullet() {
		if (isAlive()) {
			Vec position = getPosition().add(new Vec(heading).mult(SHIP_RAD + TUR_RAD));
			Vec velocity = getVelocity().add(new Vec(heading).mult(BULLET_SPEED));
			Bullet bullet = new Bullet(position, velocity);
			setVelocity(getVelocity().sub(velocity.mult(bullet.getMass()))); 
			return bullet;
		}
		return null;
	}
	
	public void setTurnDirection(TurnDirection dir) {
		turnDirection = dir;
	}
	
	public void setBoostOn(boolean b) {
		accelerating = b;
	}

	public void update(double dt) {
		heading += turnDirection.getValue() * TURN_SPEED * dt;
		if (accelerating) {
			addForce(new Vec(heading).mult(BOOST_FORCE));
		}
		super.update(dt);
		if (getSpeed() > MAX_SPEED) {
			setSpeed(MAX_SPEED);
		}
	}

	@Override
	public void getHitBy(Sprite other, double dt) {
		if (other instanceof Ship) {
			die();
		} else if (other instanceof Bullet) {
			Bullet b = (Bullet) other;
			health -= b.getPunch();
		}
		if (health < 0) {
			health = 0;
			die();
		}
	}
	
	@Override
	public Color getColor() {
		if (health < BLINK_THRESHOLD) {
			if (age() % BLINK_CYCLE < BLINK_CYCLE / 2) {
				return Color.BLACK;
			} else {
				return Color.RED;
			}
		} else {
			double h = health / STARTING_HEALTH;
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			r = r + (int)((1 - h) * (255 - r));
			g = (int)(g * h);
			b = (int)(b * h);
			return new Color(r, g, b);
		}
	}
	
	public void drawFancy(Graphics2D g) {
		double r = getRadius();
		double r2 = r / 2;
		Vec position = getPosition();
		double x = position.getX();
		double y = position.getY();
		
		Path2D shape = new Path2D.Double();
		shape.moveTo(x, y);
		shape.lineTo(x + Math.cos(heading + Math.PI * 2 / 3) * r, y + Math.sin(heading + Math.PI * 2 / 3) * r);
		shape.lineTo(x + Math.cos(heading) * r, y + Math.sin(heading) * r);
		shape.lineTo(x + Math.cos(heading - Math.PI * 2 / 3) * r, y + Math.sin(heading - Math.PI * 2 / 3) * r);
		shape.lineTo(x, y);
		g.setColor(getColor());
		g.fill(shape);

		
		Path2D cockpit = new Path2D.Double();
		cockpit.moveTo(x + Math.cos(heading) * r, y + Math.sin(heading) * r);
		cockpit.lineTo(x + Math.cos(heading - Math.PI / 3) * r2, y + Math.sin(heading - Math.PI / 3) * r2);
		cockpit.lineTo(x + Math.cos(heading + Math.PI / 3) * r2, y + Math.sin(heading + Math.PI / 3) * r2);
		cockpit.lineTo(x + Math.cos(heading) * r, y + Math.sin(heading) * r);
		g.setColor(Color.WHITE);
		g.fill(cockpit);
		
		g.setColor(Color.BLACK);
		g.fill(new Ellipse2D.Double(x + Math.cos(heading) * r2 - r / 6, y + Math.sin(heading) * r2 - r / 6, r / 3, r / 3));
	}
	
	@Override
	public void draw(Graphics2D g) {
		double x = getPosition().getX();
		double y = getPosition().getY();
		//g.setColor(TUR_COLOR);
		//g.setStroke(new BasicStroke(1));
		//g.draw(new Line2D.Double(x, y, x + Math.cos(heading) * TUR_RAD, y + Math.sin(heading) * TUR_RAD));
		if (accelerating) {
			g.setColor(BOOST_COLOR);
			g.setStroke(new BasicStroke(2));
			g.draw(new Line2D.Double(x, y, x - Math.cos(heading) * BOOST_RAD, y - Math.sin(heading) * BOOST_RAD));
		}

		drawFancy(g);
	}
}
