package my.ast.box;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Javadoc;
import my.ast.util.Util;

public class JavadocBox extends Box {

	/**
	 * エレメントに対応するコメント・ボックスを作成する。
	 * @param index
	 */
	public JavadocBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		Util.repeat(buf, Const.IndentChar, level);
		String comment = getNode().toString();
		if( comment != null ) {
			Javadoc javadoc = new Javadoc(comment);
			comment = javadoc.getComments();
			comment = comment.trim();
		}
		comment = Util.oneline(comment);
		buf.append(comment).append(Const.CRLF);
	}
}
