package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.GameOptions;
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingSlider;

@SuppressWarnings("serial")
public class PanelOptions extends EtchedPanel {
	public PanelOptions() {
		super("Game Options", new BorderLayout());
		
		JPanel optionPanel = new JPanel(new GridLayout(0, 1));
		
		UpdatingCheckBox box;
		
		box = new UpdatingCheckBox("Planet", (Boolean b) -> {
			if (Frame.getServer() != null) {
				Frame.getServer().getThing().getOptions().setPlanet(b);
			}
		}, () -> {
			return Frame.getServer() != null && Frame.getServer().getThing().getOptions().isPlanet();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Planet Stationary", (Boolean b) -> {
			if (Frame.getServer() != null) {
				Frame.getServer().getThing().getOptions().setPlanetStationary(b);
			}
		}, () -> {
			return Frame.getServer() != null && Frame.getServer().getThing().getOptions().isPlanetStationary();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Gravity", (Boolean b) -> {
			if (Frame.getServer() != null) {
				Frame.getServer().getThing().getOptions().setGravity(b);
			}
		}, () -> {
			return Frame.getServer() != null && Frame.getServer().getThing().getOptions().isGravity();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (Frame.getServer() != null) {
				Frame.getServer().getThing().getOptions().setWrap(b);
			}
		}, () -> {
			return Frame.getServer() != null && Frame.getServer().getThing().getOptions().isWrap();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Auto Restart", (Boolean b) -> {
			if (Frame.getServer() != null) {
				Frame.getServer().getThing().getOptions().setAutoRestart(b);
			}
		}, () -> {
			return Frame.getServer() != null && Frame.getServer().getThing().getOptions().isAutoRestart();
		});
		optionPanel.add(box);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Gravity Level"), BorderLayout.WEST);
		UpdatingSlider slider = new UpdatingSlider(0, 50000, 10000, (Integer i) -> {
			Frame.getServer().getThing().getOptions().setBigG(i);
		}, () -> {
			if (Frame.getServer() == null) {
				return GameOptions.DEFAULT_BIG_G;
			} else {
				return Frame.getServer().getThing().getOptions().getBigG();
			}
		});
		panel.add(slider, BorderLayout.CENTER);
		optionPanel.add(panel);
		add(optionPanel, BorderLayout.SOUTH);
	}
}
