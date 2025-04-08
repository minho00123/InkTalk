package com.inkTalk.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import com.inkTalk.app.AppController;
import com.inkTalk.domain.Room;
import com.inkTalk.domain.User;
import com.inkTalk.jdbc.JDBCTemplate;

public class EnterRoomUi extends JPanel implements ActionListener{
	private PreparedStatement pstmt;
	private ResultSet rs;
	AppController controller;
	Room room;
	
	public EnterRoomUi(AppController appController, Room room) {
		this.controller = controller;
		this.room = room;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//선택한 방 객체의 id를 가져와서 id와 pw 비교하기
//		if(e.getSource()==enterButton) {
//		
//		String title = room.getTitle();
//		String pw = new String();
//		Connection conn = JDBCTemplate.getConnection();
//		String sql = "SELECT ROOM_ID FROM ROOM WHERE ROOM_ID= ? AND PASSWORD = ?";
//		User user = controller.getLoggedInUser();
//		try {
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, user.getId()); 
//			pstmt.setString(2, title);
//			pstmt.setString(3, pw);
//			rs = pstmt.executeQuery();
//			
//			if(rs != null&& rs.next()) {
//				//방 생성 성공 -> 바로 방 입장
//				controller.show(""); //화이트보드 + 채팅 방 화면 구현한 클래스
//			}else {
//				System.out.println("입장 실패");
//			}
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//	}else if(e.getSource()==closeButton) {
//		//이전 화면으로 이동(방 목록 or 로그인 화면)
//		controller.show("");
//	}
//	
		
	}

}
