package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class UpdatingCheckBox extends JCheckBox {
    private static final int NO_KEY = 0;
	
	Supplier<Boolean> source;
    Supplier<Boolean> enabled;
    
    public UpdatingCheckBox(String title, Consumer<Boolean> dest, Supplier<Boolean> source) {
    	this(title, NO_KEY, () -> true, dest, source);
    }
    
    public UpdatingCheckBox(String title, int mne, Consumer<Boolean> dest, Supplier<Boolean> source) {
    	this(title, mne, () -> true, dest, source);
    }
    
    public UpdatingCheckBox(String title, Supplier<Boolean> enabled, Consumer<Boolean> dest, Supplier<Boolean> source) {
    	this(title, NO_KEY, enabled, dest, source);
    }

    public UpdatingCheckBox(String title, int mne, Supplier<Boolean> enabled, Consumer<Boolean> dest, Supplier<Boolean> source) {
        super(title);
        this.enabled = enabled;
        this.source = source;
        
        if (mne != NO_KEY) {
        	setMnemonic(mne);
        }
        
        setFocusable(false);
        
        addActionListener((ActionEvent e) -> {
                dest.accept(isSelected());
        });
    }
    
    @Override
    public void paint(Graphics g) {
        this.setSelected(source.get());
        this.setEnabled(enabled.get());
        super.paint(g);
    }
}
