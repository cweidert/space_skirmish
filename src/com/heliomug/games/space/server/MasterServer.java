package com.heliomug.games.space.server;

import java.util.concurrent.CopyOnWriteArrayList;

import com.heliomug.utils.server.Server;

public class MasterServer extends Server<CopyOnWriteArrayList<GameAddress>> {
	private static final long serialVersionUID = 9150953071039679780L;

	public static final String MASTER_HOST = "http://192.168.1.6";//http://home.heliomug.com";
	public static final int MASTER_PORT = 27961;
	public static final int GAME_PORT = 27960;
	
	public MasterServer(CopyOnWriteArrayList<GameAddress> serverList) {
		super(serverList, MASTER_PORT);
	}

	public static void startMasterHost() {
		CopyOnWriteArrayList<GameAddress> serverList = new CopyOnWriteArrayList<>();
		MasterServer masterHost = new MasterServer(serverList);
		masterHost.start();
	}
}
