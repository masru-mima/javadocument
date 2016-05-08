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
import my.ast.core2.Elements;
import my.ast.util.ASTNodeUtil;
import my.ast.util.Element;
import my.ast.util.ElementListUtil;
import my.ast.util.SimpleVisitor;

public class ElementMain {
	public static void main(String[] args) throws Exception {	
		if( args.length <= 0 ) {
			args = new String[]{
					"src/my/ast/core2/Elements.java", "UTF-8",
					//"src/test/AllStatements.java", "UTF-8",
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
		
		ElementMain m = new ElementMain(src,enc);
		m.parse();
	}
	
	private File src;
	private String enc;
	
	public ElementMain(File src, String enc) {
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
		
		/** ソースコードを解析してエレメント一覧を作成する。 */
		Elements elements = new Elements(source);
		
		System.out.println(elements);
	}
}
