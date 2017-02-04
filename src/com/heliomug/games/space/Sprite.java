package com.heliomug.games.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import com.heliomug.utils.games.Boundable;

class Sprite implements Boundable, Serializable {
	private static final long serialVersionUID = -6488981465597378521L;

	private static final Color DEFAULT_COLOR = Color.RED;
	
	private static final Vec DEFAULT_POSITION = new Vec();
	private static final Vec DEFAULT_VELOCITY = new Vec();
	
	private static final double DEFAULT_RADIUS = 2;
	private static final double DEFAULT_MASS = 1;
	private static final boolean DEFAULT_IS_STATIONARY = false;	
	
	private Vec position; 
	private Vec velocity;
	private Vec sumForce;
	
	private double r;
	private double mass;
	
	private boolean isAlive;
	private boolean isStationary;	
	private boolean isSafe;
	
	private boolean isAgeLimit;
	private long ageLimit;
	private long birthday;
	
	public Sprite() {
		this(DEFAULT_POSITION);
	}

	public Sprite(double rad) {
		this(DEFAULT_POSITION, DEFAULT_VELOCITY, rad, DEFAULT_MASS);
	}
	
	public Sprite(Vec position) {
		this(position, DEFAULT_VELOCITY, DEFAULT_RADIUS);
	}
	
	public Sprite(Vec position, Vec velocity, double rad) {
		this(position, velocity, rad, DEFAULT_MASS);
	}
		
	public Sprite(Vec position, Vec velocity, double rad, double mass) {
		this.position = position;
		this.velocity = velocity;
		this.r = rad;
		this.mass = mass;
		this.isAlive = true;
		this.isSafe = true;
		this.sumForce = new Vec();
		this.isStationary = DEFAULT_IS_STATIONARY;
		this.isAgeLimit = false;
		this.ageLimit = 0;
		this.birthday = System.currentTimeMillis();
	}
	
	public void setSafe(boolean b) {
		isSafe = b;
	}
	
	public boolean isSafe() {
		return isSafe;
	}
	
	public long getAge() {
		return System.currentTimeMillis() - birthday;
	}
	
	public Vec getPosition() {
		return position;
	}
	
	public Vec getVelocity() {
		return velocity;
	}
	
	public double getMass() {
		return mass; 
	}
	
	public void setMass(double m) {
		mass = m;
	}

	public double getRadius() {
		return r;
	}
	
	public void setRadius(double r) {
		this.r = r;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void setAlive(boolean b) {
		isAlive = b;
	}
	
	public double getSpeed() {
		return velocity.mag();
	}

	@Override
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(position.getX() - r, position.getY() - r, r * 2, r * 2);
	}
	
	public boolean intersects(Sprite other) {
		return position.sub(other.position).mag() < r + other.r;
	}
	
	public void setStationary(boolean b) {
		this.isStationary = b;
	}
	
	public void setVelocity(Vec velocity) {
		this.velocity = velocity;
	}
	
	public void setPosition(Vec position) {
		this.position = position;
	}

	public void setSpeed(double newSpeed) {
		velocity = velocity.norm().mult(newSpeed);
	}
	
	public void addForce(Vec force) {
		sumForce = sumForce.add(force);
	}
	
	public void setAgeLimit(long age) {
		isAgeLimit = true;
		ageLimit = age;
	}
	
	public void update(double dt) {
		if (isAgeLimit) {
			if (getAge() > ageLimit) {
				die();
			}
		}
		if (!isStationary) {
			double scalar = dt / mass;
			velocity = velocity.add(sumForce.mult(scalar));
			position = position.add(velocity.mult(dt));
		}
		sumForce = new Vec();
	}
	
	public void dieIfOutSide(Rectangle2D bounds) {
		if (!getBounds().intersects(bounds)) {
			die();
		}
	}
	
	public void wrapTo(Rectangle2D bounds) {
		position = position.wrapTo(bounds);
	}
	
	public void getHitBy(Sprite other, double dt) {
		die();
	}
	
	public void die() {
		isAlive = false;
	}
	
	public Color getColor() {
		return DEFAULT_COLOR;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(getColor());
		g.fill(new Ellipse2D.Double(position.getX() - r, position.getY() - r, 2 * r, 2 * r));
	}
	
	public String toString() {
		return String.format("Sprite @ %s", velocity);
	}
}