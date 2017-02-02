package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.heliomug.games.space.server.GameAddress;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class PanelListHosts extends UpdatingPanel {
	public PanelListHosts() {
		super(new GridBagLayout());
		PanelUtils.addEtch(this, "Host List");
	}
	
	public void update() {
		removeAll();
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.gridy = 0;
		cons.weightx = 1;
		
		if (SpaceFrame.isConnectedToMasterHost()) {
			List<GameAddress> li = SpaceFrame.getGameAddressList();
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
				for (GameAddress gameAddress : li) {
					String gameString = gameAddress.getName();
					String externalAddress = gameAddress.getAddress().getExternalAddress().toString();
					String lanAddress = gameAddress.getAddress().getLanAddress().toString();
					int port = gameAddress.getPort();
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
			        JButton button = new UpdatingButton("Join Game", () -> {
			        	SpaceFrame.joinGame(gameAddress);
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
