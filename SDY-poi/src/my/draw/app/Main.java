package my.draw.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import my.draw.core.Box;
import my.draw.core.Drawer;
import my.draw.core.DrawerImplForXls;
import my.draw.core.Shape.LineType;
import my.draw.core.Shape.TextAlign;
import my.draw.flowchart.DoBox;
import my.draw.flowchart.FlowchartBox;
import my.draw.flowchart.ForBox;
import my.draw.flowchart.IfBox;
import my.draw.flowchart.SwitchBox;
import my.draw.flowchart.TryBox;
import my.draw.flowchart.WhileBox;
import my.draw.flowchart.io.FlowchartReader;
import my.poi.Point;
	
public class Main implements Closeable {
	public static void main(String[] args) throws Exception {
		if( args.length <= 0 ) {
			args = new String[]{
					"test/data/test.xlsx",
					"test/output.xlsx",
					"test/data/test_1.txt",
			};
		}
		File xlsFile = new File(args[0]);
		File outFile = new File(args[1]);
		String txt = null;
		if( args.length > 2 ) {
			txt = args[2];
		}
		
		try(Main m = new Main(xlsFile,outFile);) {
			if( txt == null ) {
				m.draw1();
			}
			else {
				m.draw2(txt);
			}
		}
		
	}

	private File xlsFile;
	private File outFile;
	private Workbook book;
	
	public Main(File xlsFile, File outFile) throws IOException {
		this.xlsFile = xlsFile;
		this.outFile = outFile;

		try(InputStream ins=new BufferedInputStream(new FileInputStream(xlsFile));) {
			this.book = new XSSFWorkbook(ins);
		}
	}

	@Override
	public void close() throws IOException {
		try(OutputStream os=new BufferedOutputStream(new FileOutputStream(outFile));) {
			book.write(os);
		}
	}
	
	public void draw1() {
		Sheet sheet = book.createSheet("sample1");
		book.setActiveSheet(book.getSheetIndex(sheet));
		sheet.setDefaultColumnWidth(3); //3セルに漢字5 文字の割合
		sheet.setDefaultRowHeightInPoints(18); // 14ptの漢字にフィットする。
		Drawer drawer = new DrawerImplForXls(sheet);
		Point p1 = new Point(5,5);
		Point p2 = new Point(5,10);
		drawer.line(p1, p2, LineType.BOLD);
		drawer.text(p1, "愛うえお", TextAlign.LEFT);
		
		//Shape shape = new Shape("シェイプaaaaa愛うえおカキクケコさしすせそアイウエオaaaaaaabbbbbbbbbbbbccccccテスト", TextAlign.LEFT,new Size(10,-1),new Point(0,0),new BorderLineType(LineType.THIN));
		Box root = new FlowchartBox("XXX処理フロー");
		Box box2 = new Box(root,"def");
		Box box3 = new Box(root,"シェイプ3 aaaaa愛うえおカキクケコさしすせそアイウエオaaaaaaabbbbbbbbbbbbccccccテスト");
		Box box4 = new Box(box3,"シェイプ4 aaaaa愛うえおカキクケコさしすせそアイウエオaaaaaaabbbbbbbbbbbbccccccテスト");
		Box box5 = new Box(box3,"シェイプ5 aaaaa愛うえおカキクケコさしすせそアイウエオaaaaaaabbbbbbbbbbbbccccccテスト");

		Box body;
		
		// IF ... ELSE IF ... ELSE ...
		IfBox ifbox = new IfBox(root);
		ifbox.addCondition("条件１");
		body = new Box("body1");
		ifbox.add(body);
		Box ifbox21 = new Box(body,"body1-1");
		Box ifbox22 = new Box(body,"body1-2");
		Box ifbox221 = new Box(ifbox22,"body1-2-1");
		Box ifbox222 = new Box(ifbox22,"body1-2-2");
		ifbox.addCondition("条件２");
		body = new Box("body２");
		ifbox.add(body);
		ifbox.addCondition(null);
		body = new Box("body３");
		ifbox.add(body);

		// SWITCH
		SwitchBox switchbox = new SwitchBox(root, "fileType");
		switchbox.addCase("TEXT");
		body = new Box("case body1");
		switchbox.add(body);
		body = new Box("break");
		switchbox.add(body);
		switchbox.addCase(" BINARY");
		body = new Box("case body2");
		switchbox.add(body);
		switchbox.addCase(null);
		body = new Box("default body３");
		switchbox.add(body);
		
		// TRY ... CATCH ... FINALLY
		TryBox trybox = new TryBox(root);
		trybox.addResource("InputStream ins = new FileInputStream(infile);");
		trybox.addResource("OutputStream os = new FileOutputStream(outfile);");
		body = new Box("body-1");
		trybox.add(body);
		body = new Box("body-2");
		trybox.add(body);
		trybox.addCatch("IOExpression e");
		body = new Box("catch body-1");
		trybox.add(body);
		body = new Box("catch body-2");
		trybox.add(body);
		trybox.addFinally();
		body = new Box("finally body-1");
		trybox.add(body);

		// WHILE
		WhileBox whilebox = new WhileBox(root, "条件１");
		whilebox.add(new Box("body1"));
		whilebox.add(new Box("body2"));

		// DO ... WHILE
		DoBox dobox = new DoBox(root);
		body = new Box("body1");
		dobox.add(body);
		body = new Box("body2");
		dobox.add(body);
		dobox.addCondition("条件１");

		// FOR ... / EnhancedFOR ...
		ForBox forbox = new ForBox(root,"int i=0; i<=10; i++");
		forbox.add(new Box("body1"));
		forbox.add(new Box("body2"));
		
		root.reset();
		root.draw(drawer, new Point(10,20));
	}

	public void draw2(String fileName) throws IOException {
		Sheet sheet = book.createSheet("sample2");
		book.setActiveSheet(book.getSheetIndex(sheet));
		sheet.setDefaultColumnWidth(3); //3セルに漢字5 文字の割合
		sheet.setDefaultRowHeightInPoints(18); // 14ptの漢字にフィットする。
		Drawer drawer = new DrawerImplForXls(sheet);

		Point home = new Point(1,1);
		File file = new File(fileName);
		String enc = "UTF-8";
		try(FlowchartReader rdr=new FlowchartReader(file,enc);){
			while(true) {
				Box box = rdr.read();
				if( box == null ) {
					break;
				}
				box.reset();
				int height = box.draw(drawer, home);
				home.setY(home.getY()+height+1);
			}
		}
	}
}
