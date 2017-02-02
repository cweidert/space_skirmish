package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.GameOptions;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingSlider;

@SuppressWarnings("serial")
public class PanelOptions extends JPanel {
	public PanelOptions() {
		super(new BorderLayout());
		PanelUtils.addEtch(this, "Game Settings");

		JPanel optionPanel = new JPanel(new GridLayout(0, 1));
		
		UpdatingCheckBox box;
		
		box = new UpdatingCheckBox("Planet", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getOptions().setPlanet(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getOptions().isPlanet();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Planet Stationary", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getOptions().setPlanetStationary(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getOptions().isPlanetStationary();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Gravity", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getOptions().setGravity(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getOptions().isGravity();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getOptions().setWrap(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getOptions().isWrap();
		});
		optionPanel.add(box);
		/*
		box = new UpdatingCheckBox("Kill Zone", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().setKillZone(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().isKillZone();
		});
		optionPanel.add(box);
		*/
		box = new UpdatingCheckBox("Auto Restart", (Boolean b) -> {
			if (SpaceFrame.hasOwnGame()) {
				SpaceFrame.getGame().getOptions().setAutoRestart(b);
			}
		}, () -> {
			return SpaceFrame.hasOwnGame() && SpaceFrame.getGame().getOptions().isAutoRestart();
		});
		optionPanel.add(box);
		JPanel panel;
		UpdatingSlider slider;
		panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Gravity Level"), BorderLayout.WEST);
		slider = new UpdatingSlider(0, GameOptions.MAX_BIG_G, GameOptions.DEFAULT_BIG_G, (Integer i) -> {
			SpaceFrame.getGame().getOptions().setBigG(i);
		}, () -> {
			if (SpaceFrame.hasOwnGame()) {
				return GameOptions.DEFAULT_BIG_G;
			} else {
				return SpaceFrame.getGame().getOptions().getBigG();
			}
		});
		panel.add(slider, BorderLayout.CENTER);
		optionPanel.add(panel);
		/*
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
		add(optionPanel, BorderLayout.SOUTH);
	}
}
