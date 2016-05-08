package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TryStatement {
	public void methodA() {
		/** try文の全体コメント */
		try(InputStream ins=new FileInputStream("in.txt");
			 OutputStream os=new FileOutputStream("out.txt");
			) {
			//try blockのコメント
			int a = 1;
		}
		catch(Exception e) {
			//キャッチコメント
			e.printStackTrace();
		}
		finally {
			//ファイナリーコメント
			System.out.println("finally block");
		}
	}
}
