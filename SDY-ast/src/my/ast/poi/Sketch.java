package my.ast.poi;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import my.ast.box.Box;
import my.ast.box.IfBox;
import my.ast.util.Const;

public class Sketch {
	private static final Set<Class> terminals = new HashSet<>();
	
	static {
		terminals.add(PackageDeclaration.class);
		terminals.add(ImportDeclaration.class);
		terminals.add(Comment.class);
		terminals.add(Type.class);
		terminals.add(VariableDeclarationFragment.class);
		terminals.add(Expression.class);
	}
	
	/**
	 * 構文ボックス構造からスケッチを作成する。
	 * @param root
	 * @return
	 */
	public static String make(Box root) {
		StringBuilder buf = new StringBuilder();
		make(buf,0,root);
		return buf.toString();
	}
	
	private static boolean isTerminal(Class clazz) {
		if( clazz == null ) {
			return false;
		}

		if( terminals.contains(clazz) ) {
			return true;
		}
		
		return isTerminal(clazz.getSuperclass());
	}

	/**
	 * 指定された階層数で構文ボックス構造をバッファに出力する。
	 * @param buf
	 * @param level
	 * @param root
	 */
	private static void make(StringBuilder buf, int level, Box box) {
		tabs(buf,level);
		typeAndLabel(buf,box);
		buf.append(Const.CRLF);
		if( isTerminal(box.getNode().getClass()) ) {
			return;
		}
		
		for(Box son:  box.getSons()) {
			make(buf,level+1,son);
		}
	}
	
	/**
	 * 階層数に応じた数のインデント文字をバッファに出力する。
	 * @param buf
	 * @param level
	 */
	private static void tabs(StringBuilder buf, int level) {
		for(int i=0; i<level; i++) {
			buf.append(Const.IndentChar);
		}
	}
	
	private static void typeAndLabel(StringBuilder buf, Box box) {
		if( box instanceof IfBox ) {
			buf.append("IF: ");
			buf.append(label(box));
		}
		else {
			buf.append("BOX: ");
			buf.append(label(box));
		}
	}
	
	public static String label(Box box) {
		String label = box.getLabel();
		if( label != null ) {
			label = label.replace("\r\n", " ").replace("\n", " ");
		}
		return label;
	}
}
