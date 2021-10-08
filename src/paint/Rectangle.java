package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {

	public Rectangle(Color c, int w, int h, int x, int y) {
		super(c, w, h, x, y);
	}

	@Override
	public void draw(Graphics g) {
		System.out.println(c + " rectangle: " + w + "x" + h + "@" + x + "," + y);
		g.setColor(c);
		g.drawRect(x, y, w, h);
	}

	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
