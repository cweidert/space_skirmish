package com.heliomug.games.space;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class GameOptions implements Serializable {
	private static final long serialVersionUID = 7655574152522563536L;

	public static final double DEFAULT_WIDTH = 192;
	public static final double DEFAULT_HEIGHT = 144;
	public static final Rectangle2D ORIGINAL_BOUNDS = new Rectangle2D.Double(- DEFAULT_WIDTH / 2, - DEFAULT_HEIGHT / 2,	DEFAULT_WIDTH, DEFAULT_HEIGHT);
	public static final double BUFFER_WIDTH = 6;

	public static final double DEFAULT_BIG_G = 10000;
	public static final boolean DEFAULT_IS_WRAP = true;
	public static final boolean DEFAULT_IS_PLANET = true; 
	public static final boolean DEFAULT_IS_GRAVITY = true;
	public static final boolean DEFAULT_IS_AUTO_RESTART = true;
	
	private boolean isAutoRestart;
	private boolean isWrap;
	private boolean isPlanet;
	private boolean isGravity;
	private double bigG;
	private double width;
	private double height;
	
	public GameOptions() {
		isGravity = DEFAULT_IS_GRAVITY;
		isWrap = DEFAULT_IS_WRAP;
		isPlanet = DEFAULT_IS_PLANET;
		isAutoRestart = DEFAULT_IS_AUTO_RESTART;
		bigG = DEFAULT_BIG_G;
		setWidth(DEFAULT_WIDTH);
		setHeight(DEFAULT_HEIGHT);
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

	public double getBigG() {
		return bigG;
	}
	
	public void setBigG(double g) {
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

	public Rectangle2D getOriginalBounds() {
		return ORIGINAL_BOUNDS;
	}
}
