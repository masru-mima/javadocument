package test;

public class ForStatement {
	public void methodA() {
		boolean flag = true;
		
		/**
		 * for全体のコメント
		 */
		for(int i=0; i<10; i++ ) {
			/** ブロック全体のコメント */
			
			//行コメントはプリコメント
			flag = false;
		}
	}

}
