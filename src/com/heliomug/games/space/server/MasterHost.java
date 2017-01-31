package com.heliomug.games.space.server;

import java.util.ArrayList;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.SpaceGame;

public class MasterHost extends ThingHost<ArrayList<ThingHost<SpaceGame>>> {
	private static final long serialVersionUID = 9150953071039679780L;

	public static final String MASTER_HOST = "home.heliomug.com";
	public static final int MASTER_PORT = 27961;
	public static final int GAME_PORT = 27960;
	
	public MasterHost(ArrayList<ThingHost<SpaceGame>> hostList) {
		super(hostList, MASTER_PORT);
	}

	public static void startMasterServer() {
		ArrayList<ThingHost<SpaceGame>> hostList = new ArrayList<>();
		MasterHost masterHost = new MasterHost(hostList);
		masterHost.start();
		System.out.println("master server started");
	}
}
