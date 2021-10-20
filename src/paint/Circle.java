package paint;

import java.awt.Color;

public class Circle extends Ellipse {

	public Circle(Color c, int d, int x, int y) {
		super(c, d, d, x, y);
	}
	
	@Override
	protected String getShapeType() {
		return "Circle";
	}

}
