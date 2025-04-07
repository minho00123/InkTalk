package com.inkTalk;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Stroke implements Serializable{

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

	}