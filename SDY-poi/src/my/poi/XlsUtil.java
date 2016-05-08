package my.poi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsUtil {
	/**
	 * 作業用ワークブックを作成する。
	 * @return
	 */
	public static Workbook open() {
		return new XSSFWorkbook();
	}

	/**
	 * 指定のエクセルファイルを読み込んだ作業用ワークブックを作成する。
	 * @param xls
	 * @return
	 * @throws IOException
	 */
	public static Workbook open(File xls) throws IOException {
		try(InputStream ins=new BufferedInputStream(new FileInputStream(xls));) {
			Workbook book = new XSSFWorkbook(ins);
			return book;
		}
	}
	
	/**
	 * ワークブックを保存する。
	 * @param book
	 * @param path
	 * @throws IOException
	 */
	public static void saveWorkbook(Workbook book, Path path) throws IOException {
		if( book == null ) throw new IllegalArgumentException("book is null.");
		if( path == null ) throw new IllegalArgumentException("path is null.");
		File file = path.toFile();
		String fname = file.getName();
		
		int p = fname.lastIndexOf(".");
		if( p >= 0 ) {
			fname = fname.substring(0,p);
		}
		if( book instanceof HSSFWorkbook ) {
			fname += ".xls";
		} else {
			fname += ".xlsx";
		}

		try (OutputStream os=new BufferedOutputStream(new FileOutputStream(file))) {
			book.write(os);
		}
	}

	/**
	 * 指定のファイルを読み込み、ワークブックを取得する。
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbook(Path path) throws IOException {
		if( path == null ) throw new IllegalArgumentException("path is null.");
		File file = path.toFile();
		if( !file.exists() ) throw new IllegalArgumentException("file does not exist.");
		if( !file.isFile() ) throw new IllegalArgumentException("file is not a file.");
		String fname = file.getName();
		if(  !fname.endsWith(".xls") && !fname.endsWith(".xlsx") ) throw new IllegalArgumentException("file is not a XSL or XSLX file.");

		try (InputStream ins=new BufferedInputStream(new FileInputStream(file))) {
			Workbook book = null;
			if( fname.endsWith(".xls") ) {
				book = new HSSFWorkbook(ins);
			} else {
				book = new XSSFWorkbook(ins);
			}
			return book;
		}
	}

	/**
	 * 指定のシートからパターンに適合する最初のセル位置を取得する。
	 * @param sheet
	 * @param pattern
	 * @return
	 */
	public static Point searchFirst(Sheet sheet, Pattern pattern) {
		if( sheet == null ) {
			throw new IllegalArgumentException("missing sheet");
		}
		if( pattern == null ) {
			throw new IllegalArgumentException("missing pattern");
		}
		
		int r1 = sheet.getFirstRowNum();
		int r2 = sheet.getLastRowNum();
		for(int r=r1; r<=r2; r++) {
			Row row = sheet.getRow(r);
			if( row == null ) continue;
			int c1 = row.getFirstCellNum();
			int c2 = row.getLastCellNum();
			for(int c=c1; c<=c2; c++) {
				Cell cell = row.getCell(c);
				if( cell == null ) continue;
				Object v = getValue(cell);
				String sv;
				if( v  == null ) {
					sv = "";
				} else {
					sv = v.toString();
				}
				if( pattern.matcher(sv).matches() ) {
					// found
					return new Point(c,r);
				}
			}
		}
		
		//Not found
		return null;
	}
	
	/**
	 * 指定のシートからパターンに適合する全てのセル位置を取得する。
	 * @param sheet
	 * @param pattern
	 * @return
	 */
	public static Point[] searchAll(Sheet sheet, Pattern pattern) {
		if( sheet == null ) {
			throw new IllegalArgumentException("missing sheet");
		}
		if( pattern == null ) {
			throw new IllegalArgumentException("missing pattern");
		}
		List<Point> list = new ArrayList<Point>();
		int r1 = sheet.getFirstRowNum();
		int r2 = sheet.getLastRowNum();
		for(int r=r1; r<=r2; r++) {
			Row row = sheet.getRow(r);
			if( row == null ) continue;
			int c1 = row.getFirstCellNum();
			int c2 = row.getLastCellNum();
			for(int c=c1; c<=c2; c++) {
				Cell cell = row.getCell(c);
				if( cell == null ) continue;
				Object v = getValue(cell);
				String sv;
				if( v  == null ) {
					sv = "";
				} else {
					sv = v.toString();
				}
				if( pattern.matcher(sv).matches() ) {
					// found
					list.add( new Point(r,c) );
				}
			}
		}

		if( list.size() <= 0 ) {
			//Not found
			return null;
		}
		
		return list.toArray(new Point[0]);
	}

	/**
	 * 指定位置のセルを取得する。
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public static Cell getCell(Sheet sheet, int row, int col) {
		if( row < 0 || col < 0 ) throw new IllegalArgumentException("invalid point.  row=" + row + ", col=" + col);
		if( sheet == null ) return null;
		
		Row r = sheet.getRow(row);
		if( r == null ) return null;
		return r.getCell(col);
	}
	
	public static Cell getCell(Sheet sheet, Point point) {
		return getCell(sheet, point.getY(), point.getX());
	}

	/**
	 * 指定位置のセルを取得する。なお、存在しない場合は、該当位置にセルを作成する。
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public static Cell getCellForce(Sheet sheet, int row, int col) {
		if( row < 0 || col < 0 ) throw new IllegalArgumentException("negative value is invalid. row=" + row + ",  col=" + col);
		
		Row r = sheet.getRow(row);
		if( r == null ) {
			r = sheet.createRow(row);
		}
		
		Cell cell = r.getCell(col);
		if( cell == null ) {
			cell = r.createCell(col);
			CellStyle cs = sheet.getWorkbook().createCellStyle();
			cs.cloneStyleFrom(cell.getCellStyle());
			cell.setCellStyle(cs);
		}
		
		return cell;
	}
	
	/**
	 * 指定位置のセル値を取得する。
	 * @param sheet
	 * @param position
	 * @return
	 */
	public static Object getCellValue(Sheet sheet, int row, int col) {
		return getValue( getCell(sheet,row,col) );
	}
	
	/**
	 * セルの値を取得する。
	 * TODO: 日付フォーマットへの対応
	 * @param cell
	 * @return
	 */
	public static Object getValue(Cell cell) {
		if( cell == null ) return null;
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_FORMULA:
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_ERROR:
			return "-error-";
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_STRING:
		default:
			return cell.toString();
		}
	}

	/**
	 * 指定のセルが指定の境界線を持つか否かを問い合わせる。
	 * @param cell
	 * @param border
	 * @return
	 */
	public static boolean hasLine(Cell cell,  Border border) {
		if( border == null ) {
			throw new IllegalArgumentException("missing border");
		}
		if( cell == null  ) return false;
		CellStyle cs = cell.getCellStyle();
		
		int lineType = CellStyle.BORDER_NONE;
		switch(border) {
		case LEFT: lineType = cs.getBorderLeft(); break;
		case TOP: lineType = cs.getBorderTop(); break;
		case RIGHT: lineType = cs.getBorderRight(); break;
		case BOTTOM: lineType = cs.getBorderBottom(); break;
		default: return false;
		}
		
		if( lineType != CellStyle.BORDER_NONE ) {
			return  true;
		}
		return false;
	}
	
	public static boolean hasLine(Sheet sheet, int row, int col, Border border) {
		if( border == null ) {
			throw new IllegalArgumentException("missing border");
		}
		
		Cell cell = getCell(sheet,row,col);
		if( hasLine(cell,border) ) {
			return true;
		}
		
		switch(border) {
		case LEFT:
		default:
			if( col > 0 ) {
				col--;
				border = Border.RIGHT;
			}
			else {
				return false;
			}
			break;
		case RIGHT:
			col++;
			border = Border.LEFT;
			break;
		case TOP:
			if( row > 0 ) {
				row--;
				border = Border.BOTTOM;
			}
			else {
				return false;
			}
			break;
		case BOTTOM:
			row++;
			border = Border.TOP;
			break;
		}

		cell = getCell(sheet,row,col);
		if( hasLine(cell,border) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 指定シートの指定位置をホームとするテーブルのサイズを取得する。
	 * なお、テーブル領域は、ホーム直下の連続罫線で示されるものとする。
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public static Size getTableSize(Sheet sheet, int row, int col) {
		Cell home = getCell(sheet, row, col);
		if( home == null ) return null;
		
		int height = 0;
		int width = 0;

		//縦サーチ
		for(height=0; row+height<=sheet.getLastRowNum(); height++) {
			if( !hasLine(sheet, row+height, col, Border.LEFT) ) {
				break;
			}
		}
		
		//横サーチ
		Row r = sheet.getRow(row);
		if( r != null ) {
			for(width=0; col+width<=r.getLastCellNum(); width++) {
				if( !hasLine(sheet, row, col+width,Border.TOP) ) {
					break;
				}
			}
		}
		
		return new Size(width, height);
	}
	
	/**
	 * 指定位置をホームとする罫線領域のサイズを取得する。
	 * @param sheet
	 * @param row
	 * @param col
	 */
	public static Size getArea(Sheet sheet, int row, int col) {
		int height = 0;
		int width = 0;
		
		int rowmax = sheet.getLastRowNum();
		for( ; row+height<=rowmax; height++) {
			Row r = sheet.getRow(row+height);
			if( r == null ) break;
			int colmax = r.getLastCellNum();
			int w = 0;
			for( ; col+w<=colmax; w++) {
				if( hasLine(sheet,row+height,col+w,Border.RIGHT) ) {
					w++;
					break;
				}
			}
			if( w > width ) {
				width = w;
			}
			if( hasLine(sheet,row+height,col,Border.BOTTOM) ) {
				height++;
				break;
			}
		}
		return new Size(width, height);
	}

	/**
	 * 指定位置をホームとする指定罫線領域内のテキストを取得する。
	 * @param sheet
	 * @param row
	 * @param col
	 */
	public static String getAreaText(Sheet sheet, int row, int col, Size size) {
		if( sheet == null ) {
			throw new IllegalArgumentException("missing sheet");
		}
		if( size == null ) {
			throw new IllegalArgumentException("missing size");
		}
		
		StringBuilder buf = new StringBuilder();
		for(int h=0; h<size.getHeight(); h++) {
			Row r = sheet.getRow(row+h);
			if( r == null ) continue;
			int colmax = r.getLastCellNum();
			for(int w=0; w<size.getWidth(); w++) {
				Object val = getCellValue(sheet,row+h,col+w);
				if( val != null ) {
					buf.append(val);
				}
			}
		}
		return buf.toString();
	}
	
	/**
	 * 指定ホームから始まるテーブルの内容を読み込む。
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public static List<List<String>> getTable(Sheet sheet, int row, int col) {
		int tableHomeRow = row;
		int tableHomeCol = col;
		Size tableSize = getTableSize(sheet,row,col);
		
		List<List<String>> recs = new ArrayList<List<String>>();
		while( row < tableHomeRow + tableSize.getHeight() ) {
			col = tableHomeCol;
			Size area = XlsUtil.getArea(sheet, row, col);
			int rh = area.getHeight();
			List<String> rec = new ArrayList<String>();
			while( col < tableHomeCol + tableSize.getWidth()) {
				area = XlsUtil.getArea(sheet, row, col);
				String val = XlsUtil.getAreaText(sheet, row, col, area);
				rec.add(val);
				//System.out.println("row=" + row + ", col=" + col + " size:" + area + " -> [" + val + "]");
				col += area.getWidth();
			}
			recs.add(rec);
			row += rh;
		}
		
		return recs;
	}
}
