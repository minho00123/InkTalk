package com.inkTalk.server.controller;

import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkTalk.common.dto.Message;
import com.inkTalk.server.service.ChatService;
import com.inkTalk.server.service.RoomService;
import com.inkTalk.server.service.WhiteBoardService;

public class WhiteboardMessageController {
	
	private final ChatService chatService = ChatService.getInstance(); //getInstance() : 하나의 메모리 계속 유지
	private final WhiteBoardService whiteboardService = WhiteBoardService.getInstance();
	
	//json 데이터 <-> 객체 변환에 사용하는 클래스
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	/** 세션객체와 json 데이터를 받아서 chat, draw, 그 외 요청을 처리하는 메서드
	 * @param session:연결된 클라이언트의 세션
	 * @param json:메시지 데이터
	 */
	public void handelMessage(Session session, String json) {
		try {
			//메세지 객체로 변환하기
			Message message = objectMapper.readValue(json, Message.class);
			
			switch(message.getType()) {
			case CHAT:
				chatService.broadcast(message.getRoomId(), message);
				break;
			case DRAW:
				whiteboardService.broadcast(message.getRoomId(),message);
				break;
			case ENTER_ROOM:
				RoomService.getInstance().joinRoom(message.getRoomId(),message.getSender(), session);
			case EXIT_ROOM:
				RoomService.getInstance().exitRoom(message.getRoomId(),message.getSender());
			default:
				System.out.println("지원되지 않는 메시지 타입입니다.");
			}
		}catch(Exception e) {
			 e.printStackTrace();
		}
		
	}
}
