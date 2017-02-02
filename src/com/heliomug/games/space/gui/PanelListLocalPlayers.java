package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.ControlConfig;
import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingScrollPanel;

@SuppressWarnings("serial")
public class PanelListLocalPlayers extends UpdatingScrollPanel {
	public PanelListLocalPlayers() {
		super("Local Player List");
	}
	
	@Override
	public void update() {
		JPanel playerPanel = getListPanel();
		playerPanel.removeAll();
		List<Player> players = SpaceFrame.getLocalPlayers();
        if (players != null & players.size() > 0) {
        	JLabel label;
    		GridBagConstraints cons = new GridBagConstraints();
    		cons.anchor = GridBagConstraints.NORTH;
    		cons.fill = GridBagConstraints.BOTH;
    		cons.gridy = 0;
    		cons.weightx = 1;
    		
    		cons.gridx = 0;
    		playerPanel.add(new JLabel("Name", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Left", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Right", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Forward", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Back", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Fire", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Color", JLabel.CENTER), cons);
    		cons.gridx++;
    		playerPanel.add(new JLabel("Remove", JLabel.CENTER), cons);
    		
    		cons.gridy++;
    		
			for (Player player : players) {
				cons.gridx = 0;
				label = new JLabel(player.getName(), JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				playerPanel.add(label, cons);
	    		cons.gridx++;
				playerPanel.add(new KeyDisplay(player, ShipSignal.TURN_LEFT), cons);
	    		cons.gridx++;
				playerPanel.add(new KeyDisplay(player, ShipSignal.TURN_RIGHT), cons);
	    		cons.gridx++;
				playerPanel.add(new KeyDisplay(player, ShipSignal.FORWARD), cons);
	    		cons.gridx++;
				playerPanel.add(new KeyDisplay(player, ShipSignal.BACKWARDS), cons);
	    		cons.gridx++;
				playerPanel.add(new KeyDisplay(player, ShipSignal.FIRE), cons);
	    		cons.gridx++;
				JPanel panel = new JPanel();
				panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				panel.setBackground(player.getColor());
				playerPanel.add(panel, cons);
	    		cons.gridx++;
	    		JButton button = new UpdatingButton("X", () -> {
					SpaceFrame.removeLocalPlayer(player);
				});
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    		playerPanel.add(button, cons);
				cons.gridy++;
			}
		} else {
			playerPanel.add(new JLabel("no players yet!"));
		}
        playerPanel.revalidate();
        revalidate();
        repaint();
	}
	
	private class KeyDisplay extends JPanel {
		public KeyDisplay(Player player, ShipSignal sig) {
			super(new BorderLayout());
			ControlConfig controls = SpaceFrame.getControlConfig(player);

			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			JButton button = new UpdatingButton(controls.getKeyString(sig), () -> true, () -> {
				JDialog dialog = new JDialog(SpaceFrame.getFrame(), "Key Assignment");
				String prompt = String.format("Press %s's button for %s", player.getName(), sig);
				dialog.add(new JLabel(prompt));
				dialog.setFocusable(true);
				dialog.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						controls.setKey(sig, e.getKeyCode());
						dialog.dispose();
					}
				});
				dialog.pack();
				dialog.setVisible(true);
			});
			add(button, BorderLayout.CENTER);
		}
	}
}
