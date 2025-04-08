package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Chatboard extends JPanel implements ActionListener {
	JTextArea chatArea;
	JTextField inputField;
	JButton sendButton;

	static BufferedWriter bw = null;
	static BufferedReader br = null;
	String msg;

	public Chatboard(Socket socket) {

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

		startReceivingMessages(socket);

	}

	public void actionPerformed(ActionEvent e) {

		String msg = inputField.getText();
		if (!msg.isEmpty()) {
			try {
				synchronized (bw) {
					bw.write(msg);
					bw.newLine();
					bw.flush();
				}

				inputField.setText("");
				chatArea.revalidate();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	private void startReceivingMessages(Socket socket) {
		new Thread(() -> {
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				while ((msg = br.readLine()) != null) {
					// 서버로부터 받은 메시지를 chatArea에 출력
					SwingUtilities.invokeLater(() -> {
						chatArea.append(msg + "\n");
						chatArea.revalidate();
					});
				}
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "서버와 연결에 문제가 발생했습니다.");
				e1.printStackTrace();
			}
		}).start();
	}

	/**
	 * 애플리케이션 종료 시 소켓, 스트림 닫기 메서드
	 */
	public void closeConnection() {
		try {
			if (bw != null) {
				bw.close();
			}
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}