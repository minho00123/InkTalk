package com.inkTalk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.inkTalk.app.AppController;
import com.inkTalk.domain.Canvas;
import com.inkTalk.domain.Stroke;

public class Whiteboard extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	public JButton exit;

	ArrayList<Stroke> strokes = new ArrayList<>();
	Stroke currentStroke = null;
	AppController main;
	Canvas canvas;
	JButton thickness;
	JButton palette;
	JButton eraser;
	JButton clearAll;
	JButton save;
	Color currentColor = Color.BLACK;
	int currentThickness = 2;

	public Whiteboard(AppController appController, Socket socket) {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							Stroke stroke = (Stroke) in.readObject();
							System.out.println(stroke);
							strokes.add((Stroke) stroke);
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									canvas.repaint();
								}

							});
						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

			});

			thread.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.main = appController;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 800));

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
		save = new JButton(resizeIcon("fileSave.png", 50, 50));
		exit = new JButton(resizeIcon("exit.png", 50, 50));
		JButton[] buttons = { thickness, palette, eraser, clearAll, save, exit };
		for (JButton button : buttons) {
			button.setPreferredSize(new Dimension(100, 50));
			button.setMaximumSize(new Dimension(100, 50));
			button.setMinimumSize(new Dimension(100, 50));
			button.setFocusable(false);
			button.addActionListener(this);
			toolBar.add(button);

			if (button == exit) {
				toolBar.addSeparator();
				toolBar.add(button);
			}
		}

		toolBar.setBackground(new Color(213, 231, 242));

		add(toolBar, BorderLayout.WEST);
		add(canvas, BorderLayout.CENTER);
	}

	private ImageIcon resizeIcon(String path, int width, int height) {
		String imagePath = "images/" + path;
		ImageIcon icon = new ImageIcon(imagePath);
		Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

		return new ImageIcon(img);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == thickness) {
			String input = JOptionPane.showInputDialog(this, "두께를 입력하세요 (1 ~ 20)");
			try {
				int thickness = Integer.parseInt(input);
				currentThickness = Math.max(1, Math.min(20, thickness));
			} catch (NumberFormatException ignored) {

			}
		} else if (e.getSource() == palette) {
			Color pickedColor = JColorChooser.showDialog(this, "색상 선택", currentColor);
			if (pickedColor != null) {
				currentColor = pickedColor;
			}
		} else if (e.getSource() == eraser) {
			currentColor = Color.WHITE;
		} else if (e.getSource() == clearAll) {
			int choice = JOptionPane.showConfirmDialog(this, "전체 그림을 지우시겠습니까?", "전체 삭제", JOptionPane.OK_CANCEL_OPTION);

			if (choice == JOptionPane.OK_OPTION) {
				strokes.clear();
				canvas.repaint();
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
				main.dispose();
			}
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
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (currentStroke != null) {
			currentStroke.addPoint(e.getPoint());
			canvas.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
}
