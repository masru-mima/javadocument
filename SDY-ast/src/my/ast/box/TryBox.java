package my.ast.box;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class TryBox extends Box {
	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public TryBox(ElementListUtil eu, int index) {
		super(eu, index);
	}
	
	/**
	 * TryBoxを作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf, Const.IndentChar,level);
		buf.append("try:").append(Const.CRLF);
		
		boolean tryBody = true;
		for(Box son: sons) {
			ASTNode node = son.getNode();
			if( node instanceof VariableDeclarationExpression ) {
				String content = Util.oneline(son.toSketch(0).trim());
				Util.repeat(buf, Const.IndentChar,level);
				buf.append("res:").append(content).append(Const.CRLF);				
			}
			else if( node instanceof Block) {
				if( tryBody ) {
					tryBody = false;
				}
				else {
					Util.repeat(buf, Const.IndentChar,level);
					buf.append("finally:").append(Const.CRLF);				
				}
				for(Box box: son.getSons()) {
					box.toSketch(buf,level+1);
				}
			}
			else if( node instanceof CatchClause ) {
				son.toSketch(buf,level);
			}
		}
	}

}
