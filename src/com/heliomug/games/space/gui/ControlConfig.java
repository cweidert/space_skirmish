package com.heliomug.games.space.gui;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;

import java.awt.event.KeyEvent;

import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;

public class ControlConfig {
	private static int[][] DEFAULT_CONFIGS = new int[][] {
		new int[] { VK_LEFT, VK_RIGHT, VK_UP, VK_DOWN },
		new int[] { VK_A, VK_D, VK_W, VK_S},
		new int[] { VK_J, VK_L, VK_I, VK_K},
		new int[] { VK_F, VK_H, VK_T, VK_G},
//		new int[] { VK_, VK_, VK_, VK_},
//		new int[] { VK_, VK_, VK_, VK_},
	};
	
	Player player;
	
	int fireKey;
	int leftKey;
	int rightKey;
	int boostKey;
	
	boolean leftDown;
	boolean rightDown;
	boolean boostDown;
	boolean fireDown;
	
	static int numPlayers = 0;
	
	public ControlConfig(Player player) {
		this.player = player;
		int ind = numPlayers;
		if (numPlayers >= DEFAULT_CONFIGS.length) {
			ind = DEFAULT_CONFIGS.length - 1;
		}
		setAll(DEFAULT_CONFIGS[ind][0], DEFAULT_CONFIGS[ind][1], DEFAULT_CONFIGS[ind][2], DEFAULT_CONFIGS[ind][3]);
		numPlayers++;
	}
	
	public void setAll(int left, int right, int boost, int fire) {
		setLeftKey(left);
		setRightKey(right);
		setBoostKey(boost);
		setFireKey(fire);
	}
	
	public void setLeftKey(int key) {
		leftKey = key;
	}
	
	public void setRightKey(int key) {
		rightKey = key;
	}
	
	public void setBoostKey(int key) {
		boostKey = key;
	}
	
	public void setFireKey(int key) {
		fireKey = key;
	}

	public String getLeftString() {
		return getKeyString(leftKey);
	}
	
	public String getRightString() {
		return getKeyString(rightKey);
	}
	
	public String getBoostString() {
		return getKeyString(boostKey);
	}
	
	public String getFireString() {
		return getKeyString(fireKey);
	}
	
	public String getKeyString(int key) {
		return KeyEvent.getKeyText(key);
	}
	
	public ShipSignal getSignal(int key, boolean isDown) {
		if (key == leftKey) {
			if (leftDown != isDown) {
				leftDown = isDown;
				if (isDown) {
					return ShipSignal.TURN_LEFT;
				} else {
					if (rightDown) {
						return ShipSignal.TURN_RIGHT;
					} else {
						return ShipSignal.TURN_NONE;
					}
				}
			} 
		} else if (key == rightKey) {
			if (rightDown != isDown) {
				rightDown = isDown;
				if (isDown) {
					return ShipSignal.TURN_RIGHT;
				} else {
					if (leftDown) {
						return ShipSignal.TURN_LEFT;
					} else {
						return ShipSignal.TURN_NONE;
					}
				}
			} 
		} else if (key == boostKey) {
			if (boostDown != isDown) {
				boostDown = isDown;
				if (isDown) {
					return ShipSignal.ACCEL_ON;
				} else {
					return ShipSignal.ACCEL_OFF;
				}
			} 
		} else if (key == fireKey) {
			if (fireDown != isDown) {
				fireDown = isDown;
				if (isDown) {
					return ShipSignal.FIRE;
				}
			}
		}
		
		return null;
	}
}
