package com.inkTalk.server.service;

import java.io.IOException;

import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkTalk.common.dto.Message;
import com.inkTalk.server.domain.Room;

public class WhiteBoardService {
	private static final WhiteBoardService instance = new WhiteBoardService();
	private final RoomService roomService = RoomService.getInstance();
	private final ObjectMapper objectMapper = new ObjectMapper();
	public WhiteBoardService() {}
	public static WhiteBoardService getInstance() {
		return instance;
	}

	public void broadcast(Long roomId, Message message) {
		Room room = roomService.getRoom(roomId);
		
		if(room != null) {
			for(Session session : room.getConnectedUsers().values()) {
				if(session.isOpen())
					try {
						session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
					} catch (IOException e) {
						System.out.println("그림 데이터 전송 실패" + e.getMessage());
						e.printStackTrace();
					}
			}
		}
		
	}

}
