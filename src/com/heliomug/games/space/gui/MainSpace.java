package com.heliomug.games.space.gui;

import java.awt.EventQueue;

import com.heliomug.games.space.server.MasterServer;

public class MainSpace {
	public static void main(String[] args) {
		MasterServer.startMasterServer();

		EventQueue.invokeLater(() -> {
			FrameSpace frame = FrameSpace.getFrame();
			frame.setVisible(true);
		});
	}
}
