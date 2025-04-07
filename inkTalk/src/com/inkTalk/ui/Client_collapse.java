package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Client_collapse extends JFrame implements ActionListener{
	LoginUi loginui;
	Whiteboard whiteboard;
	JPanel draw_chat;
	CardLayout cl;
	JPanel masterboard;
	
	public Client_collapse() {
		
	masterboard = new JPanel();
	cl = new CardLayout();
	masterboard.setLayout(cl);
	
	loginui = new LoginUi();
	draw_chat = new JPanel(new BorderLayout());
	
	whiteboard = new Whiteboard();
	Chatboard chatting = new Chatboard();
	draw_chat.add(whiteboard, BorderLayout.CENTER);
	draw_chat.add(chatting, BorderLayout.EAST);
	
	masterboard.add(loginui, "login");
	masterboard.add(draw_chat, "drawchat");
	
	add(masterboard);
	
	loginui.loginButton.addActionListener(this);
	
	setSize(1200,700);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setVisible(true);
	
	
	cl.show(masterboard, "login");
	}
	
	public static void main(String[] args) {
		 SwingUtilities.invokeLater(Client_collapse::new);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==loginui.loginButton){
		cl.show(masterboard, "drawchat");
	}else if(e.getSource()==whiteboard.walkout) {
		whiteboard.actionPerformed(e);
	}
}
}
