package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Line extends Shape {

	public Line(Color c) {
		super(c, 0, 0, 0, 0);
	}

	@Override
	public void draw(Graphics g) {
		System.out.println(c + " line");		
	}

	@Override
	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

}
