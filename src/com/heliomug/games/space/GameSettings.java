package com.heliomug.games.space;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class GameSettings implements Serializable {
	private static final long serialVersionUID = 7655574152522563536L;

	public static final double DEFAULT_WIDTH = 256;
	public static final double DEFAULT_HEIGHT = 192;
	public static final Rectangle2D ORIGINAL_BOUNDS = new Rectangle2D.Double(- DEFAULT_WIDTH / 2, - DEFAULT_HEIGHT / 2,	DEFAULT_WIDTH, DEFAULT_HEIGHT);
	public static final double BUFFER_WIDTH = 12;

	public static final long DEFAULT_BULLET_AGE_LIMIT = 3000;
	public static final long MAX_BULLET_AGE_LIMIT = 10000;

	public static final boolean DEFAULT_IS_GRAVITY = true;
	public static final int DEFAULT_BIG_G = 10000;
	public static final int MAX_BIG_G = 100000;

	public static final boolean DEFAULT_IS_PLANET = true; 
	public static final boolean DEFAULT_IS_PLANET_STATIONARY = true;
	
	public static final boolean DEFAULT_IS_SAFE_ZONE = true;
	public static final int DEFAULT_SAFE_ZONE_RADIUS = 500;
	public static final int MAX_SAFE_ZONE_RADIUS = 1500;
	
	public static final boolean DEFAULT_IS_WRAP = false;
	public static final boolean DEFAULT_IS_AUTO_RESTART = true;
	
	private boolean isAutoRestart;
	
	private boolean isPlanet;
	private boolean isPlanetStationary;

	private boolean isGravity;
	private int bigG;
	
	private boolean isBulletAgeLimit;
	private long bulletAgeLimit;

	private boolean isWrap;
	private boolean isSafeZone;
	private int safeZoneRadius;
	
	private double width;
	private double height;
	
	public GameSettings() {
		setWidth(DEFAULT_WIDTH);
		setHeight(DEFAULT_HEIGHT);
		bigG = DEFAULT_BIG_G;
		isGravity = DEFAULT_IS_GRAVITY;
		isWrap = DEFAULT_IS_WRAP;
		
		isPlanet = DEFAULT_IS_PLANET;
		isPlanetStationary = DEFAULT_IS_PLANET_STATIONARY;
		
		isAutoRestart = DEFAULT_IS_AUTO_RESTART;
		isSafeZone = DEFAULT_IS_SAFE_ZONE;
		safeZoneRadius = DEFAULT_SAFE_ZONE_RADIUS;
		//setKillZoneRatio(DEFAULT_KILL_ZONE_RATIO);
	}
	
	public boolean isWrap() {
		return this.isWrap;
	}
	
	public void setWrap(boolean b) {
		isWrap = b;
	}
	
	
	public boolean isPlanet() {
		return isPlanet;
	}
	
	public void setPlanet(boolean b) {
		isPlanet = b;
	}
	
	public boolean isGravity() {
		return isGravity;
	}
	
	public void setGravity(boolean b) {
		isGravity = b;
	}

	public boolean isAutoRestart() {
		return isAutoRestart;
	}

	public void setAutoRestart(boolean isAutoRestart) {
		this.isAutoRestart = isAutoRestart;
	}

	public int getBigG() {
		return bigG;
	}
	
	public void setBigG(int g) {
		bigG = g;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getLeft() {
		return - getWidth() / 2;
	}
	
	public double getRight() {
		return getWidth() / 2;
	}
	
	public double getTop() {
		return getHeight() / 2;
	}
	
	public double getBottom() {
		return - getHeight() / 2;
	}
	
	public double getRatio() {
		return getWidth() / getHeight();
	}
	
	public double getBufferWidth() {
		return BUFFER_WIDTH * getRatio();
	}
	
	public double getBufferHeight() {
		return BUFFER_WIDTH;
	}

	public Rectangle2D getWrapBounds() {
		return new Rectangle2D.Double(getLeft(), getBottom(), getWidth(), getHeight());
	}
	
	public boolean isPlanetStationary() {
		return isPlanetStationary;
	}

	public void setPlanetStationary(boolean isPlanetStationary) {
		this.isPlanetStationary = isPlanetStationary;
	}

	public boolean isBulletAgeLimit() {
		return isBulletAgeLimit;
	}

	public void setBulletAgeLimit(boolean isBulletAgeLimit) {
		this.isBulletAgeLimit = isBulletAgeLimit;
	}

	public long getBulletAgeLimit() {
		return bulletAgeLimit;
	}

	public void setBulletAgeLimit(long bulletAgeLimit) {
		this.isBulletAgeLimit = true;
		this.bulletAgeLimit = bulletAgeLimit;
	}

	public void setSafeZone(boolean b) {
		isSafeZone = b;
	}
	
	public boolean isSafeZone() {
		return isSafeZone;
	}
	
	public void setSafeZoneRadius(int i) {
		safeZoneRadius = i;
	}
	
	public int getSafeZoneRadius() {
		return this.safeZoneRadius;
	}
	
	public Shape getSafeZoneShape() {
		return new Ellipse2D.Double(-safeZoneRadius, -safeZoneRadius, safeZoneRadius * 2, safeZoneRadius * 2);
	}
}
