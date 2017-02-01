package com.heliomug.utils.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class PanelUtils {
    public static void addEtch(JPanel panel, String title) {
        Border etched = BorderFactory.createEtchedBorder();
        Border border = BorderFactory.createTitledBorder(etched, title);
        panel.setBorder(border);
    }
}
