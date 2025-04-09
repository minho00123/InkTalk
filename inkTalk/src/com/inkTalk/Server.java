package com.inkTalk;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.inkTalk.domain.Message;
import com.inkTalk.domain.Stroke;

public class Server implements Runnable {
	private static final List<ObjectOutputStream> clients = new ArrayList<>();
	private static List<Stroke> drawData = new ArrayList<>();
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Server(Socket socket) {
		this.socket = socket;

		try {
            // 입력 스트림 및 출력 스트림 초기화
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // 초기화 후 플러시
            
            synchronized (clients) {
                if (clients.contains(out)) {
                    out.writeBoolean(true);  // 중복된 클라이언트 처리
                    out.flush();
                } else {
                    out.writeBoolean(false);
                    out.flush();
                    clients.add(out); // 새 클라이언트 추가
                    for (Stroke stroke : drawData) {
                        out.writeObject(stroke);  // 이전 그린 데이터 전송
                        out.flush();
                    }
                }
            }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Object receivedObject = in.readObject();

				if (receivedObject instanceof Stroke) {
					Stroke stroke = (Stroke) receivedObject;
					drawData.add(stroke);
					broadcast(stroke);
				} else if (receivedObject instanceof Message) {
					Message msg = (Message) receivedObject;
					broadcast(msg);
				} else if (receivedObject.equals("clearAll")) {
					drawData.clear();
					broadcast("clearAll");
				}
			}
		} catch (SocketException e) {
			System.out.println("클라이언트 연결이 강제로 끊어졌습니다: " + socket.getInetAddress());
		} catch (EOFException e) {
			System.out.println("클라이언트 연결 종료됨: " + socket.getInetAddress());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
                synchronized (clients) {
                    clients.remove(out);
                }
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    private void broadcast(Object object) {
        // 모든 클라이언트에게 메시지나 그림 전송
        synchronized (clients) {
            for (ObjectOutputStream client : clients) {
                try {
                    client.writeObject(object);
                    client.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(5555);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("클라이언트 접속됨: " + socket.getInetAddress());
				new Thread(new Server(socket)).start();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
