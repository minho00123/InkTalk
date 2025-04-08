package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chatboard extends JPanel implements ActionListener {
	JTextArea chatArea; 
	JTextField inputField; 
	JButton sendButton;
	
	static Socket socket = null;
	static BufferedWriter bw = null;
	
	public Chatboard () {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		
		
		try {
			socket = new Socket("172.30.1.31", 5555);
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
		
//			//--------
//			is = socket.getInputStream();
//			br = new BufferedReader(new InputStreamReader(is));
//			
//			while(true) {
//				String msg = br.readLine(); //서버에서 보내준 메세지들
//				chatArea.setText(chatArea.getText() + "\n"+msg);
//				chatArea.revalidate();
//				
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String before = inputField.getText();
		String msg = e.getActionCommand();
		
		try {
			bw.write(msg);
			bw.newLine();
			bw.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		inputField.setText("");
		chatArea.revalidate();
		
	}

}