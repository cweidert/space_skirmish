package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;

@SuppressWarnings("serial")
class ButtonColor extends JButton {
	private static final List<Color> primaryColors = Arrays.asList(new Color[] {
			new Color(0xff, 0xff, 0xff),
			new Color(0xff, 0xff, 0x00),
			new Color(0xff, 0x80, 0x00),
			new Color(0x80, 0xff, 0x00),
			new Color(0xff, 0x00, 0xff),
			new Color(0x00, 0xff, 0xff),
			new Color(0x00, 0x80, 0xff),
	});
	
	private static final List<Color> secondaryColors = Arrays.asList(new Color[] {
			//new Color(0x00, 0x64, 0x00), no dark green with grass
			new Color(0x80, 0x00, 0x00),
			new Color(0x80, 0x80, 0x00),
			new Color(0x80, 0x80, 0x80),
			new Color(0x80, 0x00, 0xff),
			new Color(0x00, 0x00, 0xff),
	});
	
	private Color color;
	
	public ButtonColor() {
		super("   ");
		setFocusable(false);	
		resetColor();
		addActionListener((ActionEvent e) -> {
			Color newColor = JColorChooser.showDialog(
					Frame.getFrame(),
                    "Choose Player Color",
                    color);
			setColor(newColor);
		});
	}
	
	public void resetColor() {
		List<Color> colors = new ArrayList<>();
		colors.addAll(primaryColors);
		colors.removeAll(Session.getPlayerColors());
		if (colors.size() == 0) {
			colors.addAll(secondaryColors);
			colors.removeAll(Session.getPlayerColors());
		}
		if (colors.size() == 0) {
			setColor(Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random()));
		} else {
			setColor(colors.get((int)(Math.random() * colors.size())));
		}
	}
	
	public void setColor(Color c) {
		color = c;
		setBackground(color);
		repaint();
	}
	
	public Color getColor() {
		return color;
	}
}
