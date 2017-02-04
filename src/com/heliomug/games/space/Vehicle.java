package com.heliomug.games.space;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.Serializable;

class Vehicle extends Sprite implements Serializable {
	private static final long serialVersionUID = 317323665656103453L;

	private static final double MAX_SPEED = 150.0;
	private static final double TURN_SPEED = Math.PI;
	
	private static final double TUR_RADIUS = 7;
	private static final float TURRET_WIDTH = 2;
	private static final Color TURRET_COLOR = Color.YELLOW;
	private static final double SHIP_RAD = 5;

	private static final double SHIP_BOOST_FORCE = 100;
	private static final double TANK_BOOST_FORCE = 1000;
	private static final double BOOST_RAD = 5;
	private static final float BOOST_WIDTH = 4;
	private static final Color BOOST_COLOR = Color.ORANGE;
	
	private static final double BULLET_SPEED = 75;

	private static final double STARTING_HEALTH = 100;
	private static final long BLINK_CYCLE = 1000;
	private static final int BLINK_THRESHOLD = 25;
	private static final double UNSAFE_DEATH_RATE = STARTING_HEALTH / 2;
	
	private static final boolean DEFAULT_IS_TANK = false;

	private static final double DEFAULT_HEADING = 0;
	private static final Vec DEFAULT_POSITION = new Vec();
	private static final Vec DEFAULT_VELOCITY = new Vec();
	
	private TurnDirection turnDirection;
	private double heading;
	
	private int accel; 

	private double health;
	private Color color;
	
	private boolean isTank;
	
	public Vehicle(Player player) {
		super(SHIP_RAD);
		heading = DEFAULT_HEADING;
		this.color = player.getColor();
		this.accel = 0;
		this.isTank = DEFAULT_IS_TANK;
		reset(DEFAULT_POSITION, heading);
	}

	public void reset(Vec pos, double heading) {
		reset(pos, DEFAULT_VELOCITY, heading);
	}
	
	public void reset(Vec pos, Vec velo, double heading) {
		reset(pos, velo, heading, DEFAULT_IS_TANK);
	}
	
	public void reset(Vec pos, Vec velo, double heading, boolean isTank) {
		this.isTank = isTank;
		setPosition(pos);
		setVelocity(velo);
		setAlive(true);
		this.heading = heading;
		turnDirection = TurnDirection.NONE;
		accel = 0;
		health = STARTING_HEALTH;
	}
	
	public void setTank(boolean b) {
		isTank = b;
	}
	
	public double getHeading() {
		return heading;
	}

	public Bullet getBullet() {
		if (isAlive()) {
			Vec position = getPosition().add(new Vec(heading).mult(SHIP_RAD + TUR_RADIUS));
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
	
	public void setAccel(int i) {
		accel = i;
	}

	public void update(double dt) {
		if (!isSafe()) {
			health -= dt * UNSAFE_DEATH_RATE;
		}
		heading += turnDirection.getValue() * TURN_SPEED * dt;
		if (accel != 0) {
			if (isTank) {
				addForce(new Vec(heading).mult(TANK_BOOST_FORCE * accel));
			} else {
				addForce(new Vec(heading).mult(SHIP_BOOST_FORCE * accel));
			}
		}
		super.update(dt);
		if (getSpeed() > MAX_SPEED) {
			setSpeed(MAX_SPEED);
		}
		if (health < 0) {
			die();
		}
		if (isTank) {
			setVelocity(new Vec());
		}
	}

	@Override
	public void getHitBy(Sprite other, double dt) {
		if (other instanceof Vehicle) {
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
			if (getAge() % BLINK_CYCLE < BLINK_CYCLE / 2) {
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
	
	public void drawTank(Graphics2D g) {
		double r = getRadius();
		Vec pos = getPosition();
		double x = pos.getX();
		double y = pos.getY();
		double a = Math.cos(heading + Math.PI / 4) * r;
		double b = Math.sin(heading + Math.PI / 4) * r;
		Path2D shape = new Path2D.Double();
		shape.moveTo(x + a, y + b);
		shape.lineTo(x - b, y + a);
		shape.lineTo(x - a, y - b);
		shape.lineTo(x + b, y - a);
		shape.lineTo(x + a, y + b);
		g.setColor(getColor());
		g.fill(shape);
		g.setColor(TURRET_COLOR);
		g.setStroke(new BasicStroke(TURRET_WIDTH));
		g.draw(new Line2D.Double(x, y, x + r * Math.cos(heading), y + r * Math.sin(heading)));
		
	}
	
	public void drawShip(Graphics2D g) {
		double r = getRadius();
		double r2 = r / 2;
		double x = getPosition().getX();
		double y = getPosition().getY();
		
		if (accel != 0) {
			g.setColor(BOOST_COLOR);
			g.setStroke(new BasicStroke(BOOST_WIDTH));
			g.draw(new Line2D.Double(x, y, x - accel * Math.cos(heading) * BOOST_RAD, y - accel * Math.sin(heading) * BOOST_RAD));
		}
		
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
		if (isTank) {
			drawTank(g);
		} else {
			drawShip(g);
		}
	}
}
