package my.draw.core;

import my.poi.Point;

public interface Drawer {
	public void line(Point from, Point to, Shape.LineType lineType);
	public void text(Point from, String text, Shape.TextAlign align);
}
