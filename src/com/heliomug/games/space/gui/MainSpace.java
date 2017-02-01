package com.heliomug.games.space.gui;

import java.awt.EventQueue;

import com.heliomug.games.space.server.MasterHost;

public class MainSpace {
	public static void main(String[] args) {
		MasterHost.startMasterHost();
		EventQueue.invokeLater(() -> {
			Frame frame = Frame.getFrame();
			frame.setVisible(true);
		});
	}
}
