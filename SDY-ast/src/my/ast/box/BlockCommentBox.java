package my.ast.box;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class BlockCommentBox extends Box {

	/**
	 * エレメントに対応するコメント・ボックスを作成する。
	 * @param index
	 */
	public BlockCommentBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf, Const.IndentChar, level);
		String comment = eu.element(index).source();
		if( comment != null ) {
			if( comment.startsWith("/*") ) {
				comment = comment.substring("/*".length()).trim();
			}
			if( comment.endsWith("*/") ) {
				comment = comment.substring(0,comment.length()-"*/".length()).trim();
			}
		}
		comment = Util.oneline(comment);
		buf.append(comment).append(Const.CRLF);
	}
}
