package com.heliomug.games.space.server;

import java.util.ArrayList;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;

public class MasterHost extends ThingHost<ArrayList<ThingHost<Game>>> {
	private static final long serialVersionUID = 9150953071039679780L;

	public static final String MASTER_HOST = "home.heliomug.com";
	public static final int MASTER_PORT = 27961;
	public static final int GAME_PORT = 27960;
	
	public MasterHost(ArrayList<ThingHost<Game>> hostList) {
		super(hostList, MASTER_PORT);
	}

	public static void startMasterServer() {
		ArrayList<ThingHost<Game>> hostList = new ArrayList<>();
		hostList.add(new ThingHost<Game>(new Game(), 1234));
		MasterHost masterHost = new MasterHost(hostList);
		masterHost.start();
		System.out.println("master server started");
	}
}
