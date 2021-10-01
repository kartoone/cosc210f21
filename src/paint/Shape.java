package paint;

import java.awt.Color;
import java.awt.Graphics;

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
	public abstract double area();
	
}
