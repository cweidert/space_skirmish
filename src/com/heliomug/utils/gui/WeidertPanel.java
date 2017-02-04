package com.heliomug.utils.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * 
 * This is a class that's just a panel that you can scroll in.  It handles zooming and panning and so on.  
 * 
 * @author Craig Weidert
 *
 */
public class WeidertPanel extends JPanel  {
	private static final long serialVersionUID = 6395548544912733950L;

	private static final boolean DEFAULT_IS_ZOOMABLE = true;
	private static final boolean DEFAULT_IS_DRAGGABLE = true;

	private static final double WHEEL_ZOOM_FACTOR = 1.25;
	
	public double getTop() {
		return top;
	}


	public void setTop(double top) {
		this.top = top;
	}

	private double left, right, bottom, top;
	
	private boolean isZoomable, isDraggable;
	
	public WeidertPanel(int pixelWidth, int pixelHeight) {
		this(pixelWidth, pixelHeight, -10, 10, -10, 10);
	}

		
	public WeidertPanel(int pixelWidth, int pixelHeight, Rectangle2D bounds) {
		this(pixelWidth, pixelHeight, bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
	}
	
	/**
	 * 
	 * Makes a new panel that you can scroll with your mouse and display shapes on an arbitrarily-sized canvas
	 * 
	 * @param pixelWidth Width of panel in pixels
	 * @param pixelHeight Height of panel in pixels
	 * @param left Coordinate on left side of screen
	 * @param right Coordinate on right side of screen
	 * @param bottom Coordinate on bottom side if screen
	 * @param top Coordinate on top side of screen
	 */
	public WeidertPanel(int pixelWidth, int pixelHeight, double left, double right, double bottom, double top) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		
		this.isZoomable = DEFAULT_IS_ZOOMABLE;
		this.isDraggable = DEFAULT_IS_DRAGGABLE;
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				fixAspectRatio();
			}
		});
		
		ScreenMouser mouser = new ScreenMouser();
		this.addMouseWheelListener(mouser);
		this.addMouseListener(mouser);
		this.addMouseMotionListener(mouser);

		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) { handleKeyPress(e); }

			@Override
			public void keyReleased(KeyEvent e) { handleKeyRelease(e); }

		});
		
		this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
		this.setDoubleBuffered(true);
	}
	
	public double getLeft() {
		return left;
	}


	public void setLeft(double left) {
		this.left = left;
	}


	public double getRight() {
		return right;
	}


	public void setRight(double right) {
		this.right = right;
	}


	public double getBottom() {
		return bottom;
	}


	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	/**
	 * Override this to do things with keys.  
	 * 
	 * @param e The key event with info about which key was pressed, holding shift, etc
	 */
	public void handleKeyPress(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			quit();
		}
	}

	/**
	 * Override this to do things with keys.  
	 * 
	 * @param e The key event with info about which key was pressed, holding shift, etc
	 */
	public void handleKeyRelease(KeyEvent e) {
		// do nothing. maybe override
	}
	
	public void quit() {
		System.exit(0);
	}
	
	/**
	 * Turns mouse zooming on / off
	 * 
	 * @param isZoomable Whether or not you can zoom with mouse wheel.  
	 */
	public void setZoomable(boolean isZoomable) { this.isZoomable = isZoomable; }

	/**
	 * Turns mouse dragging the screen on / off
	 * 
	 * @param isDraggable Whether or not you can zoom with mouse wheel.  
	 */
	public void setDraggable(boolean isDraggable) { this.isDraggable = isDraggable; }
	
	/**
	 * Override to paint.  Don't forget to call super.paint(g);
	 */
	@Override
	public void paintComponent(Graphics g) {
		update();
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setTransform(getTransform());
	}
	
	/**
	 * Scoot the screen window over by amount of pixels.  
	 * 
	 * @param dx horizontal shift
	 * @param dy vertical shift
	 */
	public void translateScreenByPixels(int dx, int dy) {
		AffineTransform t = getTransform();
		translateScreen(dx / t.getScaleX(), dy / t.getScaleY());
	}
	
	/**
	 * Scoot screen over by coordinate amounts
	 * 
	 * @param dx horizontal shift
	 * @param dy vertical shift
	 */
	public void translateScreen(double dx, double dy) {
		left += dx;
		right += dx;
		top += dy;
		bottom += dy;
	}

	/**
	 * Sets the bounds of the window
	 * 
	 * @param bounds A Rectangle2D storing the rectangle to be viewed
	 */
	public void setScreenBounds(Rectangle2D bounds) {
		setScreenBounds(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
	}
	
	/**
	 * Sets the bounds of the window
	 * 
	 * @param left left
	 * @param right right
	 * @param bottom bottom 
	 * @param top top
	 */
	public void setScreenBounds(double left, double right, double bottom, double top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		fixAspectRatio();
	}

	/**
	 * Sets center of screen
	 * 
	 * @param x x coord
	 * @param y y coord
	 */
	public void setCenterOfScreen(double x, double y) {
		double xSpan = (right - left) / 2;
		double ySpan = (top - bottom) / 2;
		setScreenBounds(x - xSpan, x + xSpan, y - ySpan, y + ySpan);
	}
	
	/**
	 * Sets the zoom / radius shown on the screen
	 * 
	 * @param r radius to show on the screeen
	 */
	public void setRadiusShown(double r) {
		double xCen = (left + right) / 2;
		double yCen = (top + bottom) / 2;
		if (getWidth() > getHeight()) {
			setScreenBounds(xCen - r * getWidth() / getHeight(), xCen + r * getWidth() / getHeight(), yCen - r, yCen + r);
		} else {
			setScreenBounds(xCen - r, xCen + r, yCen - r * getHeight() / getWidth(), yCen + r * getHeight() / getWidth());
		}
	}
	
	/**
	 * Dilate window about a point by a factor.  
	 * 
	 * @param x x coord
	 * @param y y coord
	 * @param s scale factor
	 */
	public void zoom(double x, double y, double s) {
		double l = x - (x - left) * s;
		double r = x + (right - x) * s;
		double t = y + (top - y) * s;
		double b = y - (y - bottom) * s;
		setScreenBounds(l, r, b, t);
	}
	
	/**
	 * Handles mouse clicks on the screen.  
	 * You can override this method to do all kinds of things with the mouse like make new sprites when you click.  
	 *  
	 * @param x x coord
	 * @param y y coord
	 * @param e This is a mouse event that contains things like which button was pushed, if you were holding shift, etc.  
	 */
	public void handleMouseClick(double x, double y, MouseEvent e) {
	}

	public void update() {
	}
	
	private Point2D getLocation(int px, int py) { 
		try {
			return getTransform().inverseTransform(new Point2D.Double(px,  py), null);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public final AffineTransform getTransform() {
		double xCoeff = getWidth() / (right - left);
		double yCoeff = getHeight() / (bottom - top);
		double xConst = left * getWidth() / (left - right);
		double yConst = top * getHeight() / (top - bottom);
		return new AffineTransform(xCoeff, 0, 0, yCoeff, xConst, yConst);
	}
	
	public void fixAspectRatio() {
		AffineTransform t = getTransform();
		double xScale = Math.abs(t.getScaleX());
		double yScale = Math.abs(t.getScaleY());
		if (xScale > yScale) {
			double cen = (left + right) / 2;
			double halfWidth = getWidth() / yScale / 2; 
			left = cen - halfWidth;    
			right = cen + halfWidth;
			/*
			halfWidth = (goalRight - goalLeft) / yScale / 2;
			cen = (goalRight - goalLeft) / 2;
			goalRight = cen + halfWidth;
			goalLeft = cen - halfWidth;
			*/
		} else if (xScale < yScale) {
			double cen = (top + bottom) / 2;
			double halfHeight = getHeight() / xScale / 2; 
			bottom = cen - halfHeight;    
			top = cen + halfHeight;
			/*
			cen = (goalTop + goalBottom) / 2;
			halfHeight = (goalTop - goalBottom) / xScale / 2; 
			goalBottom = cen - halfHeight;    
			goalTop = cen + halfHeight;
			*/
		}
	}
	
	private class ScreenMouser extends MouseAdapter implements MouseWheelListener {
		private int dragStartX, dragStartY;

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (isZoomable) {
				int notches = e.getWheelRotation();
				Point2D zoomPoint = getLocation(e.getX(), e.getY());
				zoom(zoomPoint.getX(), zoomPoint.getY(), Math.pow(WHEEL_ZOOM_FACTOR, notches));
				repaint();
			}
		}
	
		@Override
		public void mouseDragged(MouseEvent e) {
			if (isDraggable) {
				translateScreenByPixels(dragStartX - e.getX(), dragStartY - e.getY());
				updateDragStart(e);
				repaint();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (isDraggable) updateDragStart(e);
			Point2D p = getLocation(e.getX(), e.getY());
			WeidertPanel.this.handleMouseClick(p.getX(), p.getY(), e);
		}
		
		private void updateDragStart(MouseEvent e) {
			dragStartX = e.getX();
			dragStartY = e.getY();
		}
	}
}