package com.heliomug.games.space;

public class Bullet extends Sprite {
	private static final long serialVersionUID = -7116875414850194040L;

	private static final double BULLET_RADIUS = .5;
	
	public Bullet(double x, double y, double dx, double dy) {
		super(x, y, dx, dy, BULLET_RADIUS);
	}
}
