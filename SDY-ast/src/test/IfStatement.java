/**
 * パッケージコメント
 */
package test;

import java.util.ArrayList;
import java.util.List;

/**
 * クラスコメント
 * @author mima
 */
public class IfStatement {
	/** 出現整数の一覧 */
	private List<Integer> ilist = new ArrayList<>();

	/**
	 * メソッドコメント
	 * @param mode モードの指定
	 * @param name モードの名前
	 */
	public static void blockIf(int mode, String name) {
		/** 処理フラッグの初期設定 */
		int flag = 1, flag2 = 2;
		
		/** 計算結果格納領域  */
		int val;
		
		/**
		 * 判定処理全体のコメント
		 */
		if( flag == 1 ) {
			/**
			 * ブロックの説明コメント１
			 */
			val = 10;
		}
		else if( flag == 2 ) {
			//ブロックの説明コメント２
			val = 20;
		}
		else {
			val = 30;
		}
	}
	//クラス内最終コメント
}
//クラス外ソースの最終コメント
