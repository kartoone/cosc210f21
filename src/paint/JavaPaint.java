package paint;

import java.awt.Color;

public class JavaPaint {

	public static void main(String[] args) {
		Rectangle r1 = new Rectangle(Color.red, 20, 30, 0, 0);
		Rectangle r2 = new Rectangle(Color.green, 20, 50, 100, 100);
		r1.draw(null);
		r2.draw(null);
	}

}
