package com.inkTalk.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
	
    public MainFrame() {
        AppController controller = new AppController();
        setContentPane(controller);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }	

}
