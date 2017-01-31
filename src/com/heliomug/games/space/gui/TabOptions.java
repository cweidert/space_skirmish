package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.game.server.ThingHost;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.SpaceGame;
import com.heliomug.utils.gui.UpdatingCheckBox;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class TabOptions extends UpdatingPanel {
	JPanel totalPlayers;
	
	public TabOptions() {
		super(new BorderLayout());
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		totalPlayers = new JPanel(new GridLayout());
		add(totalPlayers, BorderLayout.CENTER);
		
		JPanel optionPanel = new JPanel(new GridLayout(0, 1));
		
		UpdatingCheckBox box;
		
		box = new UpdatingCheckBox("Planet", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().setPlanet(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().isPlanet();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Gravity", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().setGravity(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().isGravity();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().setWrap(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().isWrap();
		});
		optionPanel.add(box);
		add(optionPanel, BorderLayout.SOUTH);
	}
	
	public void update() {
		ThingHost<SpaceGame> server = SpaceFrame.getServer();
        totalPlayers.removeAll();
        if (server != null) {
        	List<Player> players = server.getThing().getPlayers();
			if (players != null & players.size() > 0) {
				for (Player player : players) {
			        totalPlayers.add(new JLabel(player.toString()));
				}
			} else {
				totalPlayers.add(new JLabel("no players yet!"));
			}
		} else {
			totalPlayers.add(new JLabel("no server!"));
		}
        revalidate();
	}
}
