package com.inkTalk.client.websocket;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class InkTalkClientEndpoint {
	private Session session;
	private final ObjectMapper mapper = new ObjectMapper();
	
	//메세지 전송 메서드
	public void sendMessage(String message) {
		try {
			String json= mapper.writeValueAsString(message);
			session.getBasicRemote().sendText(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//웹소켓 서버와 연결
	public static InkTalkClientEndpoint connect(String uri) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			InkTalkClientEndpoint endpoint = new InkTalkClientEndpoint();
			
			//클라이언트 엔드포인트 인스턴스를 서버에 연결
			container.connectToServer(endpoint, URI.create(uri));
			return endpoint;
		} catch (DeploymentException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
