package com.heliomug.games.space.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.heliomug.utils.server.NetworkUtils;
import com.heliomug.utils.server.Server;

public class MasterServer extends Server<CopyOnWriteArrayList<GameAddress>> {
	public static final String MASTER_HOST_HOME = "http://home.heliomug.com";
	public static final int MASTER_PORT = 27961;

	public MasterServer(CopyOnWriteArrayList<GameAddress> serverList) {
		super(serverList, MASTER_PORT);
	}

	public static MasterClient getClient() {
		InetAddress masterAddress;
		MasterClient masterClient;
		try {
			Logger.getGlobal().log(Level.INFO, "Trying to reach global game list @ " + MASTER_HOST_HOME + ".  ");
			masterAddress = InetAddress.getByName(new URL(MASTER_HOST_HOME).getHost());
			masterClient = new MasterClient(masterAddress, MASTER_PORT);
			masterClient.start();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.INFO, "Could not reach global game list.  ");
			try {
				masterAddress = InetAddress.getByName(NetworkUtils.getExternalAddress().getHostAddress());
				Logger.getGlobal().log(Level.INFO, "Trying to reach local game list @ " + masterAddress + ".");
				masterAddress = InetAddress.getByName(NetworkUtils.getExternalAddress().getHostAddress());
				masterClient = new MasterClient(masterAddress, MASTER_PORT);
				masterClient.start();
			} catch (IOException e1) {
				Logger.getGlobal().log(Level.INFO, "Could not reach local game list.  Starting my own game list server.");
				CopyOnWriteArrayList<GameAddress> serverList = new CopyOnWriteArrayList<>();
				MasterServer masterHost = new MasterServer(serverList);
				masterHost.start();
				try {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					masterAddress = InetAddress.getByName(NetworkUtils.getLanAddress().getHostAddress());
					masterClient = new MasterClient(masterAddress, MASTER_PORT);
					masterClient.start();
				} catch (IOException e2) {
					Logger.getGlobal().log(Level.WARNING, "Could not reach my own game list!");
					masterClient = null;
				}
			}
		}
		Logger.getGlobal().log(Level.INFO, "Connected to game list " + masterClient);
		return masterClient;
	}
}
