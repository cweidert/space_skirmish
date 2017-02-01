package com.heliomug.games.space.server;

import java.util.concurrent.CopyOnWriteArrayList;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;

public class MasterHost extends ThingHost<CopyOnWriteArrayList<ThingHost<Game>>> {
	private static final long serialVersionUID = 9150953071039679780L;

	public static final String MASTER_HOST = "http://home.heliomug.com";
	public static final int MASTER_PORT = 27961;
	public static final int GAME_PORT = 27960;
	
	public MasterHost(CopyOnWriteArrayList<ThingHost<Game>> hostList) {
		super(hostList, MASTER_PORT);
	}

	public static void startMasterHost() {
		CopyOnWriteArrayList<ThingHost<Game>> hostList = new CopyOnWriteArrayList<>();
		MasterHost masterHost = new MasterHost(hostList);
		masterHost.start();
		System.out.println("master server started");
	}
}
