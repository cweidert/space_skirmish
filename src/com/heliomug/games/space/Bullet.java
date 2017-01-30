package com.heliomug.games.space;

public class Bullet extends Sprite {
	private static final long serialVersionUID = -7116875414850194040L;

	private static final double BULLET_RADIUS = .5;
	private static final double BULLET_MASS = .01;
	
	public Bullet(Vec position, Vec velocity) {
		super(position, velocity, BULLET_RADIUS, BULLET_MASS);
	}
}
