package com.heliomug.games.space.gui;

import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import com.heliomug.game.server.NetworkUtils;
import com.heliomug.games.space.server.MasterHost;

public class MainSpace {
	public static void main(String[] args) {
		try {
			InetAddress address = InetAddress.getByName(new URL(MasterHost.MASTER_HOST).getHost());
			if (address.equals(NetworkUtils.getExternalAddress())) {
				MasterHost.startMasterHost();
			}
		} catch (UnknownHostException | MalformedURLException e) {
			e.printStackTrace();
			System.out.println("error somehow");
			// guess we won't start the master host then
		}
		EventQueue.invokeLater(() -> {
			Frame frame = Frame.getFrame();
			frame.setVisible(true);
		});
	}
}
