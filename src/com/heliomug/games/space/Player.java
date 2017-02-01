package com.heliomug.games.space;

import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = -4011767966592548445L;

	private String name;
	private int wins;
	private Color color;
	
	// id is for serialization / matching purposes
	@SuppressWarnings("unused")
	private long id;
	
	public Player(String name, Color color) {
		this.name = name;
		this.wins = 0;
		this.color = color;
		this.id = (long)(Math.random() * Long.MAX_VALUE);
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void addWin() {
		wins++;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}
