package com.inkTalk.common.domain;

import java.io.Serializable;

public class Room implements Serializable{
	private Long roomId; //방 ID
	private String title; //방 제목
	private String hostNickName; //생성자 닉네임
//	private boolean mode; //뮤트모드 여부
	private int userCount; //이용자 수
	
	
	public Room() {}
	public Room(Long roomId, String title, String hostNickName) {
		this.roomId = roomId;
		this.title = title;
		this.hostNickName = hostNickName;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHostNickName() {
		return hostNickName;
	}
	public void setHostNickName(String hostNickName) {
		this.hostNickName = hostNickName;
	}
//	public boolean isMode() {
//		return mode;
//	}
//	public void setMode(boolean mode) {
//		this.mode = mode;
//	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	
}
