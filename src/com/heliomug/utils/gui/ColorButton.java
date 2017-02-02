package com.heliomug.utils.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import com.heliomug.games.space.gui.FrameSpace;
import com.heliomug.games.space.gui.Manager;

@SuppressWarnings("serial")
public class ColorButton extends JButton {
	private static final List<Color> coolColors = Arrays.asList(new Color[] {
			Color.ORANGE,
			Color.YELLOW,
			Color.GREEN,
			new Color(0, 127, 0),
			Color.CYAN,
			Color.BLUE,
			new Color(127, 0, 127),
			Color.MAGENTA,
			Color.PINK,
			Color.GRAY,
			Color.WHITE,
	});
	
	private Color color;
	
	public ColorButton() {
		super("   ");
		setFocusable(false);
		resetColor();
		addActionListener((ActionEvent e) -> {
			Color newColor = JColorChooser.showDialog(
					FrameSpace.getFrame(),
                    "Choose Player Color",
                    color);
			setColor(newColor);
		});
	}
	
	public void resetColor() {
		List<Color> colors = new ArrayList<>();
		colors.addAll(coolColors);
		colors.removeAll(Manager.getPlayerColors());
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
