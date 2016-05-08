package my.ast.box;

import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class FieldDeclarationBox extends Box {

	/**
	 * エレメントに対応するコメント・ボックスを作成する。
	 * @param index
	 */
	public FieldDeclarationBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		StringBuilder type = new StringBuilder();
		boolean first = true;
		boolean foundType = false;
		for(Box box: getSons()) {
			if( first && box.getNode() instanceof Comment ) {
				String comment = Util.oneline(box.toSketch(0).trim());
				Util.repeat(buf, Const.IndentChar, level);
				buf.append(comment).append(Const.CRLF);
				continue;
			}
			if( !foundType ) {
				String content = Util.oneline(box.getNode().toString().trim());
				if( type.length() > 0 ) {
					type.append(" ");
				}
				type.append(content);
				if( box.getNode() instanceof Type ) {
					foundType = true;
				}
				continue;
			}

			if( box.getNode() instanceof VariableDeclarationFragment ) {
				Util.repeat(buf, Const.IndentChar, level);
				buf.append(type.toString()).append(" ");
				box.toSketch(buf,0);
			}
		}
	}
}
