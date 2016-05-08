package my.ast.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Javadoc {
	public static class Tupple {
		/** 種別 (@param, @return, @exception) */
		private String type;
		
		/** 引数名１*/
		private String name;
		
		/** 説明 */
		private String description;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		public void toString(StringBuilder buf) {
			buf.append(type).append(" ").append(name).append(" ").append(description);
		}
		
		public String toString() {
			StringBuilder buf = new StringBuilder();
			toString(buf);
			return buf.toString();
		}
	}

	private List<String> comments = new ArrayList<>();
	private List<Tupple> params = new ArrayList<>();
	
	public Javadoc(String comment)  {
		Tupple tupple = null;
		try( BufferedReader rdr = new BufferedReader(new StringReader(comment));) {
			boolean first = true;
			String line;
			while( (line=rdr.readLine()) != null ) {
				line = line.trim();
				if( first ) {
					if( line.startsWith("/**") ) {
						line = line.substring("/**".length()).trim();
					}
					first = false;
				}
				else if( line.startsWith("*")  && !line.startsWith("*/") ) {
					line = line.substring("*".length()).trim();
				}
				if( line.endsWith("*/") ) {
					line = line.substring(0,line.length()-"*/".length()).trim();
				}
				if( line.startsWith("@") ) {
					tupple = new Tupple();
					int p1 = line.indexOf(" ");
					int p2 = -1;
					if( p1 >= 0 ) {
						tupple.setType(line.substring(0,p1).trim());
						p2 = line.indexOf(" ",p1+1);
						if( p2 >= 0 ) {
							tupple.setName(line.substring(p1+1,p2).trim());
							tupple.setDescription(line.substring(p2+1).trim());
						}
						else {
							tupple.setDescription(line.substring(p1+1).trim());							
						}
						params.add(tupple);
					}
				}
				else {
					if( tupple != null ) {
						tupple.setDescription(tupple.getDescription() + Const.CRLF + line);
					}
					else {
						comments.add(line);
					}
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumberOfCommentLines() {
		return comments.size();
	}
	
	public String getCommentLine(int lineNo) {
		return comments.get(lineNo);
	}
	
	public String getComments() {
		StringBuilder buf = new StringBuilder();
		for(String line: comments) {
			if( buf.length() > 0 ) {
				buf.append(Const.CRLF);
			}
			buf.append(line);
		}
		return buf.toString();
	}
	
	public String getParamDescription(String name) {
		if( name == null ) {
			throw new IllegalArgumentException("name is null.");
		}
		for(Tupple tupple: params) {
			if( !"@param".equals(tupple.getType()) ) {
				continue;
			}
			if( name.equals(tupple.getName())  ) {
				return tupple.getDescription();
			}
		}
		return null;
	}

	public String getReturnDescription() {
		for(Tupple tupple: params) {
			if( !"@return".equals(tupple.getType()) ) {
				continue;
			}
			return tupple.getDescription();
		}
		return null;
	}

	public String getExceptionDescription(String name) {
		if( name == null ) {
			throw new IllegalArgumentException("name is null.");
		}
		for(Tupple tupple: params) {
			if( !"@exception".equals(tupple.getType()) ) {
				continue;
			}
			if( tupple.getName() != null && tupple.getName().startsWith(name)  ) {
				return tupple.getDescription();
			}
		}
		return null;
	}
}
