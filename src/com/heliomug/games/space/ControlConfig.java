package com.heliomug.games.space;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_NUMPAD4;
import static java.awt.event.KeyEvent.VK_NUMPAD5;
import static java.awt.event.KeyEvent.VK_NUMPAD6;
import static java.awt.event.KeyEvent.VK_NUMPAD7;
import static java.awt.event.KeyEvent.VK_NUMPAD8;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_Y;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class ControlConfig {
	private static int[][] DEFAULT_CONFIGS = new int[][] {
		new int[] { VK_A, VK_D, VK_W, VK_S, VK_E},
		new int[] { VK_LEFT, VK_RIGHT, VK_UP, VK_DOWN, VK_DELETE },
		new int[] { VK_J, VK_L, VK_I, VK_K, VK_U},
		new int[] { VK_F, VK_H, VK_T, VK_G, VK_Y},
		new int[] { VK_NUMPAD4, VK_NUMPAD6, VK_NUMPAD8, VK_NUMPAD5, VK_NUMPAD7},
//		new int[] { VK_, VK_, VK_, VK_},
	};
	
	private static Map<String, String> replacements;
	
	static {
		replacements = new HashMap<>();
		for (int i = 0 ; i < 10 ; i++) {
			replacements.put("NumPad-" + i, "NP-" + i);
		}
		replacements.put("Delete", "Del");
	}
	
	Player player;
	
	int fireKey;
	int leftKey;
	int rightKey;
	int forwardKey;
	int backKey;
	
	boolean leftDown;
	boolean rightDown;
	boolean forwardDown;
	boolean backDown;
	boolean fireDown;
	
	static int numPlayers = 0;
	
	public ControlConfig(Player player) {
		this.player = player;
		int ind = numPlayers;
		if (numPlayers >= DEFAULT_CONFIGS.length) {
			ind = DEFAULT_CONFIGS.length - 1;
		}
		setAll(DEFAULT_CONFIGS[ind]);
		numPlayers++;
	}
	
	private void setAll(int[] keys) { // left, int right, int forward, int back, int fire) {
		this.leftKey = keys[0];
		this.rightKey = keys[1];
		this.forwardKey = keys[2];
		this.backKey = keys[3];
		this.fireKey = keys[4];
	}
	
	private String keyToString(int key) {
		String s = KeyEvent.getKeyText(key);
		if (replacements.containsKey(s)) {
			return replacements.get(s);
		} else {
			return s;
		}
	}
	
	public String getKeyString(VehicleSignal sig) {
		if (sig == VehicleSignal.TURN_LEFT) {
			return keyToString(leftKey);
		} else if (sig == VehicleSignal.TURN_RIGHT) {
			return keyToString(rightKey);
		} else if (sig == VehicleSignal.FORWARD) {
			return keyToString(forwardKey);
		} else if (sig == VehicleSignal.BACKWARDS) {
			return keyToString(backKey);
		} else if (sig == VehicleSignal.FIRE) {
			return keyToString(fireKey);
		} else {
			return "";
		}
	}
	
	public void setKey(VehicleSignal sig, int key) {
		if (sig == VehicleSignal.TURN_LEFT) {
			leftKey = key;
		} else if (sig == VehicleSignal.TURN_RIGHT) {
			rightKey = key;
		} else if (sig == VehicleSignal.FORWARD) {
			forwardKey = key;
		} else if (sig == VehicleSignal.FIRE) {
			fireKey = key;
		} else if (sig == VehicleSignal.BACKWARDS) {
			backKey = key;
		} 
	}
	
	public VehicleSignal getSignal(int key, boolean isDown) {
		if (key == leftKey) {
			if (leftDown != isDown) {
				leftDown = isDown;
				if (isDown) {
					return VehicleSignal.TURN_LEFT;
				} else {
					if (rightDown) {
						return VehicleSignal.TURN_RIGHT;
					} else {
						return VehicleSignal.TURN_NONE;
					}
				}
			} 
		} else if (key == rightKey) {
			if (rightDown != isDown) {
				rightDown = isDown;
				if (isDown) {
					return VehicleSignal.TURN_RIGHT;
				} else {
					if (leftDown) {
						return VehicleSignal.TURN_LEFT;
					} else {
						return VehicleSignal.TURN_NONE;
					}
				}
			} 
		} else if (key == forwardKey) {
			if (forwardDown != isDown) {
				forwardDown = isDown;
				if (isDown) {
					return VehicleSignal.FORWARD;
				} else {
					if (backDown) {
						return VehicleSignal.BACKWARDS;
					} else {
						return VehicleSignal.ACCEL_OFF;
					}
				}
			} 
		} else if (key == backKey) {
			if (backDown != isDown) {
				backDown = isDown;
				if (isDown) {
					return VehicleSignal.BACKWARDS;
				} else {
					if (forwardDown) {
						return VehicleSignal.FORWARD;
					} else {
						return VehicleSignal.ACCEL_OFF;
					}
				}
			} 
		} else if (key == fireKey) {
			if (fireDown != isDown) {
				fireDown = isDown;
				if (isDown) {
					return VehicleSignal.FIRE;
				}
			}
		}
		
		return null;
	}
}
