package test;

public class DoStatement {
	public void methodA() {
		boolean flag = true;
		
		/**
		 * do全体のコメント
		 */
		do {
			/** ブロック全体のコメント */
			
			//行コメントはプリコメント
			flag = false;
		}
		while( flag == true );
	}

}
