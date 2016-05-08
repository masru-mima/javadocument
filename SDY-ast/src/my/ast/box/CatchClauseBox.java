package my.ast.box;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class CatchClauseBox extends Box {
	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public CatchClauseBox(ElementListUtil eu, int index) {
		super(eu, index);
	}
	
	/**
	 * 作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		for(Box son: sons) {
			ASTNode node = son.getNode();
			if( node instanceof VariableDeclaration ) {
				String content = Util.oneline(son.toSketch(0).trim());
				Util.repeat(buf, Const.IndentChar,level);
				buf.append("catch: ").append(content).append(Const.CRLF);				
			}
			else if( node instanceof Block) {
				for(Box box: son.getSons()) {
					box.toSketch(buf,level+1);
				}
			}
			else {
				son.toSketch(buf,level);
			}
		}
	}

}
