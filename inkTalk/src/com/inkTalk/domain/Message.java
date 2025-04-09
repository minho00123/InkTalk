package com.inkTalk.domain;

import java.awt.Color;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nickName;
	private String msg;
	private Color nameColor;

	public Message(String nickName, String msg, Color nameColor) {
		this.nickName = nickName;
		this.msg = msg;
		this.nameColor = nameColor;
	}

	public String getNickName() {
		return nickName;
	}

	public String getMsg() {
		return msg;
	}

	public Color getNameColor() {
		return nameColor;
	}

}
