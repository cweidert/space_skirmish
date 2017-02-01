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
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setPlanet(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isPlanet();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Planet Stationary", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setPlanetStationary(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isPlanetStationary();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Gravity", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setGravity(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isGravity();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setWrap(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isWrap();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Kill Zone", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setKillZone(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isKillZone();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Auto Restart", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setAutoRestart(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isAutoRestart();
		});
		optionPanel.add(box);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Gravity Level"), BorderLayout.WEST);
		UpdatingSlider slider = new UpdatingSlider(0, GameOptions.MAX_BIG_G, GameOptions.DEFAULT_BIG_G, (Integer i) -> {
			SpaceFrame.getServer().getThing().getOptions().setBigG(i);
		}, () -> {
			if (SpaceFrame.getServer() == null) {
				return GameOptions.DEFAULT_BIG_G;
			} else {
				return SpaceFrame.getServer().getThing().getOptions().getBigG();
			}
		});
		panel.add(slider, BorderLayout.CENTER);
		optionPanel.add(panel);
		add(optionPanel, BorderLayout.SOUTH);
	}
}
