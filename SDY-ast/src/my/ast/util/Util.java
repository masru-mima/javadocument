package my.ast.util;

import java.util.List;

public class Util {
	
	public static String classHierarchy(Class clazz) {
		StringBuilder buf = new StringBuilder();
		while(true) {
			if( buf.length() > 0 ) {
				buf.append(">");
			}
			buf.append(clazz.getSimpleName());
			clazz = clazz.getSuperclass();
			if( clazz == Object.class ) {
				break;
			}
		}
		return buf.toString();
	}
	
	public static void repeat(StringBuilder buf, char c, int n) {
		for(int i=0; i<n; i++) {
			buf.append(c);
		}
	}

	public static String repeat(char c, int n) {
		StringBuilder buf = new StringBuilder();
		repeat(buf,c,n);
		return buf.toString();
	}
	
	public static String join(String[] lines, String sep) {
		StringBuilder buf = new StringBuilder();
		if( lines != null ) for(String line: lines) {
			if( buf.length() > 0 ) {
				buf.append(sep);
			}
			buf.append(line);
		}
		return buf.toString();
	}
	
	public static String join(List<String> lines, String sep) {
		return join(lines.toArray(new String[0]),sep);
	}

	public static String oneline(String line) {
		if( line == null ) {
			return null;
		}
		return line.replace("\r", "\\r").replace("\n","\\n");
	}
}
