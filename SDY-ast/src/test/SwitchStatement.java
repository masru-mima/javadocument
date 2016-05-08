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
public class SwitchStatement {
	/**
	 * メソッドコメント
	 */
	public static void methodA() {
		/** 処理フラッグの初期設定 */
		int flag = 1;
		
		/**
		 * 判定処理全体のコメント
		 */
		switch( flag  ) {
		case 1:
			//case-1
			flag = 1;
			break;
		case 2:
			//case-2
			flag = 2;
			break;
		case 3:
			//case-3
			flag = 3;
		default:
			//case-default
			flag = 4;
			break;
		}
	}
}
