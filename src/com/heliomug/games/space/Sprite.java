package com.heliomug.games.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class Sprite implements Serializable {
	private static final long serialVersionUID = -6488981465597378521L;

	private static final double DEFAULT_RADIUS = 2;
	private static final double DEFAULT_MASS = 1;
	
	private double x, y;
	private double dx, dy;

	private double r;
	private double mass;
	
	public Sprite() {
		this(0, 0);
	}
	
	public Sprite(double x, double y) {
		this(x, y, -5, 0, DEFAULT_RADIUS);
	}
	
	public Sprite(double x, double y, double dx, double dy, double rad) {
		this(x, y, dx, dy, rad, DEFAULT_MASS);
	}
		
	public Sprite(double x, double y, double dx, double dy, double rad, double mass) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		r = rad;
		this.mass = mass;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getDX() {
		return dx;
	}
	
	public double getDY() {
		return dy;
	}
	
	public double getMass() {
		return mass; 
	}
	
	public double getSpeed() {
		return Math.sqrt(dx * dx + dy * dy);
	}

	public boolean intersects(Sprite other) {
		double minDist = other.r + this.r;
		double distSq = 0; 
		distSq += (other.x - this.x) * (other.x - this.x);
		distSq += (other.y - this.y) * (other.y - this.y);
		return distSq < minDist * minDist;
	}
	
	
	public void setVelocity(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void accelerate(double ddx, double ddy) {
		dx += ddx;
		dy += ddy;
	}
	
	public void setSpeed(double newSpeed) {
		double speed = getSpeed();
		dx *= newSpeed / speed;
		dy *= newSpeed / speed;
	}
	
	public void update(double dt) {
		x += dx * dt;
		y += dy * dt;
	}
	
	public void draw(Graphics2D g) {
		draw(g, Color.RED);
	}
	
	public void draw(Graphics2D g, Color c) {
		g.setColor(c);
		g.fill(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
	}
	
	public String toString() {
		return String.format("Sprite @ (%.3f, %.3f)", x, y);
	}
}