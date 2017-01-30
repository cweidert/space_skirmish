package com.heliomug.games.space;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = -4011767966592548445L;

	private String name;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}
