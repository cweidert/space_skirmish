package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetAddress;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.game.server.NetworkUtils;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class PanelListHosts extends UpdatingPanel {
	public PanelListHosts() {
		super(new GridBagLayout());
		EtchedPanel.addEtch(this, "Host List");
	}
	
	public void update() {
		removeAll();
		MasterClient masterClient = Frame.getMasterClient(); 
		ThingClient<Game> client = Frame.getClient(); 

		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.gridy = 0;
		cons.weightx = 1;
		
		if (masterClient != null) {
			List<ThingHost<Game>> li = masterClient.getThing();
			if (li != null && li.size() > 0) {
				JLabel label;
				cons.gridx = 0;
				label = new JLabel("Game", JLabel.CENTER);
				add(label, cons);
				cons.gridx = 1;
				label = new JLabel("External IP Address", JLabel.CENTER);
				add(label, cons);
				cons.gridx = 2;
				label = new JLabel("Lan IP Address", JLabel.CENTER);
				add(label, cons);
				cons.gridx = 3;
				label = new JLabel("Port", JLabel.CENTER);
				add(label, cons);
				cons.gridy++;
				for (ThingHost<Game> host : li) {
					String gameString = host.getThing().getName();
					String externalAddress = host.getExternalAddress().toString();
					String lanAddress = host.getLanAddress().toString();
					int port = host.getPort();
					cons.gridx = 0;
					label = new JLabel(gameString, JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
					cons.gridx = 1;
					label = new JLabel(externalAddress, JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
					cons.gridx = 2;
					label = new JLabel(lanAddress, JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
					cons.gridx = 3;
					label = new JLabel(String.valueOf(port), JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
			        JButton button = new UpdatingButton("Join Game", () -> client == null, () -> {
			        	if (client == null) {
			        		InetAddress hostAddress = host.getExternalAddress();
			        		if (hostAddress.equals(NetworkUtils.getExternalAddress())) {
			        			hostAddress = NetworkUtils.getLanAddress();
			        		}
			        		int hostPort = host.getPort();
			        		ThingClient<Game> newClient = new ThingClient<>(hostAddress, hostPort);
			        		Frame.setClient(newClient);
			        		newClient.start();
			        	}
			        });
			        cons.gridx = 4;
			        add(button, cons);
			        cons.gridy++;
				}
			} else {
				add(new JLabel("no games available online... yet"));
			}
		} else {
			add(new JLabel("can't reach master server at home.heliomug.com"));
		}
		revalidate();
    }
}
