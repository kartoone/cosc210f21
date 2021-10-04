package paint;

import java.awt.Color;
import java.util.ArrayList;

public class JavaPaint {

	private ArrayList<Shape> shapes;
	
	public JavaPaint() {
		shapes = new ArrayList<>();
		initUI();
	}
		
	private void initUI() {
		Rectangle r1 = new Rectangle(Color.red, 20, 30, 0, 0);
		Rectangle r2 = new Rectangle(Color.green, 20, 50, 100, 100);
		Square s1 = new Square(Color.pink, 50, 100, 100);
		Triangle t1 = new Triangle(Color.cyan, 35, 50, 75, 75);
		//r1.draw(null);
		//r2.draw(null);
		//s1.draw(null);
		shapes.add(r1);
		shapes.add(r2);
		shapes.add(s1);
		shapes.add(t1);
	}

	public static void main(String[] args) {
		JavaPaint paintWindow = new JavaPaint();
		paintWindow.show();
	}

	public void show() {
		for (Shape shape : shapes) {
			shape.draw(null);
		}
	}

}
