package my.ast.box;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;

public class InfixExpressionBox extends Box {

	/**
	 * エレメントに対応するインフィックス・ボックスを作成する。
	 * @param index
	 */
	public InfixExpressionBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		buf.append(getNode().toString()).append(Const.CRLF);
	}
}
