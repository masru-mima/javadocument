package my.ast.box;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class EnhancedForBox extends Box {
	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public EnhancedForBox(ElementListUtil eu, int index) {
		super(eu, index);
	}
	
	/**
	 * 作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		StringBuilder variable = new StringBuilder();
		StringBuilder expression = new StringBuilder();
		for(Box son: sons) {
			ASTNode node = son.getNode();
			if( node instanceof Block) {
				if( variable != null ) {
					Util.repeat(buf,Const.IndentChar,level);
					buf.append("for: ").append(variable).append(": ").append(expression).append(Const.CRLF);
					variable = null;
				}
				for(Box box: son.getSons()) {
					box.toSketch(buf,level+1);
				}
			}
			else if( variable != null && node instanceof VariableDeclaration ){
				String content = Util.oneline(son.toSketch(0).trim());
				if( variable.length() > 0 ) {
					variable.append(" ");
				}
				variable.append(content);
			}
			else if(  node instanceof Expression ){
				String content = Util.oneline(son.toSketch(0).trim());
				if( expression.length() > 0 ) {
					expression.append(" ");
				}
				expression.append(content);
			}
			else {
				Util.repeat(buf,Const.IndentChar,level+1);
				buf.append("UNKNOWN").append(Const.CRLF);
				son.toSketch(buf,level+2);
			}
		}
	}
}
