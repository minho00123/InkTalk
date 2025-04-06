package com.inkTalk.common.domain;

/**
 * 서버와 클라이언트 간의 통신용 경량 객체로, 
 * 공통으로 이해해야 하는 모델을 데이터 공유 용으로 만든 도메인이다.
 * 
 * userId, password는 클라이언트가 알면 안되기 때문에 생략
 */
public class User {
	private String nickName;
	private boolean isHost; //방 생성자인지 여부 
	
	public User() {}
	
	public User(String nickName, boolean isHost) {
		this.nickName = nickName;
		this.isHost = isHost;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}
	
	
}
