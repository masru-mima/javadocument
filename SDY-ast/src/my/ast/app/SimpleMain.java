package my.ast.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import my.ast.box.Box;
import my.ast.poi.Sketch;
import my.ast.util.ASTNodeUtil;
import my.ast.util.Element;
import my.ast.util.ElementListUtil;
import my.ast.util.SimpleVisitor;

public class SimpleMain {
	public static void main(String[] args) throws Exception {	
		if( args.length <= 0 ) {
			args = new String[]{
					"src/test/AllStatements.java", "UTF-8",
					//"src/test/IfStatement.java", "UTF-8",
					//"src/test/TryStatement.java", "UTF-8",
					//"src/test/WhileStatement.java", "UTF-8",
					//"src/test/DoStatement.java", "UTF-8",
					//"src/test/ForStatement.java", "UTF-8",
					//"src/test/EnhancedForStatement.java", "UTF-8",
					//"src/test/LambdaExpression.java", "UTF-8",  //TerminalBoxとしての扱いで実装
					//"src/test/SwitchStatement.java", "UTF-8",
					//"src/test/Comment.java", "UTF-8",
			};
		}
		File src = new File(args[0]);
		String enc = args[1];
		
		SimpleMain m = new SimpleMain(src,enc);
		m.parse();
	}
	
	private File src;
	private String enc;
	
	public SimpleMain(File src, String enc) {
		this.src = src;
		this.enc = enc;
	}

	/**
	 * ソースコードを解析し、制御構造情報を作成する。
	 * @throws IOException
	 */
	public void parse() throws IOException {
		//ソースコードを文字列として取得する。
		byte[] content = Files.readAllBytes(src.toPath());
		String source = new String(content,enc);
		
		//パーサを作成する。
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		//パーサにソースコードの文字配列を設定する。
		parser.setSource(source.toCharArray());
		
		//ソースコードを解析し、コンピレーションユニットを作成する。
		CompilationUnit unit = (CompilationUnit)parser.createAST(null);
		
		//ノード操作ユーティリティを作成する。
		ASTNodeUtil au = new ASTNodeUtil(unit,source);
		
		//ビジターを作成しコンピレーションユニットに適用し、エレメントリストを作成する。
		List<Element> elements = new ArrayList<>();
		ASTVisitor visitor = new SimpleVisitor(au,elements);
		unit.accept(visitor);
		
		//エレメントリスト操作ユーティリティを作成する。
		ElementListUtil eu = new ElementListUtil(au,elements);
		
		//エレメントリストにコメントをマージする。
		eu.mergeComment(unit.getCommentList());
			
		//ASTノード階層数を設定する。
		eu.setASTNodeLevel();
		
		//エレメント一覧をダンプ出力する。
		for(Element element: elements) {
			for(int i=0; i<element.getLevel(); i++) {
				System.out.print("\t");
			}
			System.out.println(element);
		}
		
		//エレメント一覧を構文ボックス構造に変換する。
		Box root = Box.make(eu, 0);
		System.out.println("----------------------------");	
		System.out.println(root);
		System.out.println("----------------------------");
		
		//構文ボックス構造をスケッチに変換する。
		StringBuilder buf = new StringBuilder();
		root.toSketch(buf,0);
		System.out.println(buf.toString());
	}
}
