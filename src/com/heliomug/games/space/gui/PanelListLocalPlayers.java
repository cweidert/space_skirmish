package com.heliomug.games.space.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.games.space.Player;
import com.heliomug.games.space.ShipSignal;
import com.heliomug.utils.gui.PanelUtils;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class PanelListLocalPlayers extends UpdatingPanel {
	public PanelListLocalPlayers() {
		super(new GridBagLayout());
		PanelUtils.addEtch(this, "Local Player List");
	}
	
	@Override
	public void update() {
        removeAll();
		List<Player> players = SpaceFrame.getLocalPlayers();
        if (players != null & players.size() > 0) {
        	JLabel label;
    		GridBagConstraints cons = new GridBagConstraints();
    		cons.fill = GridBagConstraints.BOTH;
    		cons.gridy = 0;
    		cons.weightx = 1;
    		
    		cons.gridx = 0;
    		add(new JLabel("Name", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Left", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Right", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Forward", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Back", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Fire", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Color", JLabel.CENTER), cons);
    		cons.gridx++;
    		add(new JLabel("Remove", JLabel.CENTER), cons);
    		
    		cons.gridy++;
    		
			for (Player player : players) {
				cons.gridx = 0;
				label = new JLabel(player.getName(), JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				add(label, cons);
	    		cons.gridx++;
				add(new KeyDisplay(player, ShipSignal.TURN_LEFT), cons);
	    		cons.gridx++;
				add(new KeyDisplay(player, ShipSignal.TURN_RIGHT), cons);
	    		cons.gridx++;
				add(new KeyDisplay(player, ShipSignal.FORWARD), cons);
	    		cons.gridx++;
				add(new KeyDisplay(player, ShipSignal.BACKWARDS), cons);
	    		cons.gridx++;
				add(new KeyDisplay(player, ShipSignal.FIRE), cons);
	    		cons.gridx++;
				JPanel panel = new JPanel();
				panel.setBackground(player.getColor());
				add(panel, cons);
	    		cons.gridx++;
	    		JButton button = new UpdatingButton("X", () -> true, () -> {
					SpaceFrame.removeLocalPlayer(player);
				});
				add(button, cons);
				cons.gridy++;
			}
		} else {
			add(new JLabel("no players yet!"));
		}
        revalidate();
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
