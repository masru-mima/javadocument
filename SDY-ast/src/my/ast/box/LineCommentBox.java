package my.ast.box;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class LineCommentBox extends Box {

	/**
	 * エレメントに対応するコメント・ボックスを作成する。
	 * @param index
	 */
	public LineCommentBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf, Const.IndentChar, level);
		String comment = eu.element(index).source();
		if( comment != null && comment.startsWith("//") ) {
			comment = comment.substring(2).trim();
		}
		comment = comment.replace("\r\n", " ").replace("\n", " ");
		buf.append(comment).append(Const.CRLF);
	}
}
