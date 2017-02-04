package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.heliomug.games.space.GameSettings;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingSlider;

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
		JPanel subpanel;
		UpdatingSlider slider;
		subpanel = new JPanel(new BorderLayout());
		subpanel.add(new JLabel("Bullet Age Limit"), BorderLayout.WEST);
		int max = (int)GameSettings.MAX_BULLET_AGE_LIMIT;
		int def = (int)GameSettings.DEFAULT_BULLET_AGE_LIMIT;
		slider = new UpdatingSlider(0, max, def, (Integer i) -> {
			Session.getGame().getSettings().setBulletAgeLimit(i);
		}, () -> {
			if (Session.hasOwnGame()) {
				return def;
			} else {
				return (int)Session.getGame().getSettings().getBulletAgeLimit();
			}
		});
		subpanel.add(slider, BorderLayout.CENTER);
		panel.add(subpanel);
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
		box = new UpdatingCheckBox("Tank Mode", (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setTankMode(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isTankMode();
		});
		panel.add(box);
		return panel;
	}
	
	private JPanel getSpacePanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Gravity Settings");
		UpdatingCheckBox box;

		box = new UpdatingCheckBox("Planet On", () -> Session.getGame().getSettings().isTankMode(), (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setPlanet(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isPlanet();
		});
		panel.add(box);
		box = new UpdatingCheckBox("Planet Stationary", (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setPlanetStationary(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isPlanetStationary();
		});
		panel.add(box);

		box = new UpdatingCheckBox("Gravity", () -> Session.getGame().getSettings().isTankMode(), (Boolean b) -> {
			if (Session.hasOwnGame()) {
				Session.getGame().getSettings().setGravity(b);
			}
		}, () -> {
			return Session.hasOwnGame() && Session.getGame().getSettings().isGravity();
		});
		panel.add(box);
		JPanel subpanel;
		UpdatingSlider slider;
		subpanel = new JPanel(new BorderLayout());
		subpanel.add(new JLabel("Gravity Level"), BorderLayout.WEST);
		slider = new UpdatingSlider(0, GameSettings.MAX_BIG_G, GameSettings.DEFAULT_BIG_G, (Integer i) -> {
			Session.getGame().getSettings().setBigG(i);
		}, () -> {
			if (Session.hasOwnGame()) {
				return GameSettings.DEFAULT_BIG_G;
			} else {
				return Session.getGame().getSettings().getBigG();
			}
		});
		subpanel.add(slider, BorderLayout.CENTER);
		panel.add(subpanel);

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
		JPanel subpanel = new JPanel(new BorderLayout());
		subpanel.add(new JLabel("Safe Zone Ratio"), BorderLayout.WEST);
		JSlider slider = new UpdatingSlider(
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
		subpanel.add(slider, BorderLayout.CENTER);
		panel.add(subpanel);
		
		return panel;
	}

}

