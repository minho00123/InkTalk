package com.inkTalk.server.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;


public class Room {
	private static final int MAX_USERS = 8; //최대 사용자 수
	
	private Long roomId; //방 ID
	private String title; //방 제목
	private int password; //비밀번호
	private boolean activation; //활성화 여부
	private LocalDateTime createDate; //생성일
	private Long hostUserId; //생성자 닉네임
	private Map<Long, Session> connectedUsers = new HashMap<>(); //이용자 Set
//	private boolean mode; //뮤트 모드
	
	public Room(Long RoomId, String title, Long hostUserId, int password) {
		this.roomId = RoomId;
		this.title = title;
		this.hostUserId = hostUserId;
		this.password = password;
		this.activation = false; //방 생성 시 비활성화 -> 입장하면 활성화로 바꿈
	}

	
	
	//방의 세션 관리, 입장 가능 여부 판단
	/**클라이언트의 요청을 websocket으로 받아 확인할 수 있다.
	 * @return 입장 가능 여부
	 * synchronized : 여러 스레드가 동시에 사용하지 못하도록
	 */
	public synchronized boolean canEnter() {
		return connectedUsers.size() < MAX_USERS;
	}
	
	//connectedUser에 addUser하기 -synchronized
	public synchronized void addUser(Long userId, Session session) {
		
	}
	//removeUser하기 -synchronized
	public synchronized void removeUser(Long userId) {
		
	}
	
	//==getter, setter==//
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		roomId = roomId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPassword() {
		return password;
	}
	public void setPassword(int password) {
		this.password = password;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	public Long getHostUserId() {
		return hostUserId;
	}
	public void setHostUserId(Long hostUserId) {
		this.hostUserId = hostUserId;
	}
	public Map<Long, Session> getConnectedUsers() {
		return connectedUsers;
	}
	public void setConnectedUsers(Map<Long, Session> connectedUsers) {
		this.connectedUsers = connectedUsers;
	}
	
//	public boolean isMode() {
//		return mode;
//	}
//	public void setMode(boolean mode) {
//		this.mode = mode;
//	}
}
