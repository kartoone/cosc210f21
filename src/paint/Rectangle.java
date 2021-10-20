package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {

	public Rectangle(Color c, int w, int h, int x, int y) {
		super(c, w, h, x, y);
	}

	@Override
	protected String getShapeType() {
		return "Rectangle";
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(c);
		g.drawRect(x-w/2, y-h/2, w, h);
	}

	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
