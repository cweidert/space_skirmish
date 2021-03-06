package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.heliomug.games.space.server.GameAddress;
import com.heliomug.utils.gui.UpdatingButton;
import com.heliomug.utils.gui.UpdatingScrollPanel;

@SuppressWarnings("serial")
class PanelListHosts extends UpdatingScrollPanel {
	JTextField addressBox;
	JSpinner portBox;
	JButton joinCustomButton;
	
	public PanelListHosts() {
		super("Host List");
		
		addressBox = new JTextField("");
		portBox = new JSpinner(new SpinnerNumberModel(Session.GAME_PORT, 1, 65535, 1));
        portBox.setEditor(new JSpinner.NumberEditor(portBox, "#"));
		joinCustomButton = getJoinCustomButton();
	}
	
	
	public JButton getJoinCustomButton() {
		JButton button = new UpdatingButton("Join", () -> {
			InetAddress address;
			try {
				String urlString = addressBox.getText();
				if (!addressBox.getText().startsWith("http://")) {
					urlString = "http://" + urlString;
				}
				address = InetAddress.getByName(new URL(urlString).getHost());
				int port = (int)portBox.getValue();
				GameAddress gameAddress = new GameAddress(address, port); 
				Session.joinGame(gameAddress);
			} catch (UnknownHostException | MalformedURLException e) {
				String message = "That host can't be found.";
				JOptionPane.showMessageDialog(Frame.getFrame(), message, "Whoops", JOptionPane.WARNING_MESSAGE);
			}
		});
		return button;
	}
	
	public void update() {
		boolean isAddressFocused = addressBox.isFocusOwner();
		boolean isPortBoxFocused = portBox.isFocusOwner();
				
		JPanel hostPanel = getListPanel(); 

		hostPanel.removeAll();
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.gridy = 0;
		cons.weightx = 1;
		
		JLabel label;
		cons.gridx = 0;
		label = new JLabel("Name", JLabel.CENTER);
		hostPanel.add(label, cons);
		cons.gridx = 1;
		label = new JLabel("External IP Address", JLabel.CENTER);
		hostPanel.add(label, cons);
		cons.gridx = 2;
		label = new JLabel("Lan IP Address", JLabel.CENTER);
		hostPanel.add(label, cons);
		cons.gridx = 3;
		label = new JLabel("Port", JLabel.CENTER);
		hostPanel.add(label, cons);
		cons.gridy++;

		List<GameAddress> li = Session.getGameAddressList();
		if (li != null && li.size() > 0) {
			for (GameAddress gameAddress : li) {
				String gameString = gameAddress.getName();
				String externalAddress = gameAddress.getAddress().getExternalAddress().toString();
				String lanAddress = gameAddress.getAddress().getLanAddress().toString();
				int port = gameAddress.getPort();
				cons.gridx = 0;
				label = new JLabel(gameString, JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hostPanel.add(label, cons);
				cons.gridx++;
				label = new JLabel(externalAddress, JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hostPanel.add(label, cons);
				cons.gridx++;
				label = new JLabel(lanAddress, JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hostPanel.add(label, cons);
				cons.gridx++;
				label = new JLabel(String.valueOf(port), JLabel.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hostPanel.add(label, cons);
				cons.gridx++;
		        JButton button = new UpdatingButton("Join", () -> {
		        	Session.joinGame(gameAddress);
		        });
		        hostPanel.add(button, cons);
		        cons.gridy++;
			}
		} 
		cons.gridx = 0;
		label = new JLabel("[Unlisted]", JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		hostPanel.add(label, cons);
		cons.gridx++;
		cons.gridwidth = 2;
		hostPanel.add(addressBox, cons);
		cons.gridx += 2;
		cons.gridwidth = 1;
		hostPanel.add(portBox, cons);
        cons.gridx++;
        hostPanel.add(joinCustomButton, cons);
        cons.gridy++;
        hostPanel.revalidate();
        repaint();
		
		if (isAddressFocused) {
			addressBox.requestFocusInWindow();
		}
		if (isPortBoxFocused) {
			portBox.requestFocusInWindow();
		}
	}
}
