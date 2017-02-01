package com.heliomug.utils.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import com.heliomug.games.space.gui.SpaceFrame;

@SuppressWarnings("serial")
public class ColorButton extends JButton {

	private Color color;
	
	public ColorButton() {
		super("   ");
		setFocusable(false);
		resetColor();
		addActionListener((ActionEvent e) -> {
			Color newColor = JColorChooser.showDialog(
                    SpaceFrame.getFrame(),
                    "Choose Player Color",
                    color);
			setColor(newColor);
		});
	}
	
	public void resetColor() {
		setColor(Color.getHSBColor((float) Math.random(), 1.0f, 1.0f));
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
