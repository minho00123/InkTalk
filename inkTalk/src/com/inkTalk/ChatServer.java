package com.inkTalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.inkTalk.domain.Message;
import com.inkTalk.domain.Stroke;

public class ChatServer implements Runnable{
	private static final List<ObjectOutputStream> clients = new ArrayList<>();
	private static final List<Stroke> drawData = new ArrayList<Stroke>();
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ChatServer(Socket socket) {
		this.socket = socket;
		
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in  = new ObjectInputStream(socket.getInputStream());
			
			synchronized (socket) {
				clients.add(out);
			}
			
			//입장할 때 그려진 데이터 미리 보내기
			synchronized (drawData) {
				for(Stroke stroke : drawData) {
					out.writeObject(stroke);
					out.flush();
				}
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		

			try {
				while(true) {
					Object receivedObject;
					receivedObject = in.readObject();
					System.out.println(receivedObject.toString());
					if(receivedObject instanceof Stroke) {
						System.out.println("@@Stoke@@");
						//1) 읽어낸 객체가 Stoke인 경우
						Stroke stroke = (Stroke)receivedObject;
						synchronized (drawData) {
							drawData.add(stroke);
						}
						broadCastStroke(stroke,clients);
					}else if(receivedObject instanceof Message) {
						System.out.println("@@msg@@");
						//2) 읽어낸 객체가 Message인 경우
						Message msg = (Message)receivedObject;
						broadCastMsg(msg,clients);
					}
					
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} finally {
				cleanup();
			}

	}
		
	
	private void broadCastStroke(Stroke stroke, List<ObjectOutputStream> clients) {
		synchronized (clients) {
			Iterator<ObjectOutputStream> iterator = clients.iterator();
			while(iterator.hasNext()) {
				ObjectOutputStream client = iterator.next();
				try {
					client.writeObject(stroke);
					client.flush();	
				}catch(IOException e) {
					//예외 발생 시 해당 클라이언트 제거
					iterator.remove();
					System.out.println("클라이언트 제거 : 전송 실패");
					e.printStackTrace();
				}

			}	
		}
		
	}
	private void broadCastMsg(Message msg,List<ObjectOutputStream> clients) {
		synchronized (clients) {
			Iterator<ObjectOutputStream> iterator = clients.iterator();
			while(iterator.hasNext()) {
				ObjectOutputStream client = iterator.next();
				
				try {
					client.writeObject(msg);
					client.flush();
				} catch (IOException e) {
					iterator.remove();
					System.out.println("클라이언트 제거 : 전송 실패");
					e.printStackTrace();
				}
		}
		
	}


	}


	private void cleanup() {
		synchronized (clients) {
			//연결 종료 시 클라이언트 제거
			clients.remove(out);
		}
		
		try {
			if(socket != null)socket.close();
			if(in != null) in.close();
			if(out != null) out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) {
		System.out.println("서버 시작!");
		try {
			ServerSocket serverSocket = new ServerSocket(5555);
			while(true) {
				Socket socket  = serverSocket.accept();
				Thread thread = new Thread(new ChatServer(socket));
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
