package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllStatements {
	/** キーワード一覧 */
	private static Set<String> keywords = new HashSet<>();

	/** 処理結果一覧リスト */
	private List<String> list = new ArrayList<>();

	/**
	 * キーワード一覧を初期化する。
	 */
	static {
		keywords.add("word1");
		keywords.add("word2");
	}

	/**
	 * ワードがキーワードであるか否かを判定する。
	 * @param word ワード
	 * @return true: キーワードである、false:キーワードでない
	 * @exception IOException IO例外
	 * @exception Exception 想定外の例外
	 */
	public boolean isKeyword(String word) throws Exception {
		/** 処理モードフラグ */
		int flag = 1;

		try {
			/**
			 * flagとwordの値に応じてflagをリセットする。
			 */
			switch(flag) {
			case 1:
				/** word長が0より長ければflagを2にリセットする。*/
				if( word.length() > 0 ) {
					flag = 2;
				}
				else {
					flag = 1;
				}
				break;
			case 2:
			case 3:
				/** work長が1より長ければ、flagを2にリセットする。*/
				if( word.length() > 1 ) {
					flag = 2;
				}
				else {
					flag = 1;
				}
				break;
			default:
				//モードの変更はしない。
				break;
			}
		}
		catch(Exception e) {
			/** 例外発生時には、モードを1に強制設定する。*/
			flag = 1;			
		}
		finally {
			/** リセット後のflag値を標準出力に出力する。*/
			System.out.println("flag=" + flag);
		}
		
		/**
		 * キーワード一覧にワードが含まれているか否かで判定する。
		 */
		if( /** 含まれている場合 */ keywords.contains(word) ) {
			/** trueを返す */
			return true;
		}
		else {
			/** false を返す */
			return false;
		}
	}
	
	/**
	 * ワードの半角文字数を算出する。
	 * @param word ワード
	 * @return 半角換算での文字数
	 */
	public int length(String word) {
		/** 文字数の格納領域 */
		int len = 0;

		/**
		 * 各文字について、全角か半角かを判定して、半角換算文字数を加算する。
		 */
		for(char c: word.toCharArray()) {
			if( /** 文字が日本語である場合 */ c > 0x80 ) {
				//半角換算文字数に2を加算する。
				len += 2;
			}
			else {
				//半角換算文字数に1を加算する。
				len++;
			}
		}

		/**
		 * 各文字について、全角か半角かを判定して、半角換算文字数を加算する。(whileバージョン)
		 */
		int i = 0;
		while( i < word.length()) {
			/** 第i文字目の文字 c を取得する。 */
			char c = word.charAt(i);
			
			if( /** 文字が日本語である場合 */ c > 0x80 ) {
				//半角換算文字数に2を加算する。
				len += 2;
			}
			else {
				//半角換算文字数に1を加算する。
				len++;
			}
		}

		/**
		 * 各文字について、全角か半角かを判定して、半角換算文字数を加算する。(doバージョン)
		 */
		i = 0;
		do {
			/** 第i文字目の文字 c を取得する。 */
			char c = word.charAt(i);
			
			if( /** 文字が日本語である場合 */ c > 0x80 ) {
				//半角換算文字数に2を加算する。
				len += 2;
			}
			else {
				//半角換算文字数に1を加算する。
				len++;
			}
			
			i++;
		}
		while( i < word.length());
		
		/** 半角換算数を返す。 */
		return len;
	}
}
