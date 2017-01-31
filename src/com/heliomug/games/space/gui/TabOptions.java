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
import com.heliomug.games.space.Game;
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
				SpaceFrame.getServer().getThing().getOptions().setPlanet(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isPlanet();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Gravity", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setGravity(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isGravity();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Wrap", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setWrap(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isWrap();
		});
		optionPanel.add(box);
		box = new UpdatingCheckBox("Auto Restart", (Boolean b) -> {
			if (SpaceFrame.getServer() != null) {
				SpaceFrame.getServer().getThing().getOptions().setAutoRestart(b);
			}
		}, () -> {
			return SpaceFrame.getServer() != null && SpaceFrame.getServer().getThing().getOptions().isAutoRestart();
		});
		optionPanel.add(box);
		add(optionPanel, BorderLayout.SOUTH);
	}
	
	public void update() {
		ThingHost<Game> server = SpaceFrame.getServer();
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
