package com.inkTalk.common.dto;

//입장, 채팅 메세지, 그림 등의 내용을 서버메세지로 보낼 때
// 객체로 담아서 보낸다.
public class Message {
	private MessageType type;
	private Long roomId;
	private Long sender;
	private String content;
	
	private int x, y, prevX, prevY;

	public Message() {}
	
	public Message(MessageType type, Long roomId, Long sender, String content) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.content = content;
	}
	//whiteboard를 보낼 때 생성자
	
	
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getSender() {
		return sender;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPrevX() {
		return prevX;
	}

	public void setPrevX(int prevX) {
		this.prevX = prevX;
	}

	public int getPrevY() {
		return prevY;
	}

	public void setPrevY(int prevY) {
		this.prevY = prevY;
	}
	
	
}
