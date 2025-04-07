package com.inkTalk.client.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatController extends JFrame implements ActionListener{
	JTextArea chatArea;
	JTextField inputField;
	JButton sendButton;
	
	public ChatController () {
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.setBackground(new Color(209, 229, 240));
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatArea);
        chatArea.setBackground(new Color(209, 229, 240));
        inputField = new JTextField();
        
        inputField.setBackground(new Color(169, 168, 217));
        sendButton = new JButton("Àü¼Û");
        sendButton.setBackground(new Color(1, 13, 38));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(this);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        add(chatPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 700);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width / 2 - 550, kit.getScreenSize().height / 2 - 350);
        setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(ChatController::new);

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sendButton) {
			
		}
		
	}
}
