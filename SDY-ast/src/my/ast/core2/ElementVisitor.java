package my.ast.core2;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class ElementVisitor  extends ASTVisitor {
	/** ターミナルノードの定義（構文解析を行わないノード） */
	private static Set<Class> terminals = new HashSet<>();
	
	/** ターミナルノードを設定する。 */
	static {
		//terminals.add(PackageDeclaration.class);
		//terminals.add(ImportDeclaration.class);
		//terminals.add(VariableDeclarationFragment.class);
	}

	/** エレメント一覧 */
	private Elements elements;

	/** 
	 * エレメント一覧を指定してインスタンスを作成する。
	 * @param elements
	 */
	public ElementVisitor(Elements elements) {
		this.elements = elements;
	}

	@Override
	public boolean preVisit2(ASTNode node) {
		Element element = elements.newElement(node);
		return  ! isTerminal(node);
	}
	
	/**
	 * ASTノードを終端扱いするかどうかを判定する。
	 * @param node
	 * @return
	 */
	public boolean isTerminal(ASTNode node) {
		if( terminals.contains(node.getClass())) {
			return true;
		}
		return false;
	}
}
