package my.ast.box;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class SwitchCaseBox extends Box {
	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public SwitchCaseBox(ElementListUtil eu, int index) {
		super(eu, index);
	}
	
	/**
	 * 作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		StringBuilder expression = new StringBuilder();
		for(Box son: sons) {
			if( son.getNode() instanceof Expression ) {
				String content = Util.oneline(son.toSketch(0).trim());
				if( expression.length() > 0 ) {
					expression.append(" ");
				}
				expression.append(content);
			}
			else {
				Util.repeat(buf, Const.IndentChar,level+1);
				buf.append("UNKNONW").append(Const.CRLF);
				son.toSketch(buf,level+2);
			}
		}
		Util.repeat(buf, Const.IndentChar,level);
		if( expression.length() <= 0 ) {
			buf.append("default: ").append(Const.CRLF);				
		}
		else {
			buf.append("case: ").append(expression).append(Const.CRLF);
		}
	}
}
