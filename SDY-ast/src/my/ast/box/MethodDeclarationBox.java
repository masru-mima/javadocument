package my.ast.box;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class MethodDeclarationBox extends Box {

	/**
	 * エレメントに対応するインフィックス・ボックスを作成する。
	 * @param index
	 */
	public MethodDeclarationBox(ElementListUtil eu, int index) {
		super(eu,index);
	}
	
	@Override
	public void toSketch(StringBuilder buf, int level) {
		List<Box> sons = getSons();

		String comment = "";
		StringBuilder typeAndName = new StringBuilder();
		StringBuilder args = new StringBuilder();
		Box body = null;
		
		boolean first = true;
		for(Box son: sons) {
			ASTNode node = son.getNode();
			if( first && node instanceof Comment ) {
				first = false;
				comment = son.toSketch(0).trim();
				continue;
			}
			else if( node instanceof Block ) {
				body = son;
				break;
			}
			else if( node instanceof Modifier || node instanceof Type || node instanceof Name ) {
				if( typeAndName.length() > 0 ) {
					typeAndName.append(" ");
				}
				typeAndName.append(son.getNode().toString());
			}
			else if( node instanceof VariableDeclaration ) {
				if( args.length() > 0 ) {
					args.append(",");
				}
				String content = Util.oneline(son.toSketch(0).trim());
				args.append(content);
			}
		}
			
		Util.repeat(buf, Const.IndentChar, level);
		buf.append("FLOWCHART: ").append(comment)
		     .append("(").append(typeAndName)
		     ;
		if( args.length() > 0 ) {
			buf.append("(").append(args.toString()).append(")");
		}
		buf.append(")")
		     .append(Const.CRLF);
		for(Box son: body.getSons()) {
			son.toSketch(buf,level+1);
		}
	}
}
