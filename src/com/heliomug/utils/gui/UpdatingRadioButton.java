package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class UpdatingRadioButton extends JRadioButton {
	private static final int NO_KEY = 0;
	
	Supplier<Boolean> sup;
	
	public UpdatingRadioButton(String text, ButtonGroup group, Supplier<Boolean> sup, Runnable com) {
		this(text, NO_KEY, group, sup, com);
	}

	public UpdatingRadioButton(String text, int key, ButtonGroup group, Runnable com) {
		this(text, key, group, () -> true, com);
	}
	
	public UpdatingRadioButton(String text, int key, ButtonGroup group, Supplier<Boolean> sup, Runnable com) {
		super(text);
		if (key != NO_KEY) {
			setMnemonic(key);
		}
		group.add(this);
		this.sup = sup;
		addActionListener((ActionEvent e) -> com.run());
	}
	
	@Override
	public void paint(Graphics g) {
		setEnabled(sup.get());
		super.paint(g);
	}
}
