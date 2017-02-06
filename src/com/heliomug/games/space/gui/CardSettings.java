package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.heliomug.games.space.GameSettings;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingRadioButton;
import com.heliomug.utils.gui.UpdatingSliderPanel;

@SuppressWarnings("serial")
class CardSettings extends JPanel {
	public CardSettings() {
		super(new BorderLayout());

		add(getSettingsPanel(), BorderLayout.CENTER);
		add(new PanelGoToGameButton(), BorderLayout.SOUTH);
	}
	
	public JPanel getSettingsPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(getMiscSettings());
		panel.add(getSpacePanel());
		panel.add(getBulletPanel());
		panel.add(getBoundaryPanel());
		return panel;
	}

	// ship collision kills?
	
	

	private JPanel getBulletPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Bullet Settings");
		UpdatingCheckBox box;
		box = new UpdatingCheckBox("Bullet Age Limit", (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setBulletAgeLimit(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isBulletAgeLimit();
		});
		panel.add(box);
		int max = (int)GameSettings.MAX_BULLET_AGE_LIMIT;
		int def = (int)GameSettings.DEFAULT_BULLET_AGE_LIMIT;
		UpdatingSliderPanel slider = new UpdatingSliderPanel("Bullet Age Limit", 0, max, def, 
		(Integer i) -> {
			Session.getGame().getSettings().setBulletAgeLimit(i);
		}, () -> {
			if (Session.hasOwnGame()) {
				return (int)Session.getGame().getSettings().getBulletAgeLimit();
			} else {
				return def;
			}
		});
		panel.add(slider);
		return panel;
	}
	
	private JPanel getMiscSettings() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Misc Settings");
		UpdatingCheckBox box;

		box = new UpdatingCheckBox("Auto Restart", (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setAutoRestart(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isAutoRestart();
		});
		panel.add(box);
		ButtonGroup gameModeGroup = new ButtonGroup();
		JRadioButton button;
		button = new UpdatingRadioButton("Tank Mode", gameModeGroup, () -> Session.hasOwnGame(), ()-> {  
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setTankMode(true);
				Frame.resetGamePanel();
				this.repaint();
			}
		});
		panel.add(button);
		button = new UpdatingRadioButton("Space Mode", gameModeGroup, () -> Session.hasOwnGame(), ()-> {  
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setTankMode(false);
				Frame.resetGamePanel();
				this.repaint();
			}
		});
		panel.add(button);
		return panel;
	}
	
	private JPanel getSpacePanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Space Settings");
		UpdatingCheckBox box;

		box = new UpdatingCheckBox("Planet On", () -> {
			return !Session.getGame().getSettings().isTankMode() && Session.hasOwnGame();
		}, (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setPlanet(b);
				this.repaint();
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isPlanet();
		});
		panel.add(box);
		box = new UpdatingCheckBox("Planet Stationary", () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isPlanet();
		}, (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setPlanetStationary(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isPlanetStationary();
		});
		panel.add(box);

		box = new UpdatingCheckBox("Gravity", () -> {
			return !Session.getGame().getSettings().isTankMode() && Session.hasOwnGame();
		}, (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setGravity(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isGravity();
		});
		panel.add(box);
		UpdatingSliderPanel slider;
		slider = new UpdatingSliderPanel("Gravity Level", 0, GameSettings.MAX_BIG_G, GameSettings.DEFAULT_BIG_G, (Integer i) -> {
			Session.getGame().getSettings().setBigG(i);
		}, () -> {
			if (Session.hasOwnGame()) {
				return Session.getGame().getSettings().getBigG();
			} else {
				return GameSettings.DEFAULT_BIG_G;
			}
		});
		panel.add(slider);

		return panel;
	}
	
	private JPanel getBoundaryPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Boundary Settings");

		UpdatingCheckBox box;
		
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setWrap(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isWrap();
		});
		panel.add(box);

		box = new UpdatingCheckBox("Safe Zone", KeyEvent.VK_S, (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setSafeZone(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isSafeZone();
		});
		panel.add(box);
		JPanel slider = new UpdatingSliderPanel(
				"Safe Zone Radius", 
				0, 
				GameSettings.MAX_SAFE_ZONE_RADIUS, 
				GameSettings.DEFAULT_SAFE_ZONE_RADIUS, 
				(Integer i) -> {
					Session.getGame().getSettings().setSafeZoneRadius(i);
				}, () -> {
					if (Session.hasOwnGame()) {
						return Session.getGame().getSettings().getSafeZoneRadius();
					} else {
						return GameSettings.DEFAULT_SAFE_ZONE_RADIUS;
					}
				}
		);
		panel.add(slider);
		
		return panel;
	}

}

