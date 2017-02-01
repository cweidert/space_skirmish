package com.heliomug.utils.gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.heliomug.games.space.gui.Frame;

@SuppressWarnings("serial")
public abstract class UpdatingPanel extends JPanel {
	private static final int CYCLE_LENGTH = 1000;
	
	private static Timer timer;
	
	private static List<UpdatingPanel> panels;
	
	static {
		panels = new ArrayList<>();
		timer = new Timer(CYCLE_LENGTH, (ActionEvent e) -> {
			for (UpdatingPanel panel : panels) {
				panel.update();
			}
			Frame.getFrame().repaint();
		});
		timer.start();
	}
	
	public UpdatingPanel(LayoutManager layout) {
		super(layout);
		panels.add(this);
	}
	
	public abstract void update();
}
