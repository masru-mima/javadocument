package my.ast.box;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class ExpressionStatementBox extends Box {

	/**
	 * エレメントに対応するインフィックス・ボックスを作成する。
	 * @param index
	 */
	public ExpressionStatementBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf, Const.IndentChar, level);
		String content = getNode().toString().trim();
		buf.append(content).append(Const.CRLF);
	}
}
