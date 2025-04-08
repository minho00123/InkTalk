package com.inkTalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class Server implements Runnable {
	private static final long serialVersionUID = 1L;

	private static List<ObjectOutputStream> clients = new ArrayList<>();
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

			synchronized (clients) {
				clients.add(out); // register client
			}

			synchronized (drawData) {
				for (Stroke stroke : drawData) {
					out.writeObject(stroke);
					out.flush();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Stroke stroke = (Stroke) in.readObject();
				synchronized (drawData) {
					drawData.add(stroke);
				}
				broadcast(stroke);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	private void broadcast(Stroke stroke) {
		synchronized (clients) {
			clients.removeIf(client -> {
				try {
					client.writeObject(stroke);
					client.flush();
					return false;
				} catch (IOException e) {
					// Remove dead client
					return true;
				}
			});
		}
		System.out.println("Broadcasting stroke to " + clients.size() + " clients.");
	}

	private void cleanup() {
		synchronized (clients) {
			clients.remove(out);
		}
		try {
			if (socket != null)
				socket.close();
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("서버 시작");
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(5555);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread thread = new Thread(new Server(socket));
				thread.start();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
