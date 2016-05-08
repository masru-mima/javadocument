package my.draw.flowchart;

import java.util.List;

import my.draw.core.Box;
import my.draw.core.Drawer;
import my.draw.core.VerticalBox;
import my.poi.Point;

public class TryBox extends VerticalBox {
	private Box son;
	
	public TryBox() {
		this(null);
	}

	public TryBox(Box parent) {
		super(parent);
		super.add(new Box("try"));
	}
	
	public Box getSon() {
		return son;
	}

	public void addResource(String resourceLabel) {
		resourceLabel = "res: " + resourceLabel;
		super.add(new Box(resourceLabel));
	}
	
	public void addCatch(String catchLabel) {
		String label = "catch: " + catchLabel;
		son = new Box(label);
		super.add(son);
	}

	public void addFinally() {
		String label = "finally: ";
		son = new Box(label);
		super.add(son);
	}

	public void add(Box body) {
		if( this.son == null ) {
			this.son = new Box(null);
			super.add(son);
		}
		this.son.add(body);
	}

	@Override
	public int draw(Drawer drawer, Point home) {
		int centerX = home.getX() + size.getWidth()/2;
		int myLines = 0;

		boolean previousIsBreaked = true;
		int previousCaseY = 0;

		Point p = new Point(0, home.getY());
		for(Box son: sons) {
			p.setX(centerX - son.getSize().getWidth()/2);
			int lines = son.draw(drawer,p);
			int currentCaseY = p.getY() + son.getLabelLines() + 1;
			if( lines > 0 ) {
				p.setY(p.getY()+lines);
				myLines += lines;
			}
			if( isCaseClause(son) ) {
				if( !previousIsBreaked ) {
					drawer.line(new Point(centerX,previousCaseY), new Point(centerX,currentCaseY), LineType.THIN);
				}
				if( isBreaked(son) ) {
					previousIsBreaked = true;
				}
				else {
					previousIsBreaked = false;
				}
				previousCaseY = p.getY() - 1;
			}
		}
		
		return myLines;
	}
	
	/**
	 * ボックスのラベルがcase:で始まるか否かを判定する。
	 * @param box
	 * @return
	 */
	private boolean isCaseClause(Box box) {
		String label = box.getLabel();
		if( label != null && label.startsWith("case:") ) {
			return true;
		}
		return false;
	}

	/**
	 * 子ボックスの最後の子のラベルにbreakが含まれるか否かを判定する。
	 * @param box
	 * @return
	 */
	private boolean isBreaked(Box box) {
		List<Box> sons = box.getSons();
		Box lastSon = sons.get(sons.size()-1);
		String label = lastSon.getLabel();
		if( label != null && label.indexOf("break") >= 0 ) {
			return true;
		}
		return false;
	}
}
