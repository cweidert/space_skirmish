package com.heliomug.games.space;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Vec implements Serializable {
	private static final long serialVersionUID = 3483426213136510850L;

	private double x, y;
	
	public Vec(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec(double theta) {
		this(Math.cos(theta), Math.sin(theta));
	}
	
	public Vec() {
		this(0, 0);
	}
	
	public Vec norm() {
		return div(mag());
	}
	
	public Vec add(Vec other) {
		return new Vec(x + other.x, y + other.y);
	}
	
	public Vec sub(Vec other) {
		return new Vec(x - other.x, y - other.y);
	}
	
	public Vec mult(double s) {
		return new Vec(x * s, y * s);
	}
	
	public Vec div(double d) {
		return mult(1 / d);
	}
	
	public double mag() {
		return Math.sqrt(x * x + y * y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public Vec wrapTo(Rectangle2D bounds) {
		double xMin = bounds.getMinX();
		double xMax = bounds.getMaxX();
		double yMin = bounds.getMinY();
		double yMax = bounds.getMaxY();
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		double x = this.x;
		double y = this.y;
		while (x < xMin) x += width;
		while (x > xMax) x -= width;
		while (y < yMin) y += height;
		while (y > yMax) y -= height;
		return new Vec(x, y);
	}

	public String toString() {
		return String.format("(%.3f, %.3f)", x, y);
	}
}
