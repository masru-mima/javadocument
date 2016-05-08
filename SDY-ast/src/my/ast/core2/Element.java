package my.ast.core2;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import my.ast.util.Util;

public class Element  {
	/** エレメント一覧 */
	private Elements elements;
	
	/** エレメント一覧インデックス */
	private int index;

	/** 構文ノード */
	private ASTNode node;
	
	/** nodeの階層数 */
	private int nodeLevel;
	
	/** node対応のコメントノード */
	private ASTNode comment;

	/**
	 * エレメント一覧を指定してインスタンスを作成する。
	 * @param elements
	 */
	Element() {
	}

	public Elements getElements() {
		return elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ASTNode getNode() {
		return node;
	}

	public void setNode(ASTNode node) {
		this.node = node;
	}

	public int getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(int nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public ASTNode getComment() {
		return comment;
	}

	public void setComment(ASTNode comment) {
		this.comment = comment;
	}

	/**
	 * ノードの先頭文字の位置を返す。
	 * @return
	 */
	public int getStartPosition() {
		if( node == null ) {
			return -1;
		}
		else {
			return node.getStartPosition();
		}
	}


	/**
	 * ノードの最終文字の位置を返す。
	 * @return
	 */
	public int getLastPosition() {
		if( node == null ) {
			return -1;
		}
		else {
			return node.getStartPosition() + node.getLength() - 1;
		}
	}

	/**
	 * エレメントを文字列化してバッファに出力する。
	 * @param buf
	 */
	public void toString(StringBuilder buf) {
		buf.append("[index=").append(index)
		     .append(" level=").append(nodeLevel)
		     .append(" range=").append(getStartPosition()).append("-").append(getLastPosition())
		     ;
		if( node == null ) {
			buf.append(" null");
		}
		else {
			buf.append(" ").append(Util.classHierarchy(node.getClass()));
		}
		
		if( node != null ) {
			buf.append(" code=").append(Util.oneline(node.toString()));
		}
	}
	
	/**
	 * エレメントを文字列化する。
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		toString(buf);
		return buf.toString();
	}

}
