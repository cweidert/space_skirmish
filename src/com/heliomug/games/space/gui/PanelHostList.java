package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.games.space.server.CommandAddHost;
import com.heliomug.games.space.server.MasterHost;

@SuppressWarnings("serial")
public class PanelHostList extends JPanel {
	JPanel others;
	
	public PanelHostList() {
		super(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		others = new JPanel(new GridLayout(0, 2));
		add(others, BorderLayout.CENTER);
		
		JButton hostButton = new JButton("Host My Own Game") {
			@Override
			public void paintComponent(Graphics g) {
				setEnabled(SpaceFrame.getServer() == null);
				super.paintComponent(g);
			}
		};
		hostButton.addActionListener((ActionEvent e) -> {
			ThingClient<ArrayList<ThingHost<SpaceGame>>> masterClient = SpaceFrame.getMasterClient(); 
			ThingHost<SpaceGame> server = SpaceFrame.getServer(); 
			ThingClient<SpaceGame> client = SpaceFrame.getClient(); 
			if (server == null && client == null) {
				ThingHost<SpaceGame> myServer = new ThingHost<SpaceGame>(new SpaceGame("[nom]"), MasterHost.GAME_PORT); 
				SpaceFrame.setServer(myServer);
				myServer.start();
				if (masterClient != null) {
					masterClient.sendCommand(new CommandAddHost(myServer));
				}
				ThingClient<SpaceGame> myClient = new ThingClient<SpaceGame>(myServer);
				SpaceFrame.setClient(myClient);
				myClient.start((Boolean b) -> {});
			}
		});
		add(hostButton, BorderLayout.SOUTH);
	}
	
	public void update() {
        others.removeAll();
		ThingClient<ArrayList<ThingHost<SpaceGame>>> masterClient = SpaceFrame.getMasterClient(); 
		ThingClient<SpaceGame> client = SpaceFrame.getClient(); 

		if (masterClient != null) {
			List<ThingHost<SpaceGame>> li = masterClient.getThing();
			if (li != null && li.size() > 0) {
				for (ThingHost<SpaceGame> host : li) {
					String gameString = host.getThing().toString();
					String addr = host.getAddress().toString();
					int port = host.getPort();
					String fmt = "<html><table>" + 
					"<tr><td>Game</td><td>%s</td></tr>" +
					"<tr><td>IP</td><td>%s</td></tr>" + 
					"<tr><td>Port</td><td>%d</td></tr></table></html>";
					others.add(new JLabel(String.format(fmt, gameString, addr, port)));
			        JButton button = new JButton("Join Game");
			        button.addActionListener((ActionEvent e) -> {
			        	if (client == null) {
			        		ThingClient<SpaceGame> newClient = new ThingClient<>(host);
			        		SpaceFrame.setClient(newClient);
			        		newClient.start();
			        	}
			        });
			        button.setEnabled(client == null);
			        others.add(button);
				}
			} else {
				others.add(new JLabel("no games available online"));
			}
		} else {
			others.add(new JLabel("can't reach home.heliomug.com"));
		}
		revalidate();
	}
}
