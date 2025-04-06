package com.inkTalk.server.domain;

import javax.websocket.Session;

/**
 * server에서 필요한 계정 정보가 담긴 user 도메인
 * 1. db에서 로그인할 때 조회하거나 계정 정보를 조회할 때 사용한다.
 * 2. 
 */
public class Account {
	//db 정보
	private Long userId;
	private String nickName;
	private int password;
	
	//웹소켓 상태 정보
	private Session session; //현재 연결 상태
	private boolean isHost; //현재 입장 중인 방의 생성자 여부 확인
	private String currentRoomId; //현재 입장중인 방 ID
	
	public Account(Long userId, String nickName, int password) {
		this.userId = userId;
		this.nickName = nickName;
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}
	
}
