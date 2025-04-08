package com.inkTalk.ui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ClientMain extends JFrame implements ActionListener {
	private Socket socket;

	JPanel masterBoard;
	LoginUi loginUi;
	CardLayout cardLayout;
	Board board;
	private static String userNickName;

	public ClientMain() {
		try {
			socket = new Socket("172.30.1.22", 5555);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		masterBoard = new JPanel();
		cardLayout = new CardLayout();
		masterBoard.setLayout(cardLayout);

		loginUi = new LoginUi();
//		userNickName = loginUi.getUserNickName();
		board = new Board(this, socket, userNickName);

		masterBoard.add(loginUi, "login");
		masterBoard.add(board, "drawChat");

		loginUi.loginButton.addActionListener(this);

		cardLayout.show(masterBoard, "login");
		add(masterBoard);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ClientMain::new);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginUi.loginButton) {
			cardLayout.show(masterBoard, "drawChat");
		} else if (e.getSource() == board.exit) {
			board.actionPerformed(e);
		}
	}
}