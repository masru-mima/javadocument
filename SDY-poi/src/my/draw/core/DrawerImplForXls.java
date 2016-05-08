package my.draw.core;

import my.draw.core.Shape.LineType;
import my.draw.core.Shape.TextAlign;
import my.poi.Point;
import my.poi.XlsUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

public class DrawerImplForXls implements Drawer {
	private static final int CROSS = 0;
	private static final int UPSLANT = 1;
	private static final int DOWNSLANT = 2;

	private Sheet sheet;
	private CellStyle[][] slantCs;
	private CellStyle alignRightCs;
	
	public DrawerImplForXls(Sheet sheet) {
		this.sheet = sheet;
		loadSlantCellStyles();
	}
	
	private void loadSlantCellStyles() {
		Sheet tsheet = sheet.getWorkbook().getSheet("_template_1");
		slantCs = new CellStyle[Shape.LineType.values().length][3];
		//細い実線
		slantCs[Shape.LineType.THIN.ordinal()][CROSS] = XlsUtil.getCell(tsheet, new Point(5,1)).getCellStyle();
		slantCs[Shape.LineType.THIN.ordinal()][UPSLANT] = XlsUtil.getCell(tsheet, new Point(6,1)).getCellStyle();
		slantCs[Shape.LineType.THIN.ordinal()][DOWNSLANT] = XlsUtil.getCell(tsheet, new Point(7,1)).getCellStyle();
		//細い点線
		slantCs[Shape.LineType.DOTTED.ordinal()][CROSS] = XlsUtil.getCell(tsheet, new Point(5,2)).getCellStyle();
		slantCs[Shape.LineType.DOTTED.ordinal()][UPSLANT] = XlsUtil.getCell(tsheet, new Point(6,2)).getCellStyle();
		slantCs[Shape.LineType.DOTTED.ordinal()][DOWNSLANT] = XlsUtil.getCell(tsheet, new Point(7,2)).getCellStyle();
		//二重線
		slantCs[Shape.LineType.DOUBLE.ordinal()][CROSS] = XlsUtil.getCell(tsheet, new Point(5,3)).getCellStyle();
		slantCs[Shape.LineType.DOUBLE.ordinal()][UPSLANT] = XlsUtil.getCell(tsheet, new Point(6,3)).getCellStyle();
		slantCs[Shape.LineType.DOUBLE.ordinal()][DOWNSLANT] = XlsUtil.getCell(tsheet, new Point(7,3)).getCellStyle();
		//太い実線
		slantCs[Shape.LineType.BOLD.ordinal()][CROSS] = XlsUtil.getCell(tsheet, new Point(5,4)).getCellStyle();
		slantCs[Shape.LineType.BOLD.ordinal()][UPSLANT] = XlsUtil.getCell(tsheet, new Point(6,4)).getCellStyle();
		slantCs[Shape.LineType.BOLD.ordinal()][DOWNSLANT] = XlsUtil.getCell(tsheet, new Point(7,4)).getCellStyle();
		//右寄せ
		alignRightCs = XlsUtil.getCell(tsheet, new Point(5,6)).getCellStyle();
}

	@Override
	public void line(Point from, Point to, LineType lineType) {
		if( from.getX() == to.getX() ) {
			//縦線
			int x = from.getX();
			int y1 = Math.min(from.getY(), to.getY());
			int y2 = Math.max(from.getY(), to.getY());
			for(int y=y1; y<y2; y++) {
				left(x,y,lineType);
			}
		}
		else if( from.getY() == to.getY() ) {
			//横線
			int y = from.getY();
			int x1 = Math.min(from.getX(), to.getX());
			int x2 = Math.max(from.getX(), to.getX());
			for(int x=x1; x<x2; x++) {
				top(x,y,lineType);
			}
		}
		else if( from.getX() == to.getX()-1 ) {
			if( from.getY() == to.getY()+1 ) {
				//右上斜線
				int x = from.getX();
				int y = from.getY() - 1;
				upslant(x,y,lineType);
			}
			else if( from.getY() == to.getY()-1 ) {
				//右下斜線
				int x = from.getX();
				int y = from.getY();
				downslant(x,y,lineType);
			}
		}
	}
	
	private void left(int x, int y, LineType lineType) {
		Cell cell = XlsUtil.getCellForce(sheet, y, x);
		CellStyle cs =cell.getCellStyle();
		switch(lineType) {
		case NONE:
		default:
			cs.setBorderLeft(CellStyle.BORDER_NONE);
			break;					
		case THIN:
			cs.setBorderLeft(CellStyle.BORDER_THIN);
			break;
		case BOLD:
			cs.setBorderLeft(CellStyle.BORDER_THICK);
			break;
		case DOUBLE:
			cs.setBorderLeft(CellStyle.BORDER_DOUBLE);
			break;
		case DOTTED:
			cs.setBorderLeft(CellStyle.BORDER_DOTTED);
			break;
		}
	}

	private void top(int x, int y, LineType lineType) {
		Cell cell = XlsUtil.getCellForce(sheet, y, x);
		CellStyle cs =cell.getCellStyle();
		switch(lineType) {
		case NONE:
		default:
			cs.setBorderTop(CellStyle.BORDER_NONE);
			break;					
		case THIN:
			cs.setBorderTop(CellStyle.BORDER_THIN);
			break;
		case BOLD:
			cs.setBorderTop(CellStyle.BORDER_THICK);
			break;
		case DOUBLE:
			cs.setBorderTop(CellStyle.BORDER_DOUBLE);
			break;
		case DOTTED:
			cs.setBorderTop(CellStyle.BORDER_DOTTED);
			break;
		}
	}

	private void upslant(int x, int y, LineType lineType) {
		Cell cell = XlsUtil.getCellForce(sheet, y, x);
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		cs.cloneStyleFrom(slantCs[lineType.ordinal()][UPSLANT]);
		cell.setCellStyle(cs);
	}

	public void downslant(int x, int y, LineType lineType) {
		Cell cell = XlsUtil.getCellForce(sheet, y, x);
		CellStyle cs = sheet.getWorkbook().createCellStyle();
		cs.cloneStyleFrom(slantCs[lineType.ordinal()][DOWNSLANT]);
		cell.setCellStyle(cs);
	}
	
	@Override
	public void text(Point from, String text, TextAlign align) {
		Cell cell = XlsUtil.getCellForce(sheet, from.getY(), from.getX());
		cell.setCellValue(text);

		CellStyle cs = cell.getCellStyle();
		
		switch(align) {
		case LEFT:
		default:
			cs.setAlignment(CellStyle.ALIGN_LEFT);
			break;
		case RIGHT:
			cs.setAlignment(CellStyle.ALIGN_RIGHT);
			cs.cloneStyleFrom(alignRightCs);
			break;
		case CENTER:
			cs.setAlignment(CellStyle.ALIGN_CENTER);
			break;
		}
	}

}
