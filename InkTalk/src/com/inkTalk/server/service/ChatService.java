package com.inkTalk.server.service;

import java.io.IOException;

import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkTalk.common.dto.Message;
import com.inkTalk.server.domain.Room;

public class ChatService {

	private static final ChatService instance = new ChatService(); 
	private final RoomService roomService = RoomService.getInstance();
	private final ObjectMapper objectMapper = new ObjectMapper();
	public ChatService() {}
	public static ChatService getInstance() {
		return instance;
	}

	/** 채팅 메세지를 같은 방의 모든 사용자에게 보내기
	 * @param roomId
	 * @param message
	 */
	public void broadcast(Long roomId, Message message) {
		Room room = roomService.getRoom(roomId);
		if(room == null) return;
		
		for(Session session : room.getConnectedUsers().values()) {
			if(session.isOpen()) {
				try {
					//엔드포인트에 json 형태의 message를 객체로 변환하여 sendText함
					session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
				} catch (IOException e) {
					System.out.println("채팅 전송 실패" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	

}
