package org.vsw.extract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Document {
	private String    url   ;
	private String    title ;
	private List<Section> section = new ArrayList<Section>() ;
	private Set<String>   tags = new HashSet<String>() ;
	
	public Document() {
		
	}
	
	public Document(String url, String title) {
		this.url = url ;
		this.title = title ;
	}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }
	
	public void addSection(Section section) { this.section.add(section) ; }
	public void addSection(Section[] section) { 
		if(section != null) {
			for(Section sel : section) this.section.add(sel) ;
		}
	}
	public List<Section> getSection() { return section; }
	public void setSection(List<Section> section) { this.section = section; }

	public boolean hasTag(String name) { return tags.contains(name) ; }
	public Set<String> getTags() { return this.tags  ; }
	public void setTags(Set<String> tags) { this.tags = tags ; }
	public void setTag(String name) { tags.add(name) ; }
	
	public int getDataLength() {
		int sum = 0 ;
		for(int i = 0; i < section.size(); i++) {
			sum += section.get(i).getDataLength() ;
		}
		return sum ;
	}
}