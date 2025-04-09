package com.inkTalk.domain;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;

	BufferedImage canvasImage;
	Graphics2D graphics2D;
	ArrayList<Stroke> strokes;

	public Canvas(ArrayList<Stroke> strokes) {
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
//        graphics.drawImage(canvasImage, 0, 0, null);
//        Graphics2D graphics2D = (Graphics2D) graphics;
//        System.out.println("paintComponent called with " + strokes.size() + " strokes");
//        for (Stroke stroke: strokes) {
//            graphics2D.setColor(stroke.getColor());
//            graphics2D.setStroke(new BasicStroke(stroke.getThickness()));
//            ArrayList<Point> points = stroke.getPoints();
//            stroke.draw(graphics);
//            revalidate();

//            for (int i = 0; i < points.size() - 1; i++) {
//                Point p1 = points.get(i);
//                Point p2 = points.get(i + 1);
//                graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
//            }
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
