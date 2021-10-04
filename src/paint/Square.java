package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Square extends Rectangle {

	public Square(Color c, int s, int x, int y) {
		super(c, s, s, x, y);
	}

	@Override
	public void draw(Graphics g) {
		System.out.println(c + " square: " + w + "x" + h);
	}

	
}
