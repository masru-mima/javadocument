package my.draw.flowchart;

import my.draw.core.Box;
import my.draw.core.Drawer;
import my.draw.core.Shape.LineType;
import my.poi.Point;
import my.poi.Size;

public class FlowchartBox extends Box {
	
	public FlowchartBox(String label) {
		this(null, label);
	}
	
	public FlowchartBox(Box parent, String label) {
		super(parent, label);
	}

	@Override
	public void reset() {
		super.reset();
		if( sons.size() > 0 ) {
			size.setHeight(size.getHeight()+4); // [開始]--[終了]の４行を加算する。
		}
	}
	
	@Override
	public int draw(Drawer drawer, Point home) {
		if( sons.size() > 0 ) {
			Box box;
			box = new Box("開　始");
			box.setSize(new Size(2,1));
			sons.add(0, box);

			box = new Box("終　了");
			box.setSize(new Size(2,1));
			sons.add(box);
		}

		try {
			return super.draw(drawer, home);
		}
		finally {
			if( sons.size() > 0 ) {
				sons.remove(0);
				sons.remove(sons.size()-1);
			}
		}
	}
}
