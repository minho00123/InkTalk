package com.inkTalk.domain;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nickName;
	private String msg;
	
	public Message(String nickName, String msg) {
		this.nickName = nickName;
		this.msg = msg;
	}

	public String getNickName() {
		return nickName;
	}

	public String getMsg() {
		return msg;
	}


}
