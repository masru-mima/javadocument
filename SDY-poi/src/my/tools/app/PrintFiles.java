package my.tools.app;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import my.poi.XlsUtil;

/**
 * 指定のファイル群をエクセルに出力する。
 */
public class PrintFiles implements Closeable {
	public static void main(String[] args) throws Exception {
		if( args.length <= 0 ) {
			args = new String[]{
					"test/files.xlsx",  //出力先のエクセルファイル
					//"../SDY-ast/src", "(.*)\\.java",  //ファイル置き場のルートディレクトリとファイルパターン
					"/Users/mima/a.txt", "(.*)\\.txt",
					"UTF-8", //ファイルの文字エンコーディング
			};
		}

		File xls = new File(args[0]);
		File root = new File(args[1]).getAbsoluteFile();
		Pattern pattern = Pattern.compile(args[2]);
		String enc = args[3];
		try(PrintFiles m = new PrintFiles(xls, root, pattern, enc);) {
			m.setTargetFiles(root);
			m.writeFiles();
		}
	}
		
	/** 出力先のエクセルファイル */
	private File xls;
	
	/** ファイル置き場のディレクトリ */
	private File root;
	
	/** ファイル置き場のパス長 */
	private int rootLength;
	
	/** 出力対象のファイルパターン */
	private Pattern pattern;
	
	/** 出力対象ファイルの文字エンコーディング */
	private String enc;
	
	/** 出力対象ファイル一覧 */
	private Set<File> targets = new TreeSet<>();
	
	/** 作業用エクセルブック */
	private Workbook book;
	
	/**
	 * 出力先のエクセルファイル、ファイル置き場、出力対象ファイルパターンを指定してインスタンスを作成する。
	 * @param xls
	 * @param root
	 * @param pattern
	 * @param enc
	 */
	public PrintFiles(File xls, File root, Pattern pattern, String enc) {
		this.xls = xls;
		this.root = root;
		this.pattern = pattern;
		this.enc = enc;
		this.book = XlsUtil.open();
		String path = root.getAbsolutePath().replace("\\", "/");
		if( path.endsWith("/") ) {
			this.rootLength = path.length();
		}
		else {
			this.rootLength = path.length() + 1;
		}
	}

	/**
	 * 作業用エクセルブックの内容を指定のエクセルファイルに出力する。
	 * @throws IOException エクセルファイル出力時エラー
	 */
	public void close() throws IOException {
		try(OutputStream os=new BufferedOutputStream(new FileOutputStream(xls));) {
			book.write(os);
		}
	}
	
	/**
	 * 引数で指定したディレクトリ以下に存在するファイルで、出力対象ファイルパターンに合致するファイルをエクセルブックに出力する。
	 * @param root ファイル探索開始ディレクトリ
	 * @throws IOException 
	 */
	public void setTargetFiles(File root) {
		/**
		 * 引数がファイルの場合、出力対象ファイルパターンに合致していれば、ファイル一覧に追加する。
		 */
		if( root.isFile() ) {
			if( pattern.matcher(root.getAbsolutePath().replace("\\", "/")).matches() ) {
				targets.add(root);
			}
			return;
		}
		
		/**
		 * 引数がディレクトリの場合、直下のファイル一覧を取得する。
		 */
		File[] sons = root.listFiles();
		
		/**
		 * 子ファイルが存在しない場合、終了する。
		 */
		if( sons == null || sons.length <= 0 ) {
			return;
		}
		
		/**
		 * 直下のファイルについて、該当ファイルをエクセルブックに出力する。
		 */
		for(File son: sons) {
			if( son.isFile() ) {
				if( pattern.matcher(son.getAbsolutePath().replace("\\", "/")).matches() ) {
					targets.add(son);
				}
			}
		}
		
		/**
		 * 直下のサブディレクトリについて、本処理を再帰的に適用し、サブディレクトリ以下の該当ファイルをエク
		 * セルブックに出力する。
		 */
		for(File son: sons) {
			if( son.isDirectory() ) {
				setTargetFiles(son);
			}
		}
	}

	/**
	 * 各出力対象ファイルをエクセルブックに出力する。
	 * @throws IOException
	 */
	public void writeFiles() throws IOException {
		for(File file: targets) {
			writeFile(file);
		}
	}
	
	/**
	 * ファイルをエクセルブックのファイル名シートに出力する。
	 * @param file
	 * @throws IOException
	 */
	public void writeFile(File file) throws IOException {
		/**
		 * 新規に出力先シートを作成する。
		 */
		String sheetName = file.getName();
		int sheetIndex = book.getSheetIndex(sheetName);
		if( sheetIndex >= 0 ) {
			book.removeSheetAt(sheetIndex);
		}
		Sheet sheet = book.createSheet(sheetName);

		try(BufferedReader rdr=new BufferedReader(new InputStreamReader(new FileInputStream(file),enc));) {
			int lcnt = 0; //行番号
			int r=0; //エクセル行番号
			int cNo = 0;  //エクセル行番号欄の番号
			int cLine = 1; //エクセル行内容欄の番号
			
			/**
			 * ファイル名を出力する。
			 */
			{
				Cell cell = XlsUtil.getCellForce(sheet, r, cNo);
				cell.setCellValue("File");
				cell = XlsUtil.getCellForce(sheet, r, cLine);
				String path = file.getAbsolutePath();
				if( rootLength < path.length() ) {
					cell.setCellValue(path.substring(rootLength));
				}
				else {
					cell.setCellValue(path);					
				}
				r++;
			}

			/**
			 * カラム名を出力する。
			 */
			{
				Cell cell = XlsUtil.getCellForce(sheet, r, cNo);
				cell.setCellValue("行番号");
				cell = XlsUtil.getCellForce(sheet, r, cLine);
				cell.setCellValue("行内容");
				r++;
			}

			String line;
			while( (line=rdr.readLine()) != null ) {
				lcnt++;
				Cell cell = XlsUtil.getCellForce(sheet, r, cNo);
				cell.setCellValue(lcnt);
				cell = XlsUtil.getCellForce(sheet, r, cLine);
				cell.setCellValue(line);
				r++;
			}
		}
	}
	
}
