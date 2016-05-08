package my.ast.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Comment;

public class ElementListUtil {
	/** ASTノード操作ユーティリティ */
	private ASTNodeUtil au;
	
	/** エレメント一覧 */
	private List<Element> elements;
	
	/**
	 * ASTノード操作ユーティリティ及びエレメント一覧からインスタンスを作成する。
	 * @param au
	 * @param elements
	 */
	public ElementListUtil(ASTNodeUtil au, List<Element> elements) {
		this.au = au;
		this.elements = elements;
	}
	
	/**
	 * エレメント一覧の各エレメントにASTノード階層数を設定する。
	 */
	public void setASTNodeLevel() {
		for(int e=1; e<elements.size(); e++) {
			setASTNodeLevel(e);
		}
	}

	/**
	 * エレメント一覧を上位方向に検索して、自分の親を特定し、自分のASTノード階層数を設定する。
	 * @param myIndex
	 */
	private void setASTNodeLevel(int myIndex) {
		Element me = elements.get(myIndex);
		for(int e=myIndex-1; e>=0; e--) {
			Element element = elements.get(e);
			if( element.start() <= me.start() 
					&& me.end() <= element.end() ) {
				me.setLevel(element.getLevel()+1);
				break;
			}
		}
	}
	
	/**
	 * エレメント一覧にコメントノードをマージする。
	 * @param comments
	 */
	public void mergeComment(List<Comment> comments) {
		int e = 0;
		for(Comment comment: comments) {
			for( ; e<elements.size(); e++) {
				Element element = elements.get(e);
				if( au.end(comment) < element.start() ) {
					if( e > 0 && elements.get(e-1).getNode() != comment ) {
						elements.add(e, new Element(au,comment));
					}
					break;
				}
			}
			if( e>=elements.size() ) {
				elements.add(e,new Element(au,comment));
			}
		}
	}

	/**
	 * 指定位置のエレメントを取得する。
	 * @param index
	 * @return
	 */
	public Element element(int index) {
		return elements.get(index);
	}
	
	/**
	 * 指定のエレメントの子エレメントのインデックス一覧を作成する。
	 * @param parent
	 * @return
	 */
	public int[] sons(int parent) {
		return sons(parent,null);
	}
	
	/**
	 * 指定のエレメントの子エレメントのうち、指定の種別のノードであるもののインデックス一覧を作成する。
	 * @param parent
	 * @param clazz
	 * @return
	 */
	public int[] sons(int parent, Class clazz) {
		List<Integer> list = new ArrayList<>();
		int sonLevel = elements.get(parent).getLevel() + 1;
		for(int e=parent+1; e<elements.size(); e++) {
			Element element = elements.get(e);
			int level = element.getLevel();
			if( level < sonLevel ) {
				break;
			}
			else if( level == sonLevel ) {
				if( clazz == null || clazz.isInstance(element.getNode())  ) {
					list.add(e);
				}
			}
			else {
				continue;
			}
		}
		
		int[] sons = new int[list.size()];
		for(int i=0; i<sons.length; i++) {
			sons[i] = list.get(i);
		}
		
		return sons;
	}
}
