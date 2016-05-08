package my.draw.core;

import my.poi.Point;
import my.poi.Size;

public class Shape {
	public static enum LineType {
		NONE,
		THIN,
		BOLD,
		DOUBLE,
		DOTTED,
	}
	
	public static enum TextAlign {
		LEFT,
		CENTER,
		RIGHT,
	}

	public static class BorderLineType {
		private LineType top;
		private LineType bottom;
		private LineType left;
		private LineType right;
	
		public BorderLineType() {
			this(LineType.NONE);
		}
		
		public BorderLineType(LineType lineType) {
			this(lineType,lineType,lineType,lineType);
		}
		
		public BorderLineType(LineType top, LineType bottom, LineType left, LineType right) {
			this.top = top;
			this.bottom = bottom;
			this.left = left;
			this.right = right;
		}

		public LineType getTop() {
			return top;
		}

		public void setTop(LineType top) {
			this.top = top;
		}

		public LineType getBottom() {
			return bottom;
		}

		public void setBottom(LineType bottom) {
			this.bottom = bottom;
		}

		public LineType getLeft() {
			return left;
		}

		public void setLeft(LineType left) {
			this.left = left;
		}

		public LineType getRight() {
			return right;
		}

		public void setRight(LineType right) {
			this.right = right;
		}
		
		public void toString(StringBuilder buf) {
			buf.append(top)
			     .append("-").append(bottom)
			     .append("-").append(left)
			     .append("-").append(right)
			     ;
		}
		
		public String toString() {
			StringBuilder buf = new StringBuilder();
			toString(buf);
			return buf.toString();
		}
	}

	/** 図形（外接長方形）のサイズ */
	protected Size size;
	
	/** 図形接続点のX、Yオフセット */
	private Point pin;
	
	/** 枠線の線種 */
	private BorderLineType borderLineType;
	
	/** ラベル */
	private String label;
	
	/** テキスト配置法 */
	private TextAlign align;
	
	/** 描画時のラベル行数 */
	private int labelLines = 0;

	/**
	 * ラベルを指定して枠線なしのインスタンスを作成する。
	 * @param label
	 */
	public Shape(String label) {
		this(label, TextAlign.LEFT, null, null, new BorderLineType(LineType.THIN));
	}
	
	public Shape(String label, TextAlign align) {
		this(label, align, null, null, new BorderLineType(LineType.THIN));
	}

	/**
	 * ラベルと枠線を指定してインスタンスを作成する。
	 * @param label
	 * @param lineType
	 */
	public Shape(String label, TextAlign align, Size size, Point pin, BorderLineType borderLineType) {
		this.label = label;
		this.borderLineType = borderLineType;
		this.size = size;
		this.pin = pin;
		this.align = align;
	}
	
	protected void reset() {
		//枠線
		if( borderLineType == null ) {
			borderLineType = new BorderLineType();
		}
		
		//サイズ
		if( label == null ) {
			if( size == null ) {
				size = new Size(0,0);
			}
			else {
				if( size.getWidth() < 0 ) {
					size.setWidth(0);
				}
				if( size.getHeight() < 0 ) {
					size.setHeight(0);
				}
			}
		}
		else {
			int width = 10;
			if( size != null && size.getWidth() > 0 ) {
				width = size.getWidth();
			}
			String[] lines = Util.lines(label, width);
			int height =lines.length;
			
			if( size == null ) {
				size = new Size(width, height);
			}
			else {
				if( size.getWidth() < 0 ) {
					size.setWidth(width);
				}
				if( size.getHeight() < 0 ) {
					size.setHeight(height);
				}
			}
		}
		
		//ピン位置
		if( pin == null ) {
			pin = new Point(size.getWidth()/2, size.getHeight()/2);
		}
		else {
			if( pin.getX() < 0 ) {
				pin.setX(size.getWidth()/2);
			}
			if( pin.getY() < 0 ){
				pin.setY(size.getHeight()/2);
			}
		}
		
		//ラベルのアライン
		if( align == null ) {
			align = TextAlign.LEFT;
		}
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Point getPin() {
		return pin;
	}

	public void setPin(Point pin) {
		this.pin = pin;
	}

	public BorderLineType getBorderLineType() {
		return borderLineType;
	}

	public void setBorderLineType(BorderLineType borderLineType) {
		this.borderLineType = borderLineType;
	}
	
	public TextAlign getAlign() {
		return align;
	}

	public void setAlign(TextAlign align) {
		this.align = align;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getLabelLines() {
		return labelLines;
	}

	public void toString(StringBuilder buf) {
		buf.append("{");
		buf.append(" label:").append(label);
		buf.append(" align:").append(align);
		buf.append(" size:");
		size.toString(buf);
		buf.append(" pin:");
		pin.toString(buf);
		buf.append(" ");
		borderLineType.toString(buf);
		buf.append(" }");
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		toString(buf);
		return buf.toString();
	}

	public int draw(Drawer drawer, Point base) {
		if( drawer == null ) throw new IllegalArgumentException("missing drawer");
		if( base == null ) throw new IllegalArgumentException("missing base point");
		
		//図形のホームポジションを算出
		int x = base.getX() - getPin().getX();
		int y = base.getY();
		
		Point from, to;
		
		if( size.getWidth() > 0 ) {
			//上罫線の描画
			from = new Point(x,y);
			to = new Point(x+getSize().getWidth(),y);
			drawer.line(from,to,getBorderLineType().getTop());

			if( size.getHeight() > 0 ) {
				//下罫線の描画
				from = new Point(x,y+getSize().getHeight());
				to = new Point(x+getSize().getWidth(),y+getSize().getHeight());
				drawer.line(from,to,getBorderLineType().getBottom());
			}
		}
		
		if( size.getHeight() > 0 ) {
			//左罫線の描画
			from = new Point(x,y);
			to = new Point(x,y+getSize().getHeight());
			drawer.line(from,to,getBorderLineType().getLeft());

			if( size.getWidth() > 0 ) {
				//右罫線の描画
				from = new Point(x+getSize().getWidth(),y);
				to = new Point(x+getSize().getWidth(),y+getSize().getHeight());
				drawer.line(from,to,getBorderLineType().getRight());
			}
		}
		
		//ラベルの出力
		switch(align) {
		case RIGHT:
			x += size.getWidth() - 1;
			break;
		case CENTER:
			x += (size.getWidth() + 1) / 2;
			break;
		case LEFT:
		default:
			// NOP
			break;
		}
		if( size.getHeight() <= 0 ) {
			y--;
		}
		
		String[] lines = Util.lines(label, size.getWidth());
		Point p = new Point(x,y);
		for(String line: lines) {
			drawer.text(p,line,align);
			p.setY(p.getY()+1);
		}
		
		this.labelLines = lines.length;
		return labelLines;
	}

}
