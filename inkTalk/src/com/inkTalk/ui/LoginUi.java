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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginUi extends JPanel {

	public JButton loginButton, signUpButton;
	JTextField idField;
	JPasswordField pwField;
	JLabel error;
	
    public LoginUi() {
    	setLayout(new BorderLayout());
    	this.setPreferredSize(new Dimension(1200, 800));
    	
        // 전체 배경 패널
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(209, 229, 240)); // 연파랑
        backgroundPanel.setPreferredSize(new Dimension(1200,800));
        // 로고 이미지
        ImageIcon logoIcon = new ImageIcon("images/logo.jpg"); // 로고 이미지 경로
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(backgroundPanel);

     // 로그인 패널
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(350, 150));
        loginPanel.setBackground(new Color(179, 206, 224));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // 여백

        // 닉네임
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("닉네임"), gbc);

        idField = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(idField, gbc);
        gbc.gridwidth = 1; // 초기화

        // 비밀번호
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("비밀번호"), gbc);

        pwField = new JPasswordField(12);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(pwField, gbc);

        // 로그인 버튼 (같은 줄에 오른쪽 배치)
        loginButton = new JButton("로그인");
        loginButton.setBackground(new Color(11, 29, 49));
        loginButton.setForeground(Color.WHITE);

        // 필드 2개 높이 기준으로 버튼 높이 설정 (선택 사항, 수동 조절 가능)
        int totalHeight = pwField.getPreferredSize().height * 2 + 20;  // 실질적 버튼 세로크기 조절
        loginButton.setPreferredSize(new Dimension(70, totalHeight));

        // 로그인 버튼을 2행을 차지하게 설정
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.NORTH; // 위쪽 정렬
        gbc.insets = new Insets(0, 10, 0, 0);  // 왼쪽 여백
        loginPanel.add(loginButton, gbc);

       
        gbc.gridheight = 1; // gridheight는 다시 1로 돌려놔야 다른 컴포넌트에 영향 없음
        
        // 회원가입 버튼 (아래 줄, 중앙 정렬)
        signUpButton = new JButton("회원가입");
        signUpButton.setBackground(new Color(11, 29, 49));
        signUpButton.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(signUpButton, gbc);
        
        error = new JLabel("에러표시");
        error.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3;
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
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.getImage("images/logoicon.jpg");
    }
    
    

//    public static void main(String[] args) {
//       new LoginUI();
    }//그냥 new loginUI로 처리할 수도 있으나, 그러면 나중에 서버와 통신하고 응답을 대기받는 상황이 발생시 통신이 오래걸리면 UI가 멈출 수 있으므로
    //이를 백그라운드에서 수행 후 결과만 EDT로 받는게 나음 + 스윙은 단일스레드가 기본이라 충돌을 피하려면 EDT에서 수행하는걸 권장한다고(?)
    //EDT : 이벤트를 실행하는 스레드 : 일반적인 스윙 애플리케이션의 main 메소드는 GUI를 구동하는 Runnable 객체를 생성하여, event dispatch thread에서 실행되도록 요청하는 코드
