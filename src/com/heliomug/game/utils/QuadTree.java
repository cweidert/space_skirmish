package com.heliomug.game.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class QuadTree<T extends Boundable> {
	private static final int MAX_CAPACITY = 5;

	private QuadTree<T>[] subTrees;
	private List<T> payload;
	private int level;
	private Rectangle2D bounds;
	
	public QuadTree() {
		level = 0;
		this.payload = new ArrayList<T>();
		this.subTrees = null;
		bounds = new Rectangle2D.Double(0, 0, 10, 10);
	}

	public QuadTree(int level, Rectangle2D bounds) {
		this();
		this.level = level;
		this.bounds = bounds;
	}

	public QuadTree(Rectangle2D bounds) {
		this(0, bounds);
	}
	
	
	public synchronized void refill(List<T> li) {
		this.clear();
		
		this.subTrees = null;
		this.payload = new ArrayList<T>();
		this.level = 0;
		if (li.size() == 0) {
			this.bounds = new Rectangle2D.Double(0, 0, 10, 10);
		} else {
			double xMin = Double.POSITIVE_INFINITY;
			double yMin = Double.POSITIVE_INFINITY;
			double xMax = Double.NEGATIVE_INFINITY;
			double yMax = Double.NEGATIVE_INFINITY;
			for (int i = 0 ; i < li.size() ; i++) {
				double x = li.get(i).getBounds().getX();
				double y = li.get(i).getBounds().getY();
				xMin = Math.min(xMin, x);
				yMin = Math.min(yMin, y);
				xMax = Math.max(xMax, x + li.get(i).getBounds().getWidth());
				yMax = Math.max(yMax, y + li.get(i).getBounds().getHeight());
			}
			this.bounds = new Rectangle2D.Double(xMin, yMin, xMax - xMin, yMax - yMin);
		}
		for (T t : li) {
			insert(t);
		}
	}
	
	public synchronized void clear() {
		payload.clear();
		if (subTreesExist()) {
			for (int i = 0 ; i < subTrees.length ; i++) {
				if (subTrees[i] != null) {
					subTrees[i].clear();
					subTrees[i] = null;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void split() {
		double subWidth = bounds.getWidth() / 2;
		double subHeight = bounds.getHeight() / 2;
		double left = bounds.getX();
		double bottom = bounds.getY();
		
		subTrees = new QuadTree[4];
		subTrees[0] = new QuadTree<T>(level + 1, new Rectangle2D.Double(left + subWidth, bottom + subHeight, subWidth, subHeight));
		subTrees[1] = new QuadTree<T>(level + 1, new Rectangle2D.Double(left, bottom + subHeight, subWidth, subHeight));
		subTrees[2] = new QuadTree<T>(level + 1, new Rectangle2D.Double(left, bottom, subWidth, subHeight));
		subTrees[3] = new QuadTree<T>(level + 1, new Rectangle2D.Double(left + subWidth, bottom, subWidth, subHeight));
	}
	
	public synchronized boolean subTreesExist() {
		return subTrees != null;
	}
	
	public synchronized int getIndex(Boundable b) {
		int toRet = -1;
		
		for (int i = 0 ; i < subTrees.length ; i++) {
			if (subTrees[i].bounds.contains(b.getBounds())) {
				toRet = i;
			}
		}
		return toRet;
	}
	
	public synchronized void insert(T b) {
		if (subTreesExist()) {
			int index = getIndex(b); 
			
			if (index == -1) {
				payload.add(b);
			} else {
				subTrees[index].insert(b);
			}
		} else {
			payload.add(b);
			
			if (payload.size() > MAX_CAPACITY && !subTreesExist()) {
				split();
				
				for (int i = 0 ; i < payload.size() ; i++) {
					T el = payload.get(i); 
					int index = getIndex(el);
					if (index != -1) {
						subTrees[index].insert(el);
						payload.remove(i);
						i--;
					}
				}
			}
		}
	}
	
	private List<T> getPossibleHits(Rectangle2D bounds) {
		return getPossibleHits(new TestRect(bounds));
	}
	
	private List<T> getPossibleHits(Boundable b) {
		List<T> toRet = new ArrayList<T>();
		
		if (subTreesExist()) {
			for (int i = 0 ; i < subTrees.length ; i++) {
				if (subTrees[i].bounds.intersects(b.getBounds())) {
					toRet.addAll(subTrees[i].getPossibleHits(b));
				}
			}
		}
		
		toRet.addAll(payload);
		
		return toRet;
	}
	
	public synchronized List<T> intersections(Boundable b) {
		return getOverlappers(b.getBounds());
	}
	
	public synchronized List<T> getOverlappers(Rectangle2D bounds) {
		List<T> li = getPossibleHits(bounds);
		for (int i = 0 ; i < li.size() ; i++) {
			T b = li.get(i);
			if (!b.getBounds().intersects(bounds)) {
				li.remove(i);
				i--;
			}
		}
		return li;
	}
	
	public synchronized List<T> getOverlappers(Boundable b) {
		return getOverlappers(b.getBounds());
	}
	
	private synchronized List<T> getPossibleHits(Point2D p) {
		List<T> toRet = new ArrayList<T>();
		
		if (subTreesExist()) {
			for (int i = 0; i < subTrees.length ; i++) {
				if (subTrees[i].bounds.contains(p)) {
					toRet.addAll(subTrees[i].getPossibleHits(p));
				}
			}
		}
		
		toRet.addAll(payload);
		
		return toRet;
	}

	public synchronized List<T> getOverlappers(double x, double y) {
		return getOverlappers(new Point2D.Double(x, y));
	}
	
	public synchronized List<T> getOverlappers(Point2D p) {
		List<T> li = getPossibleHits(p);
		for (int i = 0 ; i < li.size() ; i++) {
			T b = li.get(i);
			if (!b.getBounds().contains(p)) {
				li.remove(i);
				i--;
			}
		}
		return li;
	}

	public synchronized void draw(Graphics2D g) {
		drawRectangles(g);
		drawBounds(g);
	}
	
	private void drawRectangles(Graphics2D g) {
		if (subTreesExist()) {
			for (int i = 0 ; i < subTrees.length ; i++) {
				subTrees[i].drawRectangles(g);
			}
		}
		g.setColor(Color.RED);
		for (Boundable b : payload) {
			g.setColor(Color.RED);
			g.fill(b.getBounds());
			g.setColor(Color.BLACK);
			g.draw(b.getBounds());
		}
	}
	
	private void drawBounds(Graphics2D g) {
		if (subTreesExist()) {
			for (int i = 0 ; i < subTrees.length ; i++) {
				subTrees[i].drawBounds(g);
			}
		}
		g.setColor(Color.BLACK);
		g.draw(bounds);
	}
	
	private static class TestRect implements Boundable {
		Rectangle2D r;
		
		public TestRect(Rectangle2D bounds) {
			r = bounds;
		}
		
		public Rectangle2D getBounds() { return r; }	
	}
}
