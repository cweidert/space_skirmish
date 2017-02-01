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
import com.heliomug.utils.gui.EtchedPanel;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingPanel;

@SuppressWarnings("serial")
public class PanelListLocalPlayer extends UpdatingPanel {
	public PanelListLocalPlayer() {
		super(new GridBagLayout());
		EtchedPanel.addEtch(this, "Local Player List");
	}
	
	@Override
	public void update() {
        removeAll();
		List<Player> players = Frame.getLocalPlayers();
        if (players != null & players.size() > 0) {
        	JLabel label;
    		GridBagConstraints cons = new GridBagConstraints();
    		cons.fill = GridBagConstraints.BOTH;
    		cons.gridy = 0;
    		cons.weightx = 1;
    		
    		cons.gridx = 0;
    		add(new JLabel("Name", JLabel.CENTER), cons);
    		cons.gridx = 1;
    		add(new JLabel("Left", JLabel.CENTER), cons);
    		cons.gridx = 2;
    		add(new JLabel("Right", JLabel.CENTER), cons);
    		cons.gridx = 3;
    		add(new JLabel("Boost", JLabel.CENTER), cons);
    		cons.gridx = 4;
    		add(new JLabel("Fire", JLabel.CENTER), cons);
    		cons.gridx = 5;
    		add(new JLabel("Color", JLabel.CENTER), cons);
    		
    		cons.gridy++;
    		
			for (Player player : players) {
				cons.gridx = 0;
				label = new JLabel(player.toString(), JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				add(label, cons);
				cons.gridx = 1;
				add(new KeyDisplay(player, ShipSignal.TURN_LEFT), cons);
				cons.gridx = 2;
				add(new KeyDisplay(player, ShipSignal.TURN_RIGHT), cons);
				cons.gridx = 3;
				add(new KeyDisplay(player, ShipSignal.ACCEL_ON), cons);
				cons.gridx = 4;
				add(new KeyDisplay(player, ShipSignal.FIRE), cons);
				cons.gridx = 5;
				JPanel panel = new JPanel();
				panel.setBackground(player.getColor());
				add(panel, cons);
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
			ControlConfig controls = Frame.getControlConfig(player);

			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			JButton button = new UpdatingButton(controls.getKeyString(sig), () -> true, () -> {
				JDialog dialog = new JDialog(Frame.getFrame(), "Key Assignment");
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
