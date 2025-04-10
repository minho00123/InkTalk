package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.inkTalk.app.AppController;
import com.inkTalk.domain.User;
import com.inkTalk.jdbc.JDBCTemplate;

public class LoginUi extends JPanel implements ActionListener {
	private PreparedStatement pstmt;
	private ResultSet rs;

	private AppController controller;
	public JButton loginButton, signUpButton;
	JTextField idField;
	JPasswordField pwField;
	JLabel error;

	public LoginUi(AppController controller) {
		this.controller = controller;

		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200, 800));

		// 전체 배경 패널
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.setBackground(new Color(209, 229, 240));
		backgroundPanel.setPreferredSize(new Dimension(1200, 800));

		// 로고 이미지
		ImageIcon logoIcon = new ImageIcon("src/resources/images/logo.png");
		JLabel logoLabel = new JLabel(logoIcon);
		logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(backgroundPanel);

		// 로그인 패널
		JPanel loginPanel = new JPanel(new GridBagLayout());
		loginPanel.setPreferredSize(new Dimension(350, 150));
		loginPanel.setBackground(new Color(179, 206, 224));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 10, 5, 10);

		// 닉네임
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		loginPanel.add(new JLabel("닉네임"), gbc);

		idField = new JTextField(12);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		loginPanel.add(idField, gbc);
		gbc.gridwidth = 1;
		idField.addActionListener(this);

		// 비밀번호
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		loginPanel.add(new JLabel("비밀번호"), gbc);

		pwField = new JPasswordField(12);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		loginPanel.add(pwField, gbc);
		pwField.addActionListener(this);

		// 로그인 버튼
		loginButton = new JButton("로그인");
		loginButton.setBackground(new Color(11, 29, 49));
		loginButton.setForeground(Color.WHITE);

		int totalHeight = pwField.getPreferredSize().height * 2 + 20;
		loginButton.setPreferredSize(new Dimension(70, totalHeight));
		loginButton.addActionListener(this);

		// 로그인 버튼을 2행을 차지하게 설정
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(0, 10, 0, 0);
		loginPanel.add(loginButton, gbc);

		gbc.gridheight = 1;
		// 회원가입 버튼 (아래 줄, 중앙 정렬)
		signUpButton = new JButton("회원가입");
		signUpButton.setBackground(new Color(11, 29, 49));
		signUpButton.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(signUpButton, gbc);
		signUpButton.addActionListener(this);

		error = new JLabel("");
		error.setForeground(Color.RED);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(error, gbc);

		// 수직 정렬을 위한 박스
		JPanel verticalBox = new JPanel();
		verticalBox.setLayout(new BoxLayout(verticalBox, BoxLayout.Y_AXIS));
		verticalBox.setOpaque(false);
		verticalBox.add(logoLabel);
		verticalBox.add(Box.createVerticalStrut(30));
		verticalBox.add(loginPanel);

		backgroundPanel.add(verticalBox, new GridBagConstraints());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		error.setText("");
		String pw = new String(pwField.getPassword()).trim();
		String nickName = new String(idField.getText()).trim();
		if (e.getSource() == loginButton) {

			DB_login(nickName, pw);
		} else if (e.getSource() == signUpButton) {

			controller.show("SIGNUP");
		} else if (e.getSource() == idField) {

			DB_login(nickName, pw);
		} else if (e.getSource() == pwField) {

			DB_login(nickName, pw);
		}

		idField.setText("");
		pwField.setText("");
	}

	public void DB_login(String nickName, String pw) {

		Connection conn = JDBCTemplate.getConnection();
		String sql = "SELECT USER_ID, PASSWORD FROM \"USER\" WHERE NICKNAME= ?";

		try {

			if (idField.getText().isEmpty() || pwField.getPassword().length == 0) {
				error.setText("아이디나 비밀번호를 입력하지 않으셨습니다.");
			} else if (idField.getText().isEmpty() && pwField.getPassword().length == 0) {
				error.setText("아이디와 비밀번호를 입력하신 후 로그인을 시도하세요.");
			} else {

				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, nickName);
				rs = pstmt.executeQuery();

				if (rs != null && rs.next()) {
					String dbPw = rs.getString("PASSWORD");
					if (dbPw.equals(pw)) {
						int userId = rs.getInt("USER_ID"); // 유저아이디 컬럼명 먼지 확인
						User user = new User(userId, nickName);
						controller.loginSuccess(user);
					} else {
						error.setText("비밀번호가 일치하지 않습니다. 다시 시도하세요.");
					}
				} else {
					error.setText("등록되지 않은 닉네임입니다. 회원가입 후 이용해주세요.");
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
	}
}
