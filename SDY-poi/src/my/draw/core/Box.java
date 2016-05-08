package my.draw.core;

import java.util.ArrayList;
import java.util.List;

import my.poi.Point;
import my.poi.Size;

public class Box extends Shape {

	protected List<Box> sons = new ArrayList<>();
	
	public Box(String label) {
		this(null, label);
	}

	public Box(Box parent, String label) {
		super(label, TextAlign.LEFT, null, new Point(0,0), new BorderLineType(LineType.THIN));
		if( parent != null ) {
			parent.add(this);
		}
	}
	
	public void add(Box son) {
		if( son != null ) {
			sons.add(son);
		}
	}
	
	public List<Box> getSons() {
		return sons;
	}
	
	@Override
	public void reset() {
		Size area = new Size(0,0);
		if( sons.size() > 0 ) {
			area = estimateSonsArea();
			if( area.getWidth() > 0 ) {
				if( this.size == null ) {
					this.size = new Size(area.getWidth(),-1);
				}
				else {
					if( this.size.getWidth()  < area.getWidth() ) {
						this.size.setWidth(area.getWidth());
					}
					this.size.setHeight(-1);
				}
			}
		}
		super.reset();
		if( area.getHeight() > 0 ) {
			size.setHeight(size.getHeight() + area.getHeight()+2);
		}
	}
	
	protected Size estimateSonsArea() {
		Size s = new Size(0,0);
		if( sons.size() > 0 ) {
			boolean found = false;
			for(Box son: sons) {
				son.reset();
				Size s2 = son.getSize();
				if( s2.getHeight() > 0 ) {
					found = true;
					s.setHeight(s.getHeight()+s2.getHeight()+1);
					if( s.getWidth() < s2.getWidth() ) {
						s.setWidth(s2.getWidth());
					}
				}
			}
		
			if( found ) {
				s.setWidth(s.getWidth()+2);
				s.setHeight(s.getHeight()-1);
			}
		}
		
		return s;
	}

	@Override
	public int draw(Drawer drawer, Point home) {
		int centerX = home.getX() + size.getWidth()/2;
		int myLines = super.draw(drawer, home);

		boolean exists = false;
		Point from = null;
		Point p = new Point(0, home.getY()+myLines+1);
		for(Box son: sons) {
			p.setX(centerX - son.getSize().getWidth()/2);
			int lines = son.draw(drawer,p);
			if( lines > 0 ) {
				exists = true;
				Point to = new Point(centerX,p.getY());
				if( from != null ) {
					drawer.line(from, to, LineType.THIN);
				}
				from = new Point(centerX,to.getY()+lines);
				p.setY(p.getY()+lines+1);
				myLines += lines + 1;
			}
		}

		if( exists ) {
			myLines++;
		}

		return myLines;
	}

}
