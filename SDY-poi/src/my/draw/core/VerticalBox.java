package my.draw.core;

import my.poi.Point;
import my.poi.Size;

public class VerticalBox extends Box {
	
	public VerticalBox() {
		this(null);
	}

	public VerticalBox(Box parent) {
		super(parent, null);
	}

	@Override
	public void reset() {
		Size area = new Size(0,0);
		if( sons.size() > 0 ) {
			area = estimateSonsArea();
			this.size = area;
		}
	}	
	
	@Override
	protected Size estimateSonsArea() {
		Size s = new Size(0,0);
		if( sons.size() > 0 ) {
			for(Box son: sons) {
				son.reset();
				Size s2 = son.getSize();
				if( s2.getHeight() > 0 ) {
					s.setHeight(s.getHeight()+s2.getHeight());
					if( s.getWidth() < s2.getWidth() ) {
						s.setWidth(s2.getWidth());
					}
				}
			}
			for(Box son: sons) {
				Size s2 = son.getSize();
				s2.setWidth(s.getWidth());
				s2.setHeight(-1);
				son.reset();
			}
		}
		
		return s;
	}
	
	@Override
	public void add(Box son) {
		if( son != null ) {
			sons.add(son);
			
			if( sons.size() > 1 ) {
				BorderLineType borderLineType = sons.get(sons.size()-2).getBorderLineType();
				if( borderLineType == null ) {
					borderLineType = new BorderLineType();
				}
				borderLineType.setBottom(LineType.DOTTED);
				sons.get(sons.size()-2).setBorderLineType(borderLineType);

				borderLineType = son.getBorderLineType();
				if( borderLineType == null ) {
					borderLineType = new BorderLineType();
				}
				borderLineType.setTop(LineType.DOTTED);
				son.setBorderLineType(borderLineType);
			}
		}
	}


	@Override
	public int draw(Drawer drawer, Point home) {
		int centerX = home.getX() + size.getWidth()/2;
		int myLines = 0;

		Point p = new Point(0, home.getY());
		for(Box son: sons) {
			p.setX(centerX - son.getSize().getWidth()/2);
			int lines = son.draw(drawer,p);
			if( lines > 0 ) {
				p.setY(p.getY()+lines);
				myLines += lines;
			}
		}
		
		return myLines;
	}

}
