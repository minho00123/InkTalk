package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.inkTalk.app.AppController;
import com.inkTalk.domain.Canvas;
import com.inkTalk.domain.Message;
import com.inkTalk.domain.Stroke;
import com.inkTalk.domain.User;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	private Socket socket;
	private User loggedInUser;

	// whiteboard related-fields
	public JButton exit;
	AppController appController;
	Canvas canvas;
	List<Stroke> strokes = new ArrayList<>();
	Stroke currentStroke = null;
	JButton thickness;
	JButton palette;
	JButton eraser;
	JButton clearAll;
	JButton save;
	Color currentColor = Color.BLACK;
	int currentThickness = 2;
	int currentEraserThickness = 5;

	// chatboard related-fields
	JTextPane chatArea;
	JTextField inputField;
	JButton sendButton;

	public Board(AppController appController, Socket socket) {
		this.appController = appController;
		this.socket = socket;
		this.loggedInUser = appController.getLoggedInUser();

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1200, 800));

		// whiteboard UI
		JPanel whiteboard = new JPanel(new BorderLayout());
		whiteboard.setPreferredSize(new Dimension(880, 800));
		canvas = new Canvas(strokes);
		canvas.setBackground(Color.WHITE);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

		JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
		toolBar.setPreferredSize(new Dimension(100, getHeight()));

		thickness = new JButton(resizeIcon("pen.png", 50, 50));
		palette = new JButton(resizeIcon("palette.png", 50, 50));
		eraser = new JButton(resizeIcon("eraser.png", 50, 50));
		clearAll = new JButton(resizeIcon("eraseAll.png", 50, 50));
		save = new JButton(resizeIcon("saveFile.png", 50, 50));
		exit = new JButton(resizeIcon("exit.png", 50, 50));

		JButton[] buttons = { thickness, palette, eraser, clearAll, save,exit };
		for (JButton button : buttons) {
			toolBar.add(Box.createVerticalStrut(10));
			button.setPreferredSize(new Dimension(100, 70));
			button.setMaximumSize(new Dimension(100, 70));
			button.setMinimumSize(new Dimension(100, 70));
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setRolloverEnabled(true);
			button.setOpaque(false);
			button.setFocusable(false);
			button.addActionListener(this);
			if(button == exit) {
				continue;
			}
			toolBar.add(button);
		}
		toolBar.add(Box.createVerticalGlue());
		toolBar.add(exit);
		
		toolBar.setBackground(new Color(213, 231, 242));

		whiteboard.add(toolBar, BorderLayout.WEST);
		whiteboard.add(canvas, BorderLayout.CENTER);

		// chatboard UI
		JPanel chatboard = new JPanel(new BorderLayout());
		chatboard.setPreferredSize(new Dimension(300, 800));
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.setBackground(new Color(209, 229, 240));
		
		chatArea = new JTextPane();
		chatArea.setEditable(false);
		chatArea.setMargin(new Insets(15,10,10,10));
		JScrollPane scroll = new JScrollPane(chatArea);
		chatPanel.add(scroll, BorderLayout.CENTER);

		chatArea.setBackground(new Color(209, 229, 240));
		inputField = new JTextField();
		inputField.setBorder(null);
		inputField.addActionListener(this);

		inputField.setBackground(new Color(169, 198, 217));
		sendButton = new JButton("전송");
		sendButton.setBackground(new Color(1, 13, 38));
		sendButton.setForeground(Color.WHITE);
		sendButton.addActionListener(this);

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		chatboard.add(chatPanel, BorderLayout.CENTER);
		chatboard.add(inputPanel, BorderLayout.SOUTH);

		add(whiteboard, BorderLayout.CENTER);
		add(chatboard, BorderLayout.EAST);

		try {
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			out = new ObjectOutputStream(os);
			out.flush();
			in = new ObjectInputStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		startReceivingMessages();
	}

	private ImageIcon resizeIcon(String path, int width, int height) {
		String imagePath = "images/" + path;
		ImageIcon icon = new ImageIcon(imagePath);
		Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

		return new ImageIcon(img);
	}

	private void startReceivingMessages() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 로그인한 User객체 보내기
					out.writeObject(loggedInUser);
					out.flush();

					Boolean isDuplicate = in.readBoolean();
					if (!isDuplicate) {
						JOptionPane.showMessageDialog(null, "이미 로그인한 회원입니다.");
						appController.show("LOGIN");
						return;
					}
					out.writeObject(
							new Message("system", loggedInUser.getNickname() + "님이 입장하셨습니다.", loggedInUser.getColor()));
					out.flush();

					while (true) {
						Object obj;
						try {
							obj = in.readObject();
							if (obj instanceof Stroke) {
								strokes.add((Stroke) obj);
								SwingUtilities.invokeLater(() -> canvas.repaint());
							} else if (obj instanceof Message) {
								Message msg = (Message) obj;
								SwingUtilities.invokeLater(() -> {
									appendMessage(msg);
								});
							} else if (obj.equals("clearAll")) {
								strokes.clear();
								SwingUtilities.invokeLater(() -> canvas.repaint());
							}
						} catch (EOFException e) {
							System.out.println("서버와의 연결이 종료되었습니다.");
							break;
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
							break;
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
					showConnectionError();
				}
			}

		}).start();

	}

	private void appendMessage(Message msg) {
		StyledDocument doc = chatArea.getStyledDocument();
		SimpleAttributeSet style = new SimpleAttributeSet();

		try {
			if (msg.getNickName().equals("system")) {
				String content = msg.getMsg();

				// 입장 메시지: "닉네임님이 입장하셨습니다."
				if (content.endsWith("님이 입장하셨습니다.")) {
					String nickname = content.substring(0, content.indexOf("님이"));
					String remaining = content.substring(content.indexOf("님이"));

					// 닉네임 스타일 (입장 메시지 색상)
					Style nickStyle = chatArea.addStyle("JoinNick", null);
					StyleConstants.setForeground(nickStyle, msg.getNameColor());
					StyleConstants.setBold(nickStyle, true);


					// 나머지 메시지 스타일 (검정색)
					Style restStyle = chatArea.addStyle("JoinMsg", null);
					StyleConstants.setForeground(restStyle, Color.BLACK);

					// system: 은 검정색으로 넣어줌
					Style systemStyle = chatArea.addStyle("System", null);
					StyleConstants.setForeground(systemStyle, Color.BLACK);
					StyleConstants.setBold(systemStyle, true);

					
					doc.insertString(doc.getLength(), "system", systemStyle);
					doc.insertString(doc.getLength(), " : ", restStyle);
					doc.insertString(doc.getLength(), nickname, nickStyle);
					doc.insertString(doc.getLength(), remaining + "\n", restStyle);

				} else if (content.endsWith("님이 퇴장하셨습니다.")) {
					// 퇴장 메시지는 그대로 유지
					String nickname = content.substring(0, content.indexOf("님이"));
					String remaining = content.substring(content.indexOf("님이"));

					Style nickStyle = chatArea.addStyle("ExitNick", null);
					StyleConstants.setForeground(nickStyle, msg.getNameColor());
					StyleConstants.setBold(nickStyle, true);
					
					Style restStyle = chatArea.addStyle("ExitMsg", null);
					StyleConstants.setForeground(restStyle, Color.BLACK);
					
					Style systemStyle = chatArea.addStyle("System", null);
					StyleConstants.setForeground(systemStyle, Color.BLACK);
					StyleConstants.setBold(systemStyle, true);

					
					doc.insertString(doc.getLength(), "system", systemStyle);
					doc.insertString(doc.getLength(), " : ", restStyle);
					doc.insertString(doc.getLength(), nickname, nickStyle);
					doc.insertString(doc.getLength(), remaining + "\n", restStyle);

				} else {
					// 그 외 시스템 메시지
					Style sysStyle = chatArea.addStyle("SystemMsg", null);
					StyleConstants.setForeground(sysStyle, Color.DARK_GRAY);
					StyleConstants.setItalic(sysStyle, true);
					
					doc.insertString(doc.getLength(), "system : " + content + "\n", sysStyle);
				}
			} else {
				// 일반 사용자 메시지
				Style nickStyle = chatArea.addStyle("Nickname", null);
				StyleConstants.setForeground(nickStyle, msg.getNameColor());
				StyleConstants.setBold(nickStyle, true);

				Style msgStyle = chatArea.addStyle("Message", null);
				StyleConstants.setForeground(msgStyle, Color.BLACK);

				doc.insertString(doc.getLength(), msg.getNickName(), nickStyle);
				doc.insertString(doc.getLength(), " : " + msg.getMsg() + "\n", msgStyle);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	protected void showConnectionError() {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(this, "서버와의 연결에 문제가 발생했습니다. 프로그램을 종료합니다.");
			closeConnection();
			appController.dispose();
		});

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (currentStroke != null) {
			currentStroke.addPoint(e.getPoint());
			canvas.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		currentStroke = new Stroke(currentColor, currentThickness);
		currentStroke.addPoint(e.getPoint());
		strokes.add(currentStroke);
		canvas.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		sendStrokeToServer(currentStroke);
		currentStroke = null;
	}

	private void sendStrokeToServer(Stroke currentStroke) {
		if (out != null) {
			try {
				out.writeObject(currentStroke);
				out.flush();
			} catch (IOException e) {
				System.err.println("서버로 스트로크를 전송하는 중에 오류가 발생했습니다.");
				e.printStackTrace();
				showConnectionError();
			}
		} else {
			System.err.println("출력 스트림(out)이 null입니다. 서버에 연결되어 있지 않습니다.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = inputField.getText();
		if (e.getSource() == sendButton || e.getSource() == inputField) {
			if (!msg.trim().isEmpty()) {// 왜 그냥 .isEmpty나 .equals(null)은 안됐는지 모르겠어요
				try {
					Message message = new Message(loggedInUser.getNickname(), msg, loggedInUser.getColor());
					if (out != null) {
						out.writeObject(message);
						out.flush();

						inputField.setText("");
						inputField.setText(" ");
						chatArea.revalidate();
					} else {
						System.err.println("출력 스트림(out)이 null입니다. 서버에 연결되어 있지 않습니다.");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					showConnectionError();
				}
			}
		} else if (e.getSource() == thickness) {
			String input = JOptionPane.showInputDialog(this, "두께를 입력하세요 (1 ~ 20)");
			try {
				int thickness = Integer.parseInt(input);
				currentThickness = Math.max(1, Math.min(20, thickness));

				if (currentColor.equals(Color.WHITE)) {
					currentColor = Color.BLACK;
				}
			} catch (NumberFormatException ignored) {

			}
		} else if (e.getSource() == palette) {
			Color pickedColor = JColorChooser.showDialog(this, "색상 선택", currentColor);

			if (pickedColor != null) {
				currentColor = pickedColor;
			}
		} else if (e.getSource() == eraser) {
			try {
				String input = JOptionPane.showInputDialog(this, "두께를 입력하세요 (1 ~ 20)");
				int thickness = Integer.parseInt(input);
				currentThickness = Math.max(1, Math.min(20, thickness));
				currentColor = Color.WHITE;
			} catch (NumberFormatException ignored) {
				ignored.printStackTrace();

			}
		} else if (e.getSource() == clearAll) {
			int choice = JOptionPane.showConfirmDialog(this, "전체 그림을 지우시겠습니까?", "전체 삭제", JOptionPane.OK_CANCEL_OPTION);

			if (choice == JOptionPane.OK_OPTION) {
				try {
					strokes.clear();
					out.writeObject("clearAll");
					out.flush();
					canvas.repaint();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == save) {
			canvas.redrawToBufferedImage();

			try {
				String fileName = "my_drawing_" + System.currentTimeMillis() + ".png";
				File output = new File(fileName);
				ImageIO.write(canvas.getImage(), "png", output);
				JOptionPane.showMessageDialog(this, "이미지가 저장되었습니다:\n" + fileName);
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "저장에 실패했습니다. 다시 시도해주세요.");
			}
		} else if (e.getSource() == exit) {
			int exit = JOptionPane.showConfirmDialog(this,
					"해당 채팅방을 나가시겠습니까?\n현재 그림은 저장되지 않습니다." + "\n그림을 저장하길 원하신다면 저장 버튼을 누른 후 채팅방을 나가길 권장합니다.", "채팅방 나가기",
					JOptionPane.OK_CANCEL_OPTION);

			if (exit == JOptionPane.OK_OPTION) {
				try {
					out.writeObject(
							new Message("system", loggedInUser.getNickname() + "님이 퇴장하셨습니다.", loggedInUser.getColor()));
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				appController.dispose();
			}
		}
	}

	public void closeConnection() {
		try {
			if (out != null) {
				out.close();
			} else if (in != null) {
				in.close();
			} else if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
