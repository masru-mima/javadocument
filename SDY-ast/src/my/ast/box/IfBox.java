package my.ast.box;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import my.ast.util.Const;
import my.ast.util.ElementListUtil;
import my.ast.util.Util;

public class IfBox extends Box {
	/**
	 * （条件部、処理部）の組を表すクラス
	 */
	public static class Tupple {
		/** 条件部に対応するエレメント一覧のインデックス （-1の場合はELSE部であることを示す。）*/
		private List<Box> conditions = new ArrayList<>();
		
		/** 処理部に対応するボックス */
		private Box box;

		/**
		 * 条件を追加する。
		 * @param box
		 */
		public void addCondition(Box box) {
			conditions.add(box);
		}
		
		/**
		 * IfBoxの（条件部、処理部）タップルを文字列化してバッファに出力する。
		 * @param buf
		 */
		public void toString(StringBuilder buf, int level) {
			if( conditions.size() == 0 ) {
				Util.repeat(buf, Const.IndentChar, level);
				buf.append("condition: ");
				buf.append("null").append(Const.CRLF);
			}
			else {
				for(Box condition: conditions) {
					StringBuilder work = new StringBuilder();
					condition.toString(work, level);
					String work2 = work.toString().trim();

					Util.repeat(buf, Const.IndentChar, level);
					buf.append("condition: ");
					buf.append(work2).append(Const.CRLF);
				}
			}
			Util.repeat(buf, Const.IndentChar, level);
			buf.append("-> ");
			{
				StringBuilder work = new StringBuilder();
				box.toString(work, level);
				String work2 = work.toString().trim();
				buf.append(work2).append(Const.CRLF);
			}
		}
		
		/**
		 * IfBoxの（条件部、処理部）タップルを文字列化する。
		 */
		public String toString() {
			StringBuilder buf = new StringBuilder();
			toString(buf,0);
			return buf.toString();
		}
		
		/**
		 * IfBoxの（条件部、処理部）タップルを作図ボックス構造化してバッファに出力する。
		 * @param buf
		 */
		public void toSketch(StringBuilder buf, int level, boolean first) {
			if( conditions.size() == 0 ) {
				Util.repeat(buf, Const.IndentChar, level);
				buf.append("ELSE: ").append(Const.CRLF);
			}
			else {
				StringBuilder cond = new StringBuilder();
				for(Box condition: conditions) {
					if( cond.length() > 0 ) {
						cond.append(" ");
					}
					String content = Util.oneline(condition.toSketch(0).trim());
					cond.append(content);
				}
				Util.repeat(buf, Const.IndentChar, level);
				if( first ) {
					buf.append("IF: ").append(cond).append(Const.CRLF);
				}
				else {
					buf.append("ELSE IF: ").append(cond).append(Const.CRLF);
				}
			}
			if( box.getNode() instanceof Block ) {
				for(Box son: box.getSons()) {
					son.toSketch(buf,level+1);
				}
			}
			else {
				box.toSketch(buf, level+1);
			}
		}
	}
	
	/** （条件部、処理部）の一覧 */
	private List<Tupple> contents = new ArrayList<>();
	
	/** 先行するIFボックス */
	private IfBox parent;

	/**
	 * エレメント一覧ユーティリティ及びエレメントを指定してインスタンスを作成する。
	 * @param eu
	 * @param index
	 */
	public IfBox(ElementListUtil eu, int index) {
		super(eu, index);
		flatten();
	}
	
	/**
	 * 先行するIFボックスを設定する。
	 * @param ifbox
	 */
	private void setParent(IfBox ifbox) {
		this.parent = ifbox;
	}

	/**
	 * else if ... else ... の階層をフラットにする。
	 * @param eu
	 */
	private final void flatten() {
		int i = 0;
		Tupple tupple = new Tupple();
		for( ; i<sons.size(); i++) {
			Box son = sons.get(i);
			if( son.getNode() instanceof Statement) {
				tupple.box = son;
				contents.add(tupple);
				break;
			}
			else {
				tupple.addCondition( sons.get(i) );
			}
		}
		
		i++;
		if( sons.size() > i ) {
			Box son = sons.get(i);
			if( son instanceof IfBox ) {
				IfBox ifbox = (IfBox)son;
				contents.addAll(ifbox.contents);
				ifbox.setParent(this);
			}
			else {
				tupple = new Tupple();
				tupple.box = son;
				contents.add(tupple);
			}
		}
	}

	/**
	 * IFボックスの内容を文字列化して、バッファに出力する。
	 * @param buf
	 */
	public void toString(StringBuilder buf, int level) {
		if( parent != null ) {
			super.toString(buf,level);
			return;
		}
		
		Util.repeat(buf, Const.IndentChar, level);
		buf.append("[IfBox at=").append(index).append(Const.CRLF);

		for(Tupple tupple: contents) {
			tupple.toString(buf,level+1);
		}

		Util.repeat(buf, Const.IndentChar, level);
		buf.append("]").append(Const.CRLF);
	}
	
	/**
	 * IfBoxの（条件部、処理部）タップルを作図ボックス構造化してバッファに出力する。
	 * @param buf
	 */
	public void toSketch(StringBuilder buf, int level) {
		boolean first = true;
		for(Tupple tupple: contents) {
			tupple.toSketch(buf,level,first);
			first = false;
		}
	}

}
