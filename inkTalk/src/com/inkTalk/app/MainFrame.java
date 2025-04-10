package com.inkTalk.app;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

	public MainFrame() {
		AppController controller = new AppController();
		setContentPane(controller);
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage(getClass().getResource("/images/InkTalk.png"));
		setIconImage(img);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainFrame::new);
	}

}
