package com.heliomug.games.space.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Test extends JFrame implements ActionListener {
	int height = 0;
	
	public Test() {
		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.RED);
				g2.fill(new Ellipse2D.Double(100, height, 20, 20));
			}
		};
		panel.setPreferredSize(new Dimension(300, 300));
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		height += 1;
		repaint();
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		EventQueue.invokeLater(() -> {
			new Timer(1, test).start();
		});
		
	}
}
