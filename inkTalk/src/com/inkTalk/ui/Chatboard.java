package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.inkTalk.Message;

public class Chatboard extends JPanel implements ActionListener {
	JTextArea chatArea;
	JTextField inputField;
	JButton sendButton;

	ObjectOutputStream oos = null;
	String msg;
	String nickName;
	Socket socket;
	public Chatboard(Socket socket, String nickName) {
		this.nickName = nickName;
		this.socket = socket;
		
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
		sendButton = new JButton("전송");
		sendButton.setBackground(new Color(1, 13, 38));
		sendButton.setForeground(Color.WHITE);
		sendButton.addActionListener(this);

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		this.add(chatPanel, BorderLayout.CENTER);
		this.add(inputPanel, BorderLayout.SOUTH);

		
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		startReceivingMessages(socket);
	}

	public void actionPerformed(ActionEvent e) {

		String msg = inputField.getText();
		if (!msg.isEmpty()) {
			try {
				Message message = new Message(nickName, msg);
				oos.writeObject(message);
				oos.flush();
				
				inputField.setText("");
				inputField.setText(" ");
				chatArea.revalidate();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	private void startReceivingMessages(Socket socket) {
		new Thread(() -> {
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				while (true) {
					// 서버로부터 받은 메시지를 chatArea에 출력
					Object receivedObject = ois.readObject();
					if(receivedObject instanceof Message) {
						Message msg = (Message)receivedObject;
						SwingUtilities.invokeLater(() -> {
							chatArea.append(msg.getNickName()+" : "+msg.getMsg()+ "\n"); //이름 : 내용으로 보내기
							chatArea.revalidate();
						});
					}

				}
			} catch (IOException e1) {
//				JOptionPane.showMessageDialog(this, "서버와 연결에 문제가 발생했습니다.");
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * 애플리케이션 종료 시 소켓, 스트림 닫기 메서드
	 */
	public void closeConnection() {
		try {
			if (oos != null) {
				oos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}