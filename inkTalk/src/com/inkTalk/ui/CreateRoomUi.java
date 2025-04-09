package com.inkTalk.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import com.inkTalk.app.AppController;
import com.inkTalk.domain.User;
import com.inkTalk.jdbc.JDBCTemplate;

public class CreateRoomUi extends JPanel implements ActionListener{
	private PreparedStatement pstmt;
	private ResultSet rs;
	AppController controller;
	public CreateRoomUi(AppController controller) {
		this.controller = controller;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource()==createButton) {
//			String title = new String();
//			String pw = new String();
//			Connection conn = JDBCTemplate.getConnection();
//			String sql = "INSERT INTO ROOM() VALUES(ROOM_ID_SEQ.NEXTVAL,?,?,?,0,SYSDATE)";
//			User user = controller.getLoggedInUser();
//			try {
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, user.getId()); 
//				pstmt.setString(2, title);
//				pstmt.setString(3, pw);
//				int result = pstmt.executeUpdate();
//				
//				if(result >0) {
//					//방 생성 성공 -> 바로 방 입장
//					controller.show("");
//				}else {
//					System.out.println("생성 실패");
//				}
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		}else if(e.getSource()==closeButton) {
//			//이전 화면으로 이동(방 목록 or 로그인 화면)
//		}
		
	}

}
