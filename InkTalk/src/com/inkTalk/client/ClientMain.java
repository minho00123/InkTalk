package com.inkTalk.client;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkTalk.client.websocket.InkTalkClientEndpoint;
import com.inkTalk.common.dto.Message;
import com.inkTalk.common.dto.MessageType;

public class ClientMain {

	public static void main(String[] args) {
		
		String uri = "ws://192.168.56.1/inkTalk/whiteboard"; //server 엔드포인트주소
		
		InkTalkClientEndpoint client = InkTalkClientEndpoint.connect(uri);
		
		if(client != null) {
			Message msg = new Message(MessageType.SYSTEM,null,null, "클라이언트 X");
			client.sendMessage(uri);
		}
	}
}
