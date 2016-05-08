package my.ast.box;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class SwitchBox extends Box {
	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public SwitchBox(ElementListUtil eu, int index) {
		super(eu, index);
	}
	
	/**
	 * 作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		boolean caseFound = false;
		for(Box son: sons) {
			ASTNode node = son.getNode();
			if( node instanceof Expression ) {
				String content = Util.oneline(son.toSketch(0).trim());
				Util.repeat(buf, Const.IndentChar,level);
				buf.append("switch: ").append(content).append(Const.CRLF);
			}
			else if( node instanceof SwitchCase ) {
				son.toSketch(buf,level);				
			}
			else  {
				son.toSketch(buf,level+1);
			}
		}
	}
}
