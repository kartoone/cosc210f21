package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
		if (this instanceof Line) {
			Line l = (Line) this;
			out.print(getShapeType() + "," + c.getRGB() + "," + 0 + "," + 0 + "," + 0 + "," + 0);
			for (Point p : l.points) {
				out.print("," + p.x + "-" + p.y);
			}
			out.println();
		} else {
			out.println(getShapeType() + "," + c.getRGB() + "," + w + "," + h + "," + x + "," + y);
		}
	}

	
}
