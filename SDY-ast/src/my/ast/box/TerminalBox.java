package my.ast.box;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class TerminalBox extends Box {

	/**
	 * エレメントに対応するコメント・ボックスを作成する。
	 * @param index
	 */
	public TerminalBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		String content = Util.oneline(eu.element(index	).source().trim());
		Util.repeat(buf, Const.IndentChar, level);
		buf.append(content).append(Const.CRLF);
	}
}
