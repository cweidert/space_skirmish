package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.GameSettings;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingSlider;

@SuppressWarnings("serial")
public class CardSettings extends JPanel {
	public CardSettings() {
		super(new BorderLayout());

		add(getSettingsPanel(), BorderLayout.CENTER);
		add(new PanelReturnToGame(), BorderLayout.SOUTH);
	}
	
	public JPanel getSettingsPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(getMiscSettings());
		panel.add(getGravityPanel());
		panel.add(getPlanetPanel());
		panel.add(getBoundaryPanel());
		return panel;
	}

	private JPanel getMiscSettings() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Misc Settings");
		UpdatingCheckBox box;
		box = new UpdatingCheckBox("Auto Restart", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getSettings().setAutoRestart(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getSettings().isAutoRestart();
		});
		panel.add(box);
		return panel;
	}
	
	private JPanel getGravityPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Gravity Settings");
		UpdatingCheckBox box;
		box = new UpdatingCheckBox("Gravity", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getSettings().setGravity(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getSettings().isGravity();
		});
		panel.add(box);
		JPanel subpanel;
		UpdatingSlider slider;
		subpanel = new JPanel(new BorderLayout());
		subpanel.add(new JLabel("Gravity Level"), BorderLayout.WEST);
		slider = new UpdatingSlider(0, GameSettings.MAX_BIG_G, GameSettings.DEFAULT_BIG_G, (Integer i) -> {
			SpaceFrame.getGame().getSettings().setBigG(i);
		}, () -> {
			if (SpaceFrame.hasOwnGame()) {
				return GameSettings.DEFAULT_BIG_G;
			} else {
				return SpaceFrame.getGame().getSettings().getBigG();
			}
		});
		subpanel.add(slider, BorderLayout.CENTER);
		panel.add(subpanel);
		return panel;
	}
	
	private JPanel getPlanetPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Planet Settings");
		UpdatingCheckBox box;
		box = new UpdatingCheckBox("Planet On", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getSettings().setPlanet(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getSettings().isPlanet();
		});
		panel.add(box);
		box = new UpdatingCheckBox("Planet Stationary", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getSettings().setPlanetStationary(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getSettings().isPlanetStationary();
		});
		panel.add(box);
		return panel;
	}
	
	private JPanel getBoundaryPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		PanelUtils.addEtch(panel, "Boundary Settings");

		UpdatingCheckBox box;
		
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getSettings().setWrap(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getSettings().isWrap();
		});
		panel.add(box);

		/*
		box = new UpdatingCheckBox("Kill Zone", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().setKillZone(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().isKillZone();
		});
		optionPanel.add(box);
		panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Kill Zone Ratio"), BorderLayout.WEST);
		slider = new UpdatingSlider(
				0, 
				(int)GameOptions.MAX_KILL_ZONE_RATIO * 100, 
				(int)GameOptions.DEFAULT_KILL_ZONE_RATIO * 100, 
				(Integer i) -> {
					SpaceFrame.getGame().setKillZoneRatio(i / 100.0);
					System.out.println(SpaceFrame.getGame().getKillZoneRatio());
				}, () -> {
					if (SpaceFrame.getServer() == null) {
						return (int)GameOptions.DEFAULT_KILL_ZONE_RATIO * 100;
					} else {
						return (int)(SpaceFrame.getGame().getKillZoneRatio() * 100);
					}
				}
		);
		panel.add(slider, BorderLayout.CENTER);
		optionPanel.add(panel);
		*/
		
		return panel;
	}

}

