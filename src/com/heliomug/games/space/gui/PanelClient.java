package com.heliomug.games.space.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
class PanelClient extends UpdatingPanel {
	JPanel panel;
	
	public PanelClient() {
		super(new FlowLayout());
		PanelUtils.addEtch(this, "My Current Connected Game");
		panel = new JPanel();
	}
	
	@Override
	public void update() {
		panel.removeAll();
		if (Session.isClient()) {
			panel.add(new JLabel(String.valueOf(Session.getClientAddress())));
		} else {
			panel.add(new JLabel("you are not a client of anyone"));
		}
		repaint();
	}
}
