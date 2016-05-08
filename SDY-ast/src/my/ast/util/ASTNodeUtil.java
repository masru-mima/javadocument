package my.ast.util;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTNodeUtil {
	/** コンピレーションユニット */
	private CompilationUnit unit;
	
	/** ソースコード */
	private String source;
	
	/**
	 * コンピレーションユニット及びソースコードを指定してインスタンスを作成する。
	 * @param unit
	 * @param source
	 */
	public ASTNodeUtil(CompilationUnit unit, String source) {
		this.unit = unit;
		this.source = source;
	}
	
	/**
	 * ノードの最初の文字位置を取得する。
	 * @param node
	 * @return
	 */
	public int start(ASTNode node) {
		return node.getStartPosition();
	}
	
	/**
	 * ノードの最後の文字位置を取得する。
	 * @param node
	 * @return
	 */
	public int end(ASTNode node) {
		return node.getStartPosition() + node.getLength() - 1;
	}
	
	/**
	 * ノードの文字数を取得する。
	 * @param node
	 * @return
	 */
	public int length(ASTNode node) {
		return node.getLength();
	}
	
	/**
	 * ノードの最初の文字が存在する行番号を取得する。
	 * @param node
	 * @return
	 */
	public int startLine(ASTNode node) {
		return unit.getLineNumber(start(node));
	}
	
	/**
	 * ノードの最後の文字が存在する行番号を取得する。
	 * @param node
	 * @return
	 */
	public int endLine(ASTNode node) {
		return unit.getLineNumber(end(node));
	}

	/**
	 * ノードに対応するソースコードを取得する。
	 * @param node
	 * @return
	 */
	public String source(ASTNode node) {
		return source.substring(start(node), end(node)+1);
	}
	
	/**
	 * ノード情報を文字列化してバッファに出力する。
	 * @param buf
	 * @param node
	 * @param withSource
	 */
	public void nodeInfo(StringBuilder buf, ASTNode node, boolean withSource) {
		buf.append(node.getClass().getSimpleName());
		extendsInfo(buf,node.getClass().getSuperclass());
		 buf.append(" pos=").append(start(node)).append("-").append(end(node))
		     .append(" line=").append(startLine(node)).append("-").append(endLine(node))
		     ;
		if( withSource ) {
			String segment =  source.substring(start(node),end(node)+1);
			segment = segment.replace("\r\n", " ").replace("\n", " ");
			buf.append(" source=").append(segment);
		}
	}
	
	/**
	 * スーパークラスの情報をバッファに出力する。
	 * @param buf
	 * @param clazz
	 */
	private void extendsInfo(StringBuilder buf, Class clazz) {
		if( clazz == ASTNode.class || clazz == java.lang.Object.class ) {
			return;
		}
		buf.append(">").append(clazz.getSimpleName());
		extendsInfo(buf,clazz.getSuperclass());
	}
	
	/**
	 * ノード情報を文字列化する。
	 * @param node
	 * @param withSource
	 * @return
	 */
	public String nodeInfo(ASTNode node, boolean withSource) {
		StringBuilder buf = new StringBuilder();
		nodeInfo(buf,node,withSource);
		return buf.toString();
	}
}
