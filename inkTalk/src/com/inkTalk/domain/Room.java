package com.inkTalk.domain;

import java.time.LocalDate;

public class Room {
	private int roomId;
	private int userId;
	private String title;
	private LocalDate createDate;
	public Room(int roomId, int userId, String title) {
		this.roomId = roomId;
		this.userId = userId;
		this.title = title;
	}
	public int getRoomId() {
		return roomId;
	}
	public int getUserId() {
		return userId;
	}
	public String getTitle() {
		return title;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	
}
