package com.heliomug.games.space;

import java.awt.Color;

public class Bullet extends Sprite {
	private static final long serialVersionUID = -7116875414850194040L;

	private static final double BULLET_RADIUS = 1;
	private static final double BULLET_MASS = .01;
	
	private static final double DEFAULT_PUNCH = 10;
	
	private double punch;
	
	public Bullet(Vec position, Vec velocity) {
		super(position, velocity, BULLET_RADIUS, BULLET_MASS);
		punch = DEFAULT_PUNCH;
	}
	
	public double getPunch() {
		return this.punch;
	}
	
	@Override
	public Color getColor() {
		int green = (int) Math.max(0, 255 - 3 * (punch - 10));
		return new Color(255, green, 0);
	}
	
	@Override
	public void getHitBy(Sprite other, double dt) {
		if (other instanceof Bullet) {
			Bullet bullet = (Bullet) other;
			if (isAlive() && bullet.isAlive()) {
				bullet.die();
				double totalMass = getMass() + bullet.getMass();
				this.setRadius(Math.sqrt(getRadius()*getRadius() + bullet.getRadius()*bullet.getRadius()));
				this.setVelocity(getVelocity().mult(getMass()).add(bullet.getVelocity().mult(bullet.getMass())).div(totalMass));
				this.setPosition(getPosition().mult(getMass()).add(bullet.getPosition().mult(bullet.getMass())).div(totalMass));
				this.setMass(totalMass);
				punch += bullet.punch;
			} 
		} else {
			super.getHitBy(other, dt);
		}
	}
}
