package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.heliomug.game.server.ThingClient;
import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Game;
import com.heliomug.games.space.server.MasterClient;
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class PanelListHost extends UpdatingPanel {
	public PanelListHost() {
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
				label = new JLabel("IP Address", JLabel.CENTER);
				add(label, cons);
				cons.gridx = 2;
				label = new JLabel("Port", JLabel.CENTER);
				add(label, cons);
				cons.gridy++;
				for (ThingHost<Game> host : li) {
					String gameString = host.getThing().getName();
					String addr = host.getAddress().toString();
					int port = host.getPort();
					cons.gridx = 0;
					label = new JLabel(gameString, JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
					cons.gridx = 1;
					label = new JLabel(addr, JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
					cons.gridx = 2;
					label = new JLabel(String.valueOf(port), JLabel.CENTER);
					label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					add(label, cons);
			        JButton button = new UpdatingButton("Join Game", () -> client == null, () -> {
			        	if (client == null) {
			        		ThingClient<Game> newClient = new ThingClient<>(host);
			        		Frame.setClient(newClient);
			        		newClient.start();
			        	}
			        });
			        cons.gridx = 3;
			        add(button, cons);
			        cons.gridy++;
				}
			} else {
				add(new JLabel("no games available online"));
			}
		} else {
			add(new JLabel("can't reach home.heliomug.com"));
		}
		revalidate();
    }
}
