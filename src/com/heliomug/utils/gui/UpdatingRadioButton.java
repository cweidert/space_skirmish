package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class UpdatingRadioButton extends JRadioButton {
	Supplier<Boolean> sup;
	
	public UpdatingRadioButton(String text, ButtonGroup group, Supplier<Boolean> sup, Runnable com) {
		super(text);
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
