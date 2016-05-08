package my.draw.core;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static final int BYTES_PER_CELL = 4;
	
	/**
	 * 指定の文字列データを指定の文字数（全角文字での文字数）もしくは改行文字で改行し、文字列配列として取得する。
	 * @param data 文字列データ
	 * @param maxPerLine １行当りの最大文字数（全角の文字数。半角の場合は、2文字で1と数える。）
	 * @return
	 */
	public static String[] lines(String data, int maxPerLine) {
		if( data == null ) {
			return new String[0];
		}

		List<String> lines = new ArrayList<String>();
		if( maxPerLine <= 0 ) {
			maxPerLine = 1;
		}
		int maxBytesPerLine = maxPerLine * BYTES_PER_CELL;
		
		int len = data.length();
		int chars = 0;
		StringBuilder buf = new StringBuilder();
		for(int i=0; i<len; i++) {
			char c = data.charAt(i);

			if( c == '\n' ) {
				lines.add(buf.toString());
				buf = new StringBuilder();
				chars = 0;
				continue;
			}
			else if( c == '\r' ) {
				if( i+1 < len && data.charAt(i+1) == '\n' ) {
					continue;
				}
				lines.add(buf.toString());
				buf = new StringBuilder();
				chars = 0;
				continue;
			}

			if( c < 0x80 ) {
				chars++;
			} else {
				chars += 2;
			}

			if( chars > maxBytesPerLine ) {
				lines.add(buf.toString());
				buf = new StringBuilder();
				buf.append(c);
				if( c < 0x80 ) {
					chars = 1;
				}
				else {
					chars = 2;
				}
			}
			else if( chars == maxBytesPerLine ) {
				buf.append(c);
				lines.add(buf.toString());
				buf = new StringBuilder();
				chars = 0;
			}
			else {
				buf.append(c);
			}
		}
		if( buf.length() > 0 ) {
			lines.add(buf.toString());
		}
		
		return lines.toArray(new String[0]);
	}
	
	/**
	 * 文字列長を半角を１文字として計算する。
	 * @param s
	 * @return
	 */
	public static int length(String s) {
		int len = 0;
		for(char c : s.toCharArray()) {
			if( c >= 0x80 ) {
				len += 2;
			}
			else {
				len++;
			}
		}
		return len;
	}
}
