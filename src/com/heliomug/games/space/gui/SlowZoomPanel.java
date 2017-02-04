package com.heliomug.games.space.gui;

import com.heliomug.utils.gui.WeidertPanel;

@SuppressWarnings("serial")
class SlowZoomPanel extends WeidertPanel {
	private static final boolean DEFAULT_IS_SLOW_ZOOM = true;
	
	private static final double DEFAULT_ZOOM_LIMIT = 200;
	

	private double goalLeft, goalRight, goalBottom, goalTop;
	private double zoomLimit;
	private boolean isSlowZoom;

	private long lastUpdated;

	public SlowZoomPanel(int pixelWidth, int pixelHeight) {
		this(pixelWidth, pixelHeight, -10, 10, -10, 10);
	}
		
	public SlowZoomPanel(int pixelWidth, int pixelHeight, double left, double right, double bottom, double top) {
		super(pixelWidth, pixelHeight, left, right, bottom, top);
		this.goalLeft = left;
		this.goalRight = right;
		this.goalTop = top;
		this.goalBottom = bottom;
		
		this.lastUpdated = System.currentTimeMillis();
		
		this.isSlowZoom = DEFAULT_IS_SLOW_ZOOM;
		this.zoomLimit = DEFAULT_ZOOM_LIMIT;
	}

	
	public void setZoomLimit(double d) {
		zoomLimit = d;
	}
	
	public double getZoomLimit() {
		return zoomLimit;
	}
	
	public boolean isZoomLimit() {
		return isSlowZoom;
	}
	
	public void setSlowZoom(boolean b) {
		isSlowZoom = b;
	}
	
	@Override
	public void setScreenBounds(double left, double right, double bottom, double top) {
		goalLeft = left;
		goalRight = right;
		goalBottom = bottom;
		goalTop = top;
		update();
	}

	/*
	private void fixAspect() {
		System.out.println(String.format("%s, %s", getWidth(), getHeight()));
		double xDensity =  (goalRight - goalLeft) / getWidth();
		double yDensity = (goalTop - goalBottom) / getHeight();
		if (xDensity < yDensity) {
			double halfWidth = (goalRight - goalLeft) / 2;
			halfWidth *= yDensity / xDensity;
			double cen = (goalRight + goalLeft) / 2;
			goalLeft = cen - halfWidth;
			goalRight = cen + halfWidth;
		} else if (xDensity > yDensity) {
			double halfHeight = (goalTop - goalBottom) / 2;
			halfHeight = xDensity / yDensity;
			double cen = (goalTop + goalBottom) / 2;
			goalBottom = cen - halfHeight;    
			goalTop = cen + halfHeight;
		}
	}
	*/
	
	private double getAllowedMovementRatio(double left, double right, double bot, double top, double dt) {
		double rat = 1;

		double allowed = zoomLimit * dt;

		if (left > 0 && left > allowed) {
			//System.out.println("l " + left);
			rat = Math.min(rat, allowed / left);
		}
		if (right < 0 && right < -allowed) {
			//System.out.println("r " + right);
			rat = Math.min(rat, - allowed / right);
		}
		if (bot > 0 && bot > allowed) {
			//System.out.println("b " + bot);
			rat = Math.min(rat, allowed / bot);
		}
		if (top < 0 && top < -allowed) {
			//System.out.println("t " + top);
			rat = Math.min(rat, - allowed / top);
		}

		//System.out.println(rat);
		
		return rat;
	}
	
	@Override
	public void update() {
		if (isSlowZoom) {
			//fixAspect();
			long now = System.currentTimeMillis();
			double dt = (now - lastUpdated) / 1000.0;

			double dLeft = goalLeft - getLeft(); 
			double dRight = goalRight - getRight();
			double dBot = goalBottom - getBottom();
			double dTop = goalTop - getTop();

			double rat = getAllowedMovementRatio(dLeft, dRight, dBot, dTop, dt);
			
			if (dBot > 0) dBot *= rat;
			if (dTop < 0) dTop *= rat;
			if (dLeft > 0) dLeft *= rat;
			if (dRight < 0) dRight *= rat;
			
			setTop(getTop() + dTop);
			setBottom(getBottom() + dBot);
			setLeft(getLeft() + dLeft);
			setRight(getRight() + dRight);

			lastUpdated = now;
		} else {
			super.setScreenBounds(goalLeft, goalRight, goalBottom, goalTop); 
		}
		fixAspectRatio();
		
	}
	
	/*
	@Override
	public void update() {
		if (isSlowZoom) {
			long now = System.currentTimeMillis();
			double dt = (now - lastUpdated) / 1000.0;
			double allowed = zoomLimit * dt;

			double dTop = goalTop - getTop();
			double dBot = goalBottom - getBottom();
			double dLeft = goalLeft - getLeft(); 
			double dRight = goalRight - getRight();

			dLeft= Math.min(allowed, dLeft);
			dRight = Math.max(-allowed, dRight);
			dBot = Math.min(allowed, dBot);
			dTop = Math.max(-allowed, dTop);
			
			setTop(getTop() + dTop);
			setBottom(getBottom() + dBot);
			setLeft(getLeft() + dLeft);
			setRight(getRight() + dRight);

			lastUpdated = now;
		} else {
			super.setScreenBounds(goalLeft, goalRight, goalBottom, goalTop); 
		}
		fixAspectRatio();
		
	}
	*/
}
