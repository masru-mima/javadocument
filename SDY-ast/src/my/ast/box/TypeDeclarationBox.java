package my.ast.box;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Name;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class TypeDeclarationBox extends Box {

	/**
	 * エレメントに対応するインフィックス・ボックスを作成する。
	 * @param index
	 */
	public TypeDeclarationBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		List<Box> sons = getSons();

		String comment = "";
		StringBuilder typeAndName = new StringBuilder();
		Box body = null;
		
		boolean foundName = false;
		for(int i=0; i<sons.size(); i++) {
			Box son = sons.get(i);
			if( i == 0 && son.getNode() instanceof Comment ) {
				comment = Util.oneline(son.toSketch(0).trim());
				continue;
			}

			if( !foundName ) {
				if(  son.getNode() instanceof Name ) {
					foundName = true;
					if( typeAndName.length() > 0 ) {
						typeAndName.append(" ");
					}
					typeAndName.append(son.getNode().toString());
					Util.repeat(buf, Const.IndentChar, level);
					buf.append("タイプ定義 ")
					      .append(comment)
					      .append(" (").append(typeAndName.toString()).append(")")
					      .append(Const.CRLF)
					      ;	
				}
				else {
					String content = son.getNode().toString().trim();
					if( typeAndName.length() > 0 ) {
						typeAndName.append(" ");
					}
					typeAndName.append(content);
				}
				continue;
			}

			son.toSketch(buf,level+1);
		}
	}
}
