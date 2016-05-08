package my.draw.flowchart;

import java.util.List;

import my.draw.core.Box;
import my.draw.core.Drawer;
import my.draw.core.VerticalBox;
import my.poi.Point;

public class DoBox extends VerticalBox {
	private Box son;
	
	public DoBox() {
		this(null);
	}

	public DoBox(Box parent) {
		super(parent);
		son = new Box("loop: ");
		super.add(son);
	}

	public void add(Box body) {
		this.son.add(body);
	}
	
	public void addCondition(String expression) {
		super.add(new Box("while: " + expression));
	}
}
