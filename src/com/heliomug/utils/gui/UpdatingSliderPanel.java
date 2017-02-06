package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
public class UpdatingSliderPanel extends JPanel {
	private Supplier<Integer> sup;	
	private JSlider slider;
	private JLabel val;
	private Function<Integer, String> fmt;
	private Function<Integer, Integer> conv;

	public UpdatingSliderPanel(
			String text, 
			int min, int max, int init, 
			Consumer<Integer> con, 
			Supplier<Integer> sup) {
		this(text, min, max, init, con, sup, (Integer i) -> String.valueOf(i), (Integer i) -> i);
	}
	
	public UpdatingSliderPanel(
			String text, 
			int min, int max, int init, 
			Consumer<Integer> con, 
			Supplier<Integer> sup, 
			Function<Integer, String> fmt,
			Function<Integer, Integer> conv) {
		super(new GridBagLayout());

		this.fmt = fmt;
		this.conv = conv;
		this.sup = sup;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		JLabel label = new JLabel(text);
		slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
		slider.addChangeListener((ChangeEvent e) -> {
			con.accept(slider.getValue());
			repaint();
		});
		add(label, gbc);
		gbc.weightx = 1;
		add(slider, gbc);
		val = new JLabel(String.valueOf(sup.get()));
		gbc.weightx = 0;
		add(val, gbc);
	}
	
	public void paint(Graphics g) {
		slider.setValue(sup.get());
		val.setText(fmt.apply(conv.apply(sup.get())));
		super.paint(g);
	}
}
