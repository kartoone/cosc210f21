package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Triangle extends Shape {

	public Triangle(Color c, int w, int h, int x, int y) {
		super(c, w, h, x, y);
	}

	@Override
	public void draw(Graphics g) {
		System.out.println(c + " triangle: " + w + "x" + h);
	}

	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
