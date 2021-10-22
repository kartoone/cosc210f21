package paint;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JOptionPane;

public class JavaPaint extends JFrame implements MouseListener, MouseMotionListener {

	public enum ShapeSelection {
		RECTANGLE, SQUARE, LINE,
		ELLIPSE, CIRCLE, TRIANGLE
	}
	
	private ArrayList<Shape> shapes; // the shapes that have been "dropped" onto our painting

	private ShapeSelection currentShape = ShapeSelection.RECTANGLE; // defaults to whatever the first enum is
	private int currentWidth = 100;
	private int currentHeight = 50;
	private Color currentColor = Color.red;
	
	private Line currentLine = null;
	
	public JavaPaint() {
		super("JavaPaint");
		shapes = new ArrayList<>();
		initUI();
	}
		
	private void initUI() {
		setupMenuBar();
		setSize(750,500);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

// Example code for creating shapes		
//		Rectangle r1 = new Rectangle(Color.red, 20, 30, 0, 0);
//		Rectangle r2 = new Rectangle(Color.green, 20, 50, 100, 100);
//		Square s1 = new Square(Color.pink, 50, 100, 100);
//		Triangle t1 = new Triangle(Color.cyan, 35, 50, 75, 75);
//		//r1.draw(null);
//		//r2.draw(null);
//		//s1.draw(null);
//		shapes.add(r1);
//		shapes.add(r2);
//		shapes.add(s1);
//		shapes.add(t1);
	}

	private void setupMenuBar() {
		JMenuBar menubar = new JMenuBar();		
		JMenu filemenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem exit = new JMenuItem("Exit");
		filemenu.add(open);
		filemenu.add(save);
		filemenu.add(exit);
		menubar.add(filemenu);

		JMenu shapemenu= new JMenu("Shape");
		JRadioButtonMenuItem line = new JRadioButtonMenuItem("Line");
		shapemenu.add(line);
		shapemenu.addSeparator();
		JRadioButtonMenuItem rectangle = new JRadioButtonMenuItem("Rectangle", true);  // the second parameter "true" makes this the default selected shape
		JRadioButtonMenuItem square = new JRadioButtonMenuItem("Square");
		shapemenu.add(rectangle);
		shapemenu.add(square);
		shapemenu.addSeparator();
		JRadioButtonMenuItem ellipse = new JRadioButtonMenuItem("Ellipse");
		JRadioButtonMenuItem circle = new JRadioButtonMenuItem("Circle");
		shapemenu.add(ellipse);
		shapemenu.add(circle);
		shapemenu.addSeparator();
		JRadioButtonMenuItem triangle = new JRadioButtonMenuItem("Triangle");
		shapemenu.add(triangle);

		// the ButtonGroup code below is what makes it so that when you select one shape the other is automatically deselected
		ButtonGroup group = new ButtonGroup();
		group.add(line);
		group.add(rectangle);
		group.add(square);
		group.add(ellipse);
		group.add(circle);
		group.add(triangle);
		
		menubar.add(shapemenu);
		
		JMenu propmenu = new JMenu("Properties");
		JMenuItem size = new JMenuItem("Size");
		JMenuItem color = new JMenuItem("Color");
		propmenu.add(size);
		propmenu.add(color);
		menubar.add(propmenu);

		JMenu infomenu = new JMenu("Info");
		JMenuItem count = new JMenuItem("Count Shapes");
		JMenuItem list = new JMenuItem("List Shapes");
		infomenu.add(count);
		infomenu.add(list);
		menubar.add(infomenu);
		
		setJMenuBar(menubar);
		
		exit.addActionListener((event) -> System.exit(0));
		open.addActionListener((event) -> handleOpen());
		save.addActionListener((event) -> handleSave());
		size.addActionListener((event) -> updateSize());
		color.addActionListener((event) -> updateColor());
		list.addActionListener((event) -> listShapes());
		count.addActionListener((event) -> countShapes());
		line.addActionListener((event) -> switchShapes(ShapeSelection.LINE));
		rectangle.addActionListener((event) -> switchShapes(ShapeSelection.RECTANGLE));
		square.addActionListener((event) -> switchShapes(ShapeSelection.SQUARE));
		ellipse.addActionListener((event) -> switchShapes(ShapeSelection.ELLIPSE));
		circle.addActionListener((event) -> switchShapes(ShapeSelection.CIRCLE));
		triangle.addActionListener((event) -> switchShapes(ShapeSelection.TRIANGLE));

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private void handleOpen() {
		System.out.println("opening file...");
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			openFile(fc.getSelectedFile());
		}	
	}

	// Algorithm
	//   1. Delete all shapes from our "shapes" ArrayList
	//   2. As we parse the file, create and add the "correct" shape into the ArrayList
	//   3. Call "repaint()" to paint our newly created shapes into the window
	private void openFile(File selectedFile) {
		try {
			// Step 1: delete all shapes
			shapes.clear();
			
			// Step 2: parse the file and create the shapes
			Scanner in = new Scanner(selectedFile);
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String parts[] = line.split(",");
				System.out.println(line);
				Shape s = null;
				Color c = new Color(Integer.parseInt(parts[1]));
				int w = Integer.parseInt(parts[2]);
				int h = Integer.parseInt(parts[3]);
				int x = Integer.parseInt(parts[4]);
				int y = Integer.parseInt(parts[5]);
				switch(parts[0]) {
				case "Rectangle":
					System.out.println("creating rectangle");
					s = new Rectangle(c, w, h, x, y);
					break;
				case "Square":
					System.out.println("creating square");
					s = new Square(c, h, x, y);
					break;
				case "Ellipse":
					System.out.println("creating ellipse");
					s = new Ellipse(c, w, h, x, y);
					break;
				case "Circle":
					System.out.println("creating circle");
					s = new Circle(c, h, x, y);
					break;
				case "Triangle":
					System.out.println("creating triangle");
					s = new Triangle(c, w, h, x, y);
					break;
				case "Line":
					System.out.println("creating line");
					s = new Line(c);
					for (int i=6; i<parts.length; i++) {
						String coords[] = parts[i].split("-");
						int px = Integer.parseInt(coords[0]);
						int py = Integer.parseInt(coords[1]);
						((Line)s).points.add(new Point(px,py));
					}
					break;
				}
				
				// 3. Store the newly created shape in our list of shapes
				if (s!=null) {
					shapes.add(s);
				}
				
				repaint();
			}
		} catch (FileNotFoundException e) {

		}
		
		
	}

	private void updateColor() {
		Color newColor = JColorChooser.showDialog(null, "Choose a color", currentColor);
	    if (newColor != null) {
	    	currentColor = newColor;
	    }
	}

	private void updateSize() {
		String userinput = JOptionPane.showInputDialog("Enter a new width");
		currentWidth = Integer.parseInt(userinput);
		userinput = JOptionPane.showInputDialog("Enter a new height (will be used for squares, circles)");
		currentHeight = Integer.parseInt(userinput);
	}

	private void switchShapes(ShapeSelection selectedShape) {
		currentShape = selectedShape;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (Shape shape : shapes) {
			shape.draw(g);	// this is the polymorphic code in our program ... the "correct" draw method is called for each "Shape" stored in our shapes ArrayList
		}
	}

	private void handleSave() {
		System.out.println("saving file...");
		JFileChooser fc = new JFileChooser();
		int result = fc.showSaveDialog(null);
		//System.out.println(result);
		if (result == JFileChooser.APPROVE_OPTION) {
			saveFile(fc.getSelectedFile());
		}
	}

	private void saveFile(File selectedFile) {
		try {
			PrintWriter out = new PrintWriter(selectedFile);
			for (Shape shape : shapes) {
				shape.save(out);
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}		
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JavaPaint paintWindow = new JavaPaint();
		});
	}

	// Example code for printing all the shapes into the console window
	public void countShapes() {
		System.out.println("You have added " + shapes.size() + " shape(s) to the drawing.");
	}

	// Polymorphic code for printing all the shapes into the console window
	public void listShapes() {
		// Extra check to make sure something is printed even if we haven't added any shapes yet.
		if (shapes.size()==0) {
			System.out.println("You haven't added any shapes yet!");
		}
		for (Shape shape : shapes) {
			shape.draw(getGraphics());	// this is the polymorphic code in our program ... the "correct" draw method is called for each "Shape" stored in our shapes ArrayList
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouse clicked: " + e.getX() + ", " + e.getY());
		switch (currentShape) {
		case RECTANGLE:			
			shapes.add(new Rectangle(currentColor, currentWidth, currentHeight, e.getX(), e.getY()));
			break;
		case SQUARE: 
			shapes.add(new Square(currentColor, currentHeight, e.getX(), e.getY()));
			break;
		case ELLIPSE: 
			shapes.add(new Ellipse(currentColor, currentWidth, currentHeight, e.getX(), e.getY()));
			break;
		case CIRCLE: 
			shapes.add(new Circle(currentColor, currentHeight, e.getX(), e.getY()));
			break;		
		case TRIANGLE: 
			shapes.add(new Triangle(currentColor, currentWidth, currentHeight, e.getX(), e.getY()));
			break;
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) { currentLine = null; }

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println(e.getX() + "," + e.getY());
		if (currentLine == null) {
			currentLine = new Line(currentColor);
			shapes.add(currentLine);
		}
		currentLine.points.add(new Point(e.getX(), e.getY()));
		repaint();

// The code below would force you to select "line" from
// the menu before you can draw a line.
//		if (currentShape == ShapeSelection.LINE) {
//			if (currentLine == null) {
//				currentLine = new Line(Color.RED);
//				shapes.add(currentLine);
//			}
//			currentLine.points.add(new Point(e.getX(), e.getY()));
//			repaint();
//		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
