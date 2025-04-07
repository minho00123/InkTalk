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
        	System.out.println("����...");
        	
			try {
				while(true) {
					Object object = in.readObject();
	        		if(object instanceof Stroke) {
	        			Stroke stroke = (Stroke)object;
	        			System.out.println("���� Stroke:"+stroke);
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
        System.out.println("������ ���۵Ǿ����ϴ�.");
        ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(5555);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Ŭ���̾�Ʈ �����: " + socket.getInetAddress());

                new Thread(new ClientHandler(socket)).start();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}


    }
}

