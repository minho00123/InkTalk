package com.inkTalk.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkTalk.client.websocket.InkTalkClientEndpoint;
import com.inkTalk.common.dto.Message;
import com.inkTalk.common.dto.MessageType;

public class ChatService {
//클라이언트측의 chatService 
//채팅 메세지를 ClientEndpoint로 보내 서버로 보내도록 한다.
	private final InkTalkClientEndpoint client;
	private final ObjectMapper mapper = new ObjectMapper();
	public ChatService(InkTalkClientEndpoint client) {
		this.client = client;
	}
	//채팅, 그림그리기 데이터 웹소켓 전송
	public void sendChat(Long roomId, Long senderId, String content) {
		try {
			Message message = new Message(MessageType.CHAT, roomId, senderId, content);
			String json = mapper.writeValueAsString(message);
			client.sendMessage(json);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
	//로그인, 방 목록 조회, 방 생성, 방 입장 가능 여부 확인, 방 삭제
	//db 사용
}
