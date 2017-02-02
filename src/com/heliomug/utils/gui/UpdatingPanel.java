package com.heliomug.utils.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public abstract class UpdatingPanel extends JPanel {
	private static final int CYCLE_LENGTH = 1000;
	
	private static Timer timer;
	
	private static List<UpdatingPanel> panels;
	
	static {
		panels = new ArrayList<>();
		timer = new Timer(CYCLE_LENGTH, (ActionEvent e) -> {
			updateAll();
		});
		timer.start();
	}
	
	public static void updateAll() {
		for (UpdatingPanel panel : panels) {
			panel.update();
		}
	}
	
	public UpdatingPanel(LayoutManager layout) {
		super(layout);
		panels.add(this);
	}
	
	public abstract void update();
}
