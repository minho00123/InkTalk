package com.inkTalk;

import com.inkTalk.Stroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
	//들어온 데이터들을 중복이 없도록 set에 모아둠
    private static Set<ObjectOutputStream> clientOutputs = new HashSet<>();

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            } 
        }

        @Override
        public void run() {
        	System.out.println("실행...");
        	
			try {
				while(true) {
					Object object = in.readObject();
	        		if(object instanceof Stroke) {
	        			Stroke stroke = (Stroke)object;
	        			System.out.println("받은 Stroke:"+stroke);
	        		}
				}

			} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
        	}
        	

        }

        private void broadcastStroke(Stroke stroke) {
            synchronized (clientOutputs) {
                for (ObjectOutputStream clientOut : clientOutputs) {
                    try {
                        clientOut.writeObject(stroke);
                        clientOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                            if (socket != null) {
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("서버가 시작되었습니다.");
        ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(5555);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 연결됨: " + socket.getInetAddress());

                new Thread(new ClientHandler(socket)).start();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}


    }
}

