package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Line extends Shape {

	protected ArrayList<Point> points = new ArrayList<>();
	
	public Line(Color c) {
		super(c, 0, 0, 0, 0);
	}

	@Override
	protected String getShapeType() {
		return "Line";
	}

	@Override
	public void draw(Graphics g) {
		System.out.println(c + " line");
		g.setColor(c);
		for (int i = 0; i < points.size()-1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.drawLine(p1.x,p1.y,p2.x,p2.y);
		}
	}

	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
