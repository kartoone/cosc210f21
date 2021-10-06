package paint;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class JavaPaint extends JFrame {

	private ArrayList<Shape> shapes;
	
	public JavaPaint() {
		super("JavaPaint v1.0");
		shapes = new ArrayList<>();
		initUI();
	}
		
	private void initUI() {
		setSize(500, 300);
		setVisible(true);
//		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

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
		EventQueue.invokeLater(() -> {
			try {
			JavaPaint paintWindow = new JavaPaint();
			} catch (Throwable ex) {
		        System.err.println("Uncaught exception - " + ex.getMessage());
		        ex.printStackTrace(System.err);
		    }
//			paintWindow.setVisible(true);
//			paintWindow.show();
		});
	}

	public void show() {
		for (Shape shape : shapes) {
			shape.draw(null);	// this is the polymorphic code in our program ... the "correct" draw method is called for each "Shape" stored in our shapes ArrayList
		}
	}

}
