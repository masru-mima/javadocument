package my.ast.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SimpleVisitor  extends ASTVisitor {
	/** ターミナルノードの定義（構文解析を行わないノード） */
	private static Set<Class> terminals = new HashSet<>();
	static {
		//terminals.add(PackageDeclaration.class);
		//terminals.add(ImportDeclaration.class);
		//terminals.add(VariableDeclarationFragment.class);
	}
	
	/** ASTノード操作ユーティリティ */
	private ASTNodeUtil au;
	
	/** ノード一覧 */
	private List<Element> elements;

	/** 
	 * ノード一覧を指定してインスタンスを作成する。
	 * @param nodes
	 */
	public SimpleVisitor(ASTNodeUtil au, List<Element> elements) {
		this.au = au;
		this.elements = elements;
	}

	@Override
	public boolean preVisit2(ASTNode node) {
		elements.add(new Element(au,node));
		if( isTerminal(node)) {
			return false;
		}
		return true;
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
