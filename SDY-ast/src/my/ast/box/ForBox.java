package my.ast.box;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class ForBox extends Box {
	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public ForBox(ElementListUtil eu, int index) {
		super(eu, index);
	}
	
	/**
	 * 作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		StringBuilder condition = new StringBuilder();
		for(Box son: sons) {
			ASTNode node = son.getNode();
			if( node instanceof Expression && condition != null ) {
				String content = Util.oneline(son.toSketch(0).trim());
				if( condition.length() > 0 ) {
					condition.append("; ");
				}
				condition.append(content);				
			}
			else if( node instanceof Block) {
				if( condition != null ) {
					Util.repeat(buf, Const.IndentChar,level);
					buf.append("for: ").append(condition).append(Const.CRLF);
					condition = null;
				}
				for(Box box: son.getSons()) {
					box.toSketch(buf,level+1);
				}
			}
			else {
				Util.repeat(buf, Const.IndentChar,level+1);
				buf.append("UNKNOWN").append(Const.CRLF);
				son.toSketch(buf,level+2);
			}
		}
	}
}
