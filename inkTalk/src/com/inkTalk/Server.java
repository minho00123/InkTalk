package com.inkTalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.inkTalk.domain.Stroke;

public class Server implements Runnable {
	private static final List<ObjectOutputStream> clients = Collections.synchronizedList(new ArrayList<>());
	private static List<Stroke> drawData = new ArrayList<>();
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Server(Socket socket) {
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			clients.add(out);

			for (Stroke stroke : drawData) {
				out.writeObject(stroke);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Stroke stroke = (Stroke) in.readObject();
				drawData.add(stroke);
				broadcast(stroke);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void broadcast(Stroke stroke) {
		for (ObjectOutputStream client : clients) {
			try {
				client.writeObject(stroke);
				client.flush();
			} catch (IOException e) {
				e.printStackTrace();
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
