package com.inkTalk.domain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Stroke implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<Point> points;
	private Color color;
	int thickness;

	public Stroke(Color color, int thickness) {
		this.color = color;
		this.thickness = thickness;
		this.points = new ArrayList<>();
	}

	public void addPoint(Point p) {
		points.add(p);
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public Color getColor() {
		return color;
	}

	public int getThickness() {
		return thickness;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		for (int i = 1; i < points.size(); i++) {
			Point p1 = points.get(i - 1);
			Point p2 = points.get(i);
			g2.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
}
