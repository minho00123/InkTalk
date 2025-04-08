package com.inkTalk.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.inkTalk.app.AppController;
import com.inkTalk.jdbc.JDBCTemplate;

public class SignupUi extends JPanel implements DocumentListener, ActionListener{
	private PreparedStatement pstmt;
	private AppController controller;
	
    public PlaceholderTextField nameField, pwordField;
    public static JLabel nameerror, pworderror;
    public String nicknameinput, passwordinput;
    public static JButton signupConfirm, signupCancel;
    

    public SignupUi(AppController controller) {
    	this.controller = controller;
    	this.setPreferredSize(new Dimension(1200, 800));
    	
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(209, 229, 240)); // 연파랑
        backgroundPanel.setPreferredSize(new Dimension(1200, 800));
        this.add(backgroundPanel);

        // 로고 이미지
        ImageIcon logoIcon = new ImageIcon("images/logo.jpg"); // 로고 이미지 경로
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel signupPanel = new JPanel(new GridBagLayout());
        signupPanel.setPreferredSize(new Dimension(400, 250));
        signupPanel.setBackground(new Color(179, 206, 224));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // 여백

        // 닉네임 라벨
        JLabel nickname = new JLabel("닉네임");
        nickname.setFont(new Font("", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.EAST;
        signupPanel.add(nickname, gbc);

        // 닉네임 입력 필드
        nameField = new PlaceholderTextField("10자 이내로 작성해주세요. (특수문자 사용 금지)");
        nameField.setColumns(23);
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        signupPanel.add(nameField, gbc);
        nameField.getDocument().addDocumentListener(this);

        nameerror = new JLabel(" ");
        nameerror.setForeground(Color.RED);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 4;
        signupPanel.add(nameerror, gbc);

        // 비밀번호 라벨
        JLabel password = new JLabel("비밀번호");
        password.setFont(new Font("", Font.BOLD, 15));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        signupPanel.add(password, gbc);

        // 비밀번호 입력 필드
        pwordField = new PlaceholderTextField("숫자로 이루어진 8자로 작성해주세요.");
        pwordField.setColumns(23);
        gbc.gridx = 2; gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        signupPanel.add(pwordField, gbc);
        pwordField.getDocument().addDocumentListener(this);

        pworderror = new JLabel(" ");
        pworderror.setForeground(Color.RED);
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.gridwidth = 4;
        signupPanel.add(pworderror, gbc);

        // 버튼
        signupConfirm = new JButton("회원등록");
        signupConfirm.setBackground(new Color(11, 29, 49));
        signupConfirm.setForeground(Color.WHITE);
        signupConfirm.setFont(new Font("", Font.BOLD, 15));
        gbc.gridx = 1; gbc.gridy = 8;
        gbc.gridwidth = 2;
        int totalHeight = pwordField.getPreferredSize().height * 2;
        signupConfirm.setPreferredSize(new Dimension(100, totalHeight));
        signupPanel.add(signupConfirm, gbc);

        signupCancel = new JButton("취소");
        signupCancel.setBackground(new Color(11, 29, 49));
        signupCancel.setForeground(Color.WHITE);
        signupCancel.setFont(new Font("", Font.BOLD, 15));
        gbc.gridx = 3; gbc.gridy = 8;
        gbc.gridwidth = 2;
        signupCancel.setPreferredSize(new Dimension(100, totalHeight));
        signupPanel.add(signupCancel, gbc);

        // 수직 정렬
        JPanel verticalBox = new JPanel();
        verticalBox.setLayout(new BoxLayout(verticalBox, BoxLayout.Y_AXIS));
        verticalBox.setOpaque(false);
        verticalBox.add(logoLabel);
        verticalBox.add(Box.createVerticalStrut(30));
        verticalBox.add(signupPanel);

        backgroundPanel.add(verticalBox, new GridBagConstraints());

        
    }

    // DocumentListener 메소드
    @Override
    public void insertUpdate(DocumentEvent e) {
        validateText();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateText();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}

    public boolean validateText() {
    	
    	boolean Validation = true;
    	
        nicknameinput = nameField.getText();
        passwordinput = pwordField.getText();

        if (!nameField.isPlaceholderVisible()) {
            if (nicknameinput.matches("[a-zA-Z0-9가-힣]{1,10}")) {
                nameerror.setText(" ");
            } else if (nicknameinput.isEmpty() || nicknameinput.length() > 10) {
                nameerror.setText("닉네임은 10자 이내이며, 비워둘 수 없습니다.");
               Validation = false;
            } else {
                nameerror.setText("닉네임은 한글,영어,숫자만 허용됩니다.");
                Validation = false;
            }
        }

        if (!pwordField.isPlaceholderVisible()) {
            if (passwordinput.matches("[0-9]{8}")) {
                pworderror.setText(" ");
            } else if (passwordinput.isEmpty() || passwordinput.length() != 8) {
                pworderror.setText("비밀번호는 8글자여야 합니다.");
                Validation =  false;
            } else {
                pworderror.setText("비밀번호는 숫자로만 이루어져야 합니다.");
                Validation = false;
            }
        }
        return Validation;
    }

    // 내부 클래스: Placeholder 텍스트 필드
   static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        private boolean showingPlaceholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            this.showingPlaceholder = true;
            setText(placeholder);
            setForeground(Color.GRAY);
            setPreferredSize(new Dimension(250, 30));

            this.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (showingPlaceholder) {
                        setText("");
                        setForeground(Color.BLACK);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                        showingPlaceholder = true;
                    }
                }
            });
        }

        public boolean isPlaceholderVisible() {
            return showingPlaceholder;
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==signupConfirm) {
			Connection conn = JDBCTemplate.getConnection();
			String sql = "INSERT INTO USER(USER_ID, NICKNAME, PASSWORD) VALUES(USER_ID_SEQ,?,?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, nameField.getText());
				pstmt.setString(2, pwordField.getText());

				int result = pstmt.executeUpdate();
				if(result>0 ) {
					controller.show("LOGIN");
				}else {
					System.out.println("회원가입 실패");

				}			
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			
		}
		
	}

}
