package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.util.function.Supplier;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class UpdatingLabel extends JLabel {
	private Supplier<String> sup;
	
	public UpdatingLabel(String str, Supplier<String> sup) {
		super(str);
		this.sup = sup;
	}
	
	@Override
	public void paint(Graphics g) {
		setText(sup.get());
		super.paint(g);
	}
	
}
