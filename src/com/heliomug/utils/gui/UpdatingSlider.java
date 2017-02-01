package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
public class UpdatingSlider extends JSlider {
	private Supplier<Integer> sup;	
	
	public UpdatingSlider(int min, int max, int init, Consumer<Integer> con, Supplier<Integer> sup) {
		super(JSlider.HORIZONTAL, min, max, init); 
		
		this.sup = sup;
		
		addChangeListener((ChangeEvent e) -> con.accept(getValue()));
	}
	
	public void paint(Graphics g) {
		setValue(sup.get());
		super.paint(g);
	}
}
