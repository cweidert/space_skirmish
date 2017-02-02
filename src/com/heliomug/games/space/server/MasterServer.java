package com.heliomug.games.space.server;

import java.util.concurrent.CopyOnWriteArrayList;

import com.heliomug.games.space.gui.Manager;
import com.heliomug.utils.server.Server;

public class MasterServer extends Server<CopyOnWriteArrayList<GameAddress>> {
	public MasterServer(CopyOnWriteArrayList<GameAddress> serverList) {
		super(serverList, Manager.MASTER_PORT);
	}

	public static void startMasterServer() {
		CopyOnWriteArrayList<GameAddress> serverList = new CopyOnWriteArrayList<>();
		MasterServer masterHost = new MasterServer(serverList);
		masterHost.start();
	}
}
