package my.draw.flowchart.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import my.draw.core.Box;
import my.draw.flowchart.DoBox;
import my.draw.flowchart.FlowchartBox;
import my.draw.flowchart.ForBox;
import my.draw.flowchart.IfBox;
import my.draw.flowchart.SwitchBox;
import my.draw.flowchart.TryBox;
import my.draw.flowchart.WhileBox;

public class FlowchartReader implements Closeable {
	private static final Log log = LogFactory.getLog(FlowchartReader.class); 
	
	private File file;
	private String enc;
	
	private BufferedReader rdr;
	private String pushbackLine = null;
	private int lcnt = 0;
	
	public FlowchartReader(File file, String enc) throws UnsupportedEncodingException, FileNotFoundException {
		this.file = file;
		this.enc = enc;
		this.rdr = new BufferedReader(new InputStreamReader(new FileInputStream(file),enc));
	}
	
	@Override
	public void close() throws IOException {
		if( rdr != null ) {
			rdr.close();
			rdr = null;
		}
	}

	public Box read() throws IOException {
		List<Box> list = new ArrayList<>();
		
		int prevLevel = -1;
		String line;
		while(  true ) {
			if( pushbackLine != null ) {
				line = pushbackLine;
				pushbackLine = null;
			}
			else {
				line=rdr.readLine();
				if( line == null ) {
					break;
				}
			}
			lcnt++;
			
			if( line.trim().length() <= 0 ) {
				continue;
			}

			int level = level(line);
			
			if( level == 0 ) {
				if( list.size() > 0 ) {
					pushbackLine = line;
					lcnt--;
					break;
				}
				else {
					makeBoxAndSet(list,line);
					continue;
				}
			}
			else {
				if( list.size() < level ) {
					throw new IllegalStateException("invalid nest level at " + lcnt);
				}
				makeBoxAndSet(list,line);
			}
		}

		if( list.size() <= 0 ) {
			return null;
		}
		return list.get(0);
	}
	
	private int level(String line) {
		line = line.replace("  ", "\t"); //２個連続の空白をTABに置換する。
		for(int i=0; i<line.length(); i++) {
			if( line.charAt(i) != '\t' ) {
				return i;
			}
		}
		return 0;
	}
	
	private void remove(List<Box> list, int level) {
		while(list.size() > level) {
			Box box = list.remove(list.size()-1);
			if( log.isTraceEnabled() ) {
				log.trace("removed: " + box.getClass().getSimpleName() + ": " + box.getLabel());
			}
		}
	}
	
	private void add(List<Box> list, Box box) {
		if( list.size() > 0 ) {
			list.get(list.size()-1).add(box);
		}
		list.add(box);
		if( log.isTraceEnabled() ) {
			log.trace("added: " + box.getClass().getSimpleName() + ": " + box.getLabel());
		}
	}
	
	private void makeBoxAndSet(List<Box> list, String line) {
		int level = level(line);

		int p = line.indexOf(":");
		String type;
		String label;
		if( p >= 0 ) {
			type = line.substring(0,p).trim().toUpperCase();
			label = line.substring(p+1).trim();
		}
		else {
			type = "BOX";
			label = line.trim();
		}

		if( "IF".equals(type) ) {
			remove(list,level);
			IfBox ifbox = new IfBox();
			ifbox.addCondition(label);
			add(list,ifbox);
			list.add(ifbox.getCondition());
		}
		else if( "ELSE IF".equals(type) ) {
			remove(list,level+1);
			IfBox box = (IfBox)list.get(list.size()-1);
			box.addCondition(label);
			list.add(box.getCondition());
		}
		else if( "ELSE".equals(type) ) {
			remove(list,level+1);
			IfBox box = (IfBox)list.get(list.size()-1);
			box.addCondition(null);
			list.add(box.getCondition());
		}
		
		else if( "SWITCH".equals(type) ) {
			remove(list,level);
			SwitchBox box = new SwitchBox(label);
			add(list,box);
		}
		else if( "CASE".equals(type) ) {
			remove(list,level+1);
			SwitchBox box = (SwitchBox)list.get(list.size()-1);
			box.addCase(label);
		}
		else if( "DEFAULT".equals(type) ) {
			remove(list,level+1);
			SwitchBox box = (SwitchBox)list.get(list.size()-1);
			box.addCase(null);
		}
		
		else if( "TRY".equals(type) ) {
			remove(list,level);
			TryBox box = new TryBox();
			add(list,box);
		}
		else if( "RES".equals(type) ) {
			remove(list,level+1);
			TryBox box = (TryBox)list.get(list.size()-1);
			box.addResource(label);
		}
		else if( "CATCH".equals(type) ) {
			remove(list,level+1);
			TryBox box = (TryBox)list.get(list.size()-1);
			box.addCatch(label);
		}
		else if( "FINALLY".equals(type) ) {
			remove(list,level+1);
			TryBox box = (TryBox)list.get(list.size()-1);
			box.addFinally();
		}
		
		else if( "WHILE".equals(type) ) {
			remove(list,level+1);
			if( list.size() > 0 ) {
				Box parent = list.get(list.size()-1);
				if( parent instanceof DoBox ) {
					((DoBox)parent).addCondition(label);
					return;
				}
			}
			
			remove(list,level);
			WhileBox box = new WhileBox(label);
			add(list,box);
		}
		
		else if( "FOR".equals(type) ) {
			remove(list,level);
			ForBox box = new ForBox(label);
			add(list,box);
		}
		
		else if( "DO".equals(type) ) {
			remove(list,level);
			DoBox box = new DoBox();
			add(list,box);
		}
		
		else if( "FLOWCHART".equals(type) ) {
			remove(list,level);
			FlowchartBox box = new FlowchartBox(label);
			add(list,box);
		}
		
		else {
			Box box = new Box(label);
			remove(list,level);
			add(list,box);
		}
	}
}
