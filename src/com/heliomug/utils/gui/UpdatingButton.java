package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class UpdatingButton extends JButton {
	private Supplier<Boolean> sup;
	
	public UpdatingButton(String text, Supplier<Boolean> sup, Runnable command) {
		super(text);
		this.sup = sup;

		setFocusable(false);
		addActionListener((ActionEvent e) -> {
			command.run();
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		setEnabled(sup.get());
		super.paintComponent(g);
	}
}
