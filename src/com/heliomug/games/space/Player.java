package com.heliomug.games.space;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

import com.heliomug.utils.server.Address;

public class Player implements Serializable {
	private static final long serialVersionUID = -4011767966592548445L;

	private String name;
	private int wins;
	private Color color;
	
	private Address address;
	
	// id is for serialization / matching purposes
	private long id;
	
	public Player(int i, Color c) {
		this(getNameString(i), c);
	}
	
	public Player(String name, Color color) {
		this.name = name;
		this.wins = 0;
		this.color = color;
		this.id = (long)(Math.random() * Long.MAX_VALUE);
		this.address = new Address();
	}
	
	private static String getNameString(int i) {
		return "Player " + i; 
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
	
	public Address getAddress() {
		return this.address;
	}
	
	// i know keys to maps are supposed to be immutable,
	// so i fudged the hashCode and equals a bit.
	@Override
	public int hashCode() {
		return Objects.hash(name, id);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other instanceof Player) {
			Player op = (Player) other;
			return op.id == id && op.name.equals(name);
		} else {
			return false;
		}
	}
	
	public String getWinString() {
		String winString = "wins";
		if (wins == 1) {
			winString = "win";
		}
		return String.format("%d " + winString, wins);
	}
	
	public String toString() {
		return String.format("%s %s", getName(), getWinString());
	}
}
