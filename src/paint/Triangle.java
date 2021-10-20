package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Triangle extends Shape {

	public Triangle(Color c, int w, int h, int x, int y) {
		super(c, w, h, x, y);
	}

	@Override
	public void draw(Graphics g) {
		System.out.println("drawing triangle");
		// use three g.drawLine(...) methods 
		// calculate the top of the triangle, bottom left, and bottom right
//		int x1 = ?;
//		int y1 = ?;
//		int x2 = ?;
//		int y2 = ?;
//		int x3 = ?;
//		int y3 = ?;
//		g.drawLine(x1, y1, x2, y2);
//		plus two more g.drawLines(...);
	}

	@Override
	protected String getShapeType() {
		return "Triangle";
	}
	
	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
