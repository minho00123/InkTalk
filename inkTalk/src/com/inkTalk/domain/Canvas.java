package com.inkTalk.domain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	BufferedImage canvasImage;
	Graphics2D graphics2D;
	List<Stroke> strokes;

	public Canvas(List<Stroke> strokes) {
		this.strokes = strokes;
		canvasImage = new BufferedImage(1000, 700, BufferedImage.TYPE_4BYTE_ABGR);
		graphics2D = canvasImage.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		for (Stroke stroke : strokes) {
			stroke.draw(graphics);
		}
	}

	public void redrawToBufferedImage() {
		graphics2D.setColor(Color.WHITE);
		graphics2D.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());

		for (Stroke stroke : strokes) {
			graphics2D.setColor(stroke.getColor());
			graphics2D.setStroke(new BasicStroke(stroke.getThickness()));

			for (int i = 0; i < stroke.getPoints().size() - 1; i++) {
				Point p1 = stroke.getPoints().get(i);
				Point p2 = stroke.getPoints().get(i + 1);
				graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}

	public BufferedImage getImage() {
		return canvasImage;
	}
}
