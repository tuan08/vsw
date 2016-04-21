package org.vsw.extract;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.vsw.util.StringUtil;

public class Section {
	private String       type  ;
	private String       header ;
	private List<String> paragraph ;
	
	public Section() {
		
	}
	
	public Section(String type, String header, List<String> paragraph) {
		this.type = type ;
		this.header = header ;
		this.paragraph = paragraph ;
	}
	
	public String getType() { return this.type ; }
	public void   setType(String name) { this.type = name ; }
	
	public String getHeader() { return this.header ; }
	public void setHeader(String s) { this.header = s ; }
	
	public List<String> getParagraph() { return this.paragraph ; }
	public void setParagraph(List<String> list) { this.paragraph = list; }
	
	@JsonIgnore
	public String getText() {
		return StringUtil.joinStringCollection(paragraph, "\n\n") ;
	}
	
	public int getDataLength() {
		int sum = 0 ;
		for(int i = 0; i < paragraph.size(); i++) {
			sum += paragraph.get(i).length() ;
		}
		return sum ;
	}
}
