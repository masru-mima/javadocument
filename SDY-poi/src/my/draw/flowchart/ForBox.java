package my.draw.flowchart;

import java.util.List;

import my.draw.core.Box;
import my.draw.core.Drawer;
import my.draw.core.VerticalBox;
import my.poi.Point;

public class ForBox extends VerticalBox {
	private Box son;
	
	public ForBox(String expression) {
		this(null,expression);
	}

	public ForBox(Box parent, String expression) {
		super(parent);
		super.add(new Box("loop: " + expression));
		son = new Box(null);
		super.add(son);
	}

	public void add(Box body) {
		this.son.add(body);
	}
}
