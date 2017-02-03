package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class UpdatingButton extends JButton {
	private static final int NO_KEY = 0;
	
	private Supplier<Boolean> sup;
	
	public UpdatingButton(String text, Runnable command) {
		this(text, NO_KEY, command);
	}
	
	public UpdatingButton(String text, int key, Runnable command) {
		this(text, key, () -> true, command);
	}

	public UpdatingButton(String text, Supplier<Boolean> sup, Runnable command) {
		this(text, NO_KEY, sup, command);
	}
	
	public UpdatingButton(String text, int key, Supplier<Boolean> sup, Runnable command) {
		super(text);
		this.sup = sup;

		if (key != NO_KEY) {
			setMnemonic(key);
		}
		
		setFocusable(false);
		addActionListener((ActionEvent e) -> {
			command.run();
			UpdatingPanel.updateAll();
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		setEnabled(sup.get());
		super.paintComponent(g);
	}
}
