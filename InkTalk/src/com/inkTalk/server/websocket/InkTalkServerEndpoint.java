package com.inkTalk.server.websocket;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.inkTalk.server.controller.WhiteboardMessageController;

//웹소켓으로 접속 가능한 url 정보 명시 - 소켓 서버를 생성해줌
@ServerEndpoint(value = "/inkTalk/whiteboard")
public class InkTalkServerEndpoint {
	private static final WhiteboardMessageController messageController = new WhiteboardMessageController();
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("클라이언트 연결됨: "+session.getId());
	}
	
	/**
	 * 클라이언트에게 메세지를 받을 때 실행됨
	 */
	@OnMessage
	public void onMessage(Session session, String messageJson) {
		messageController.handelMessage(session, messageJson);
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("에러 발생 -세션: "+session.getId());
		throwable.printStackTrace();
	}
	
}
