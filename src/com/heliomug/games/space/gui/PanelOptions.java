package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingSlider;

@SuppressWarnings("serial")
public class PanelOptions extends EtchedPanel {
	public PanelOptions() {
		super("Game Options", new BorderLayout());
		
		JPanel optionPanel = new JPanel(new GridLayout(0, 2));
		
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
		UpdatingSlider slider = new UpdatingSlider(0, 50000, 10000, (Integer i) -> {
			num = i ; System.out.println(i);
		}, () -> num);
		optionPanel.add(slider);
		add(optionPanel, BorderLayout.SOUTH);
	}
	
	int num = 50;
}
