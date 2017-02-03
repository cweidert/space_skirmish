package com.heliomug.games.space;

import java.awt.Color;

class Planet extends Sprite {
	private static final long serialVersionUID = 1328500729510526414L;

	private static final double DEFAULT_RADIUS = 8.0;
	private static final double DEFAULT_MASS = 5.0;
	
	public Planet(Vec position, boolean isStationary) {
		super(position, new Vec(), DEFAULT_RADIUS, DEFAULT_MASS);
		setStationary(isStationary);
	}

	@Override
	public Color getColor() {
		return Color.GREEN;
	}
	
	@Override
	public void getHitBy(Sprite sprite, double dt) {
		sprite.die();
		setMass(sprite.getMass() + getMass());
	}
}
