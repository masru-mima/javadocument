package my.ast.core2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import my.ast.util.Const;
import my.ast.util.Util;

/**
 * エレメント一覧
 */
public class Elements {
	/** 入力ソースコード */
	private String source;

	/** 解析結果の構文構造 */
	private CompilationUnit unit;
	
	/** エレメント一覧 */
	private List<Element> list = new ArrayList<>();
	
	/**
	 * 入力ソースコードを解析してインスタンスを作成する。この時、ソースコードの解析も行う。
	 * @param source
	 */
	public Elements(String source) {
		this.source = source;
		
		/** ソースコードを解析する。*/
		parse();
		
		/** 階層数を設定する。*/
		resetNodeLevels();
	}
	
	/**
	 * 指定エレメントの子エレメントの一覧を取得する。指定エレメントがnullの場合は、階層数が 0 のエレメントの一覧を取得する。
	 * @param parent
	 * @return
	 */
	public List<Element> sons(Element parent) {
		List<Element> sons = new ArrayList<>();

		/** 検索開始位置及び階層数を設定する。*/
		int startIndex;
		int targetLevel;
		if( parent != null ) {
			startIndex = parent.getIndex()+1;
			targetLevel = parent.getNodeLevel() + 1;
		}
		else {
			startIndex = 0;
			targetLevel = 0;
		}

		/**
		 * 検索開始位置から一覧末尾に向けて、検索対象階層数より小さい階層数を検出するまで繰り返す。
		 */
		for(int e=startIndex; e<list.size(); e++) {
			/** チェック対象のエレメントを一覧から取得する。*/
			Element element = list.get(e);
			
			/** エレメントの階層数を取得する。*/
			int nodeLevel = element.getNodeLevel();
			
			if( nodeLevel < targetLevel ) {
				/** 検索対象階層数より小さい階層数なので、検索を終了する。*/
				break;
			}
			else if( nodeLevel == targetLevel ) {
				/** 丁度、検索対象階層数であるので、子一覧に追加する。*/
				sons.add(element);
			}
		}

		/** 検索結果の一覧を返す。*/
		return sons;
	}

	/**
	 * 入力ソースコードを解析してエレメント一覧を作成する。
	 */
	private void parse() {
		/** パーサを作成する。*/
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		/** パーサにソースコードの文字配列を設定する。*/
		parser.setSource(source.toCharArray());
		
		/** ソースコードを解析し、コンピレーションユニットを作成する。 */
		unit = (CompilationUnit)parser.createAST(null);
		
		//ビジターを作成しコンピレーションユニットに適用し、エレメントリストを作成する。
		ASTVisitor visitor = new ElementVisitor(this);
		unit.accept(visitor);
	}

	/**
	 * 新規に空のエレメントを作成しエレメント一覧に登録する。
	 * @return
	 */
	Element newElement() {
		Element element = new Element();
		list.add(element);
		element.setElements(this);
		element.setIndex(list.size()-1);
		return element;
	}
	
	/**
	 * 新規にASTNode対応のエレメントを作成しエレメント一覧に登録する。
	 * @return
	 */
	Element newElement(ASTNode node) {
		Element element = newElement();
		element.setNode(node);
		return element;
	}
	

	/**
	 * エレメント一覧内の全てのエレメントにノード階層数を再設定する。
	 */
	private void resetNodeLevels() {
		/** エレメントが存在しない場合は何もしない。 */
		if( list.size() <= 0 ) {
			return;
		}
		
		/** 先頭ノードの階層数に階層数0を設定する。 */
		list.get(0).setNodeLevel(0);

		/**
		 * ２番目以降のエレメントの階層数を設定する。なお、エレメントの階層数は、対応ノードの開始位置、終了位置の包含関係
		 * によって決定する。
		 */
		for(int e=1; e<list.size(); e++) {
			Element me = list.get(e);
			ASTNode meNode = me.getNode();
			boolean foundParent = false;
			for(int p=e-1; p>=0; p-- ) {
				Element parent = list.get(p);
				ASTNode parentNode = parent.getNode();
				if( parent.getStartPosition() <= me.getStartPosition()
				 && parent.getLastPosition() >= me.getLastPosition() ) {
					me.setNodeLevel(parent.getNodeLevel()+1);
					foundParent = true;
					break;
				}
			}
			if( !foundParent ) {
				throw new IllegalStateException("parent of the element at " + e + " was not found.");
			}
		}
	}
	
	/**
	 * エレメント一覧を文字列化してバッファに出力する。
	 * @param buf
	 */
	public void toString(StringBuilder buf) {
		buf.append("*** list of elements").append(Const.CRLF);
		for(Element element: list) {
			buf.append(Util.repeat(Const.IndentChar, element.getNodeLevel()));
			element.toString(buf);
			buf.append(Const.CRLF);
		}
		buf.append("*** end of elements").append(Const.CRLF);
	}
	
	/**
	 * エレメント一覧を文字列化する。
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		toString(buf);
		return buf.toString();
	}
}
