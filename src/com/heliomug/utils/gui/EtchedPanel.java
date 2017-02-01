package com.heliomug.utils.gui;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class EtchedPanel extends JPanel {
    public static void addEtch(JPanel panel, String title) {
        Border etched = BorderFactory.createEtchedBorder();
        Border border = BorderFactory.createTitledBorder(etched, title);
        panel.setBorder(border);
    }
	
	public EtchedPanel(String title, LayoutManager manager) {
        super(manager);
        addEtch(this, title);
    }
}
