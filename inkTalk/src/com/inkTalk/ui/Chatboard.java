package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chatboard extends JPanel implements ActionListener {
	JTextArea chatArea;
	JTextField inputField;
	JButton sendButton;

	public Chatboard() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 800));
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.setBackground(new Color(209, 229, 240));
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(chatArea);
		chatPanel.add(scroll, BorderLayout.CENTER);

		chatArea.setBackground(new Color(209, 229, 240));
		inputField = new JTextField();

		inputField.setBackground(new Color(169, 168, 217));
		sendButton = new JButton("����");
		sendButton.setBackground(new Color(1, 13, 38));
		sendButton.setForeground(Color.WHITE);
		sendButton.addActionListener(this);

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		this.add(chatPanel, BorderLayout.CENTER);
		this.add(inputPanel, BorderLayout.SOUTH);

	}
//	public static void main(String[] args) {
//		new Chatboard();

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}