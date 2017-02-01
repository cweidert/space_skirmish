package com.heliomug.games.space.gui;

import java.awt.EventQueue;

import com.heliomug.games.space.server.MasterServer;

public class MainSpace {
	public static void main(String[] args) {
		MasterServer.startMasterHost();

		EventQueue.invokeLater(() -> {
			SpaceFrame frame = SpaceFrame.getFrame();
			frame.setVisible(true);
		});
	}
}
