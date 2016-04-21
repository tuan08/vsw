package org.vsw.extract.mediawiki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.vsw.extract.Section;

public class InfoBox extends HashMap<String, String> {
	private String header ;

	public String getHeader() { return this.header ; }
	public void  setHeader(String s) { this.header = s; } 
	
	public void add(String name, String value) {
		put(name, value) ;
	}
	
	public Section toSection() {
		Iterator<Map.Entry<String, String>> i = entrySet().iterator() ;
		StringBuilder b = new StringBuilder() ;
		while(i.hasNext()) {
			Map.Entry<String, String> entry = i.next(); 
			b.append(entry.getKey()).append(" = ").append(entry.getValue()).append(";\n") ;
		}
		List<String> paragraph = new ArrayList<String>() ;
		paragraph.add(b.toString()) ;
		return new Section("infobox", header, paragraph) ;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Header: ").append(header) ;
		Iterator<Map.Entry<String, String>> i = entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, String> entry = i.next() ;
			b.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n") ;
		}
		return b.toString() ;
	}
	
	static Pattern ROW_SEPARATOR = Pattern.compile("([\n\r ]\\|)|(\\|[ \n\r])") ;
	static public InfoBox parse(String text) {
		char[] buf = text.toCharArray() ;
		Candidate candidate = null ;
		int start = text.indexOf("{{Infobox") ;
		if(start < 0) start = 0 ;
		while((candidate = findWikiFormat(buf, start)) != null) {
			//System.out.println(new String(buf, candidate.from, candidate.to - candidate.from));
			String wformat = new String(buf, candidate.from + 2 , candidate.to - candidate.from - 4) ;
			String[] row = ROW_SEPARATOR.split(wformat) ;
			if(row.length > 2) {
				InfoBox infoBox = new InfoBox() ;
				int equalCounter = 0 ;
				for(int i = 0; i < row.length; i++) {
					int equalIdx = row[i].indexOf('=') ;
					if(i == 0 && equalIdx < 1) {
						infoBox.setHeader(row[i]) ;
					} else if(equalIdx > 0) {
						String name = row[i].substring(0, equalIdx).trim() ;
						if(equalIdx + 1 < row[i].length()) {
						  String value = row[i].substring(equalIdx + 1).trim() ;
						  infoBox.add(name, value) ;
						  equalCounter++ ;
						} else {
							infoBox.add(name, "") ;
						}
					} else {
						infoBox.add(row[i], "") ;
					}
				}
				if(equalCounter > 0) return infoBox ;
			}
			start = candidate.to; 
		}
		return null ;
	}
	
	static Candidate findWikiFormat(char[] buf, int from) {
		int idx  = from ;
		int openCount = 0 ;
		int openFormatAt = -1 ;
		while(idx < buf.length) {
			if(isOpenWikiFormat(buf, idx)) {
				openCount++ ;
				if(openFormatAt < 0) openFormatAt = idx ;
				idx += 2 ;
			} else if(isCloseWikiFormat(buf, idx)) {
				openCount-- ;
				if(openCount == 0) {
					return new Candidate(openFormatAt, idx + 2) ;
				}
				idx += 2 ;
			} else if(openCount == 0 && isEnd(buf, idx)) {
				return null ;
			} else {
				idx++ ;
			}
		}
		return null ;
	}
	
	private static boolean isOpenWikiFormat(char[] buf, int pos) {
		if(pos + 1 >= buf.length) return false ;
		return buf[pos] == '{' && buf[pos + 1] == '{' ;
	}
	
	private static boolean isCloseWikiFormat(char[] buf, int pos) {
		if(pos + 1 >= buf.length) return false ;
		return buf[pos] == '}' && buf[pos + 1] == '}' ;
	}
	
	private static boolean isEnd(char[] buf, int pos) {
		if(pos + 3 >= buf.length) return false ;
		return (buf[pos] == '\n'  || buf[pos] == '\r') && buf[pos + 1] == '=' && buf[pos + 2] == '=';
	}
	
	static class Candidate {
		int from , to ;
		
		Candidate(int from, int to) {
			this.from = from ;
			this.to = to ;
		}
	}
}
