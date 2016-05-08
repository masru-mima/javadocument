package my.ast.box;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class SingleVariableDeclarationBox extends Box {

	/**
	 * エレメントに対応するコメント・ボックスを作成する。
	 * @param index
	 */
	public SingleVariableDeclarationBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf, Const.IndentChar, level);
		buf.append(getNode().toString()).append(Const.CRLF);
	}
}
