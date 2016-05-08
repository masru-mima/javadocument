package my.ast.util;

import org.eclipse.jdt.core.dom.ASTNode;

public class Element {
	/** ASTノード操作ユーティリティ */
	private ASTNodeUtil au;
	
	/** ASTノード */
	private ASTNode node;

	/** ASTノード階層数 */
	private int level;
	
	/**
	 * ノードを指定してインスタンスを作成する。
	 * @param node
	 */
	public Element(ASTNodeUtil au, ASTNode node) {
		this.au = au;
		this.node = node;
	}
	
	/**
	 * ASTノード階層数を設定する。
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * ASTノード階層数を取得する。
	 * @return
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * ASTノードを取得する。
	 * @return
	 */
	public ASTNode getNode() {
		return node;
	}
	
	/**
	 * ノードの最初の文字位置を取得する。
	 * @return
	 */
	public int start() {
		return au.start(node);
	}
	
	/**
	 * ノードの最後の文字位値を取得する。
	 * @return
	 */
	public int end() {
		return au.end(node);
	}
	
	/**
	 * ノード対応のソースを取得する。
	 * @return
	 */
	public String source() {
		return au.source(node);
	}
	
	/**
	 * エレメント情報を文字列化してバッファに出力する。
	 * @param buf
	 */
	public void toString(StringBuilder buf, boolean withSource) {
		buf.append("[element level=").append(level).append(" ");
		au.nodeInfo(buf,node,withSource);
		buf.append("]");
	}

	/**
	 * エレメント情報を文字列化して取得する。
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		toString(buf,true);
		return buf.toString();
	}
	
}
