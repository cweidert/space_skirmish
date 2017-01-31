package com.heliomug.games.space.gui;

import java.awt.EventQueue;

import com.heliomug.games.space.server.MasterHost;

public class MainSpace {
	public static void main(String[] args) {
		MasterHost.startMasterServer();
		EventQueue.invokeLater(() -> {
			SpaceFrame frame = SpaceFrame.getFrame();
			frame.setVisible(true);
		});
	}
}
