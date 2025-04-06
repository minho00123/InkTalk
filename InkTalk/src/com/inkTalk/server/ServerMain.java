package com.inkTalk.server;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import com.inkTalk.server.websocket.InkTalkServerEndpoint;

public class ServerMain {
	public static void main(String[] args) {
		Server server = new Server("192.168.56.1",8080,"/inkTalk",null,InkTalkServerEndpoint.class);
		try {
			server.start();

		} catch (DeploymentException e) {
			e.printStackTrace();
		}finally {			
			server.stop();
		}
	}
}
