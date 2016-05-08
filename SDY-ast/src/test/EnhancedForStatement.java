package test;

import java.util.ArrayList;
import java.util.List;

public class EnhancedForStatement {
	public void methodA() {
		List<Integer> list = new ArrayList<>();
		
		/**
		 * for全体のコメント
		 */
		for(Integer i: list) {
			/** ブロック全体のコメント */
			
			//行コメントはプリコメント
			i = i + 1;
		}
	}

}
