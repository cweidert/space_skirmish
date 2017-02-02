package com.heliomug.utils.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public abstract class UpdatingScrollPanel extends UpdatingPanel {
	private JPanel listPanel;
	
	public UpdatingScrollPanel(String title) {
		super(new BorderLayout());
		PanelUtils.addEtch(this, title);
		
		listPanel = new JPanel(new GridBagLayout());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(listPanel, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}

	public JPanel getListPanel() {
		return listPanel;
	}
}
