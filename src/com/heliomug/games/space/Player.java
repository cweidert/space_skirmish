package com.heliomug.games.space;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = -4011767966592548445L;

	private String name;
	private int wins;
	
	public Player(String name) {
		this.name = name;
		this.wins = 0;
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
