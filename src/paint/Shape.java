package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;

public abstract class Shape {

	protected Color c;
	protected int w;
	protected int h;
	protected int x;
	protected int y;
	
	public Shape(Color c, int w, int h, int x, int y) {
		this.c = c;
		this.w = w;
		this.h = h;
		this.x = x;
		this.y = y;
	}
	
	public abstract void draw(Graphics g);
	protected abstract String getShapeType();
	public abstract double area();

	public void save(PrintWriter out) {
		out.println(getShapeType() + "," + c + "," + w + "," + h + "," + x + "," + y);		
	}

	
}
