package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Ellipse extends Shape {

	public Ellipse(Color c, int w, int h, int x, int y) {
		super(c, w, h, x, y);
	}

	@Override
	public void draw(Graphics g) {
		System.out.println(c + " ellipse: " + w + "x" + h);
	}


	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
