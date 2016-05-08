package my.ast.box;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import my.ast.util.Const;
import my.ast.util.Element;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class Box {
	/**
	 * エレメントに対応するボックスのインスタンスを作成する。
	 * @param eu
	 * @param index
	 * @return
	 */
	public static Box make(ElementListUtil eu, int index) {
		ASTNode node = eu.element(index).getNode();
		Box box;
		if( node instanceof IfStatement ) {
			box = new IfBox(eu,index);
		}
		else if( node instanceof ForStatement ) {
			box = new ForBox(eu,index);
		}
		else if( node instanceof EnhancedForStatement ) {
			box = new EnhancedForBox(eu,index);
		}
		else if( node instanceof WhileStatement ) {
			box = new WhileBox(eu,index);
		}
		else if( node instanceof DoStatement ) {
			box = new DoBox(eu,index);
		}
		else if( node instanceof SwitchStatement ) {
			box = new SwitchBox(eu,index);
		}
		else if( node instanceof SwitchCase ) {
			box = new SwitchCaseBox(eu,index);
		}
		else if( node instanceof BreakStatement ) {
			box = new TerminalBox(eu,index);
		}
		else if( node instanceof TryStatement ) {
			box = new TryBox(eu,index);
		}
		else if( node instanceof CatchClause ) {
			box = new CatchClauseBox(eu,index);
		}
		else if( node instanceof InfixExpression ) {
			box = new InfixExpressionBox(eu,index);
		}
		else if( node instanceof ExpressionStatement ) {
			box = new ExpressionStatementBox(eu,index);
		}
		else if( node instanceof VariableDeclarationStatement ) {
			box = new VariableDeclarationStatementBox(eu,index);
		}
		else if( node instanceof VariableDeclarationFragment ) {
			box = new VariableDeclarationFragmentBox(eu,index);
		}
		else if( node instanceof SingleVariableDeclaration ) {
			box = new SingleVariableDeclarationBox(eu,index);
		}
		else if( node instanceof ReturnStatement ) {
			box = new TerminalBox(eu,index);
		}
		else if( node instanceof LineComment ) {
			box = new LineCommentBox(eu,index);
		}
		else if( node instanceof BlockComment ) {
			box = new BlockCommentBox(eu,index);
		}
		else if( node instanceof Javadoc ) {
			box = new JavadocBox(eu,index);
		}
		else if( node instanceof MethodDeclaration ) {
			box = new MethodDeclarationBox(eu,index);
		}
		else if( node instanceof FieldDeclaration ) {
			box = new FieldDeclarationBox(eu,index);
		}
		else if( node instanceof TypeDeclaration ) {
			box = new TypeDeclarationBox(eu,index);
		}
		else if( node instanceof ImportDeclaration ) {
			box = new ImportDeclarationBox(eu,index);
		}
		else if( node instanceof PackageDeclaration ) {
			box = new PackageDeclarationBox(eu,index);
		}
		else if( node instanceof Expression ) {
			box = new TerminalBox(eu,index);
		}
		else if( node instanceof VariableDeclaration ) {
			box = new TerminalBox(eu,index);
		}
		else {
			box = new Box(eu,index);
		}
		return box;
	}
	
	/** エレメント一覧操作ユーティリティ */
	protected ElementListUtil eu;
	
	/** 自分に対応するエレメント一覧上のインデックス */
	protected int index;
	
	/** エレメントの子エレメントの一覧 */
	protected List<Box> sons = new ArrayList<>();
	
	/**
	 * エレメントに対応するボックスを作成する。
	 * @param index
	 */
	public Box(ElementListUtil eu, int index) {
		this.eu = eu;
		this.index = index;
		loadSons();
	}

	/**
	 * ボックスに対応するエレメントのインデックスを取得する。
	 * @return
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * 子ボックスの一覧を取得する。
	 * @return
	 */
	public List<Box> getSons() {
		return sons;
	}
	
	/**
	 * 標準的な方法（ASTノード階層数が+1であるものを子エレメントとする方法）で子エレメントを設定する。
	 */
	private final void loadSons() {
		int[] indicies = eu.sons(index);
		for(int s=0; s<indicies.length; s++) {
			Box son = Box.make(eu, indicies[s]);
			sons.add(son);
		}
	}

	/**
	 * エレメント対応のASTノードを文字列化する。
	 * @return
	 */
	public String getLabel() {
		Element element = eu.element(index);
		ASTNode node = element.getNode();
		return node.toString();
	}
	
	/**
	 * エレメントに対応するASTノードを取得する。
	 * @return
	 */
	public ASTNode getNode() {
		Element element = eu.element(index);
		return element.getNode();
	}
	
	/**
	 * ボックスの内容を文字列化して、バッファに出力する。
	 * @param buf
	 */
	public void toString(StringBuilder buf, int level) {
		Util.repeat(buf,Const.IndentChar,level);
		buf.append("[Box at=").append(index);
		
		if( sons.size() <= 0 ) {
			buf.append(" ");
			eu.element(index).toString(buf,true);
			buf.append("]").append(Const.CRLF);
		}
		else {
			buf.append(" ");
			eu.element(index).toString(buf,false);
			buf.append(Const.CRLF);
			for(Box son: sons) {
				son.toString(buf,level + 1);
			}

			Util.repeat(buf,Const.IndentChar,level);
			buf.append("]").append(Const.CRLF);
		}
	}

	/**
	 * ボックスの内容を文字列化する。
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		toString(buf,0);
		return buf.toString();
	}
	
	/**
	 * ボックスの内容を作図ボックス構造化して、バッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf,Const.IndentChar,level);
		
		buf.append(getNode().getClass().getSimpleName()).append(Const.CRLF);
		
		for(Box son: sons) {
			son.toSketch(buf,level + 1);
		}
	}

	/**
	 * ボックスの内容を作図ボックス構造化して、バッファに出力する。
	 * @param buf
	 */
	public String toSketch(int level) {
		StringBuilder buf = new StringBuilder();
		toSketch(buf,level);
		return buf.toString();
	}
}
