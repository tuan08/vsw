package org.vsw.nlp.classification;

import java.util.ArrayList;
import java.util.List;

public class ClassifiedDocument {
	private String category ;
	private String title ;
	private List<Section> sections = new ArrayList<Section>() ;
	
	private String path ;
	
	public String getCategory() { return this.category ; }
	public void setCategory(String cat) { this.category = cat ; }
	
	public String getTitle() { return this.title ; }
	public void setTitle(String s) { this.title = s ; }

	public void add(Section section) {
		this.sections.add(section) ;
	}
	public void add(String sectionName, String content) {
		this.sections.add(new Section(sectionName, content)) ;
	}
	public List<Section> getSections() { return this.sections ; }

	public String getPath() { return this.path ;}
	public void setPath(String p) { this.path = p ; }
	
	static public class Section {
		private String name ;
		private String content ;

		public Section(String name, String content) {
			this.name = name; 
			this.content = content ;
		}

		public String getName() { return this.name ; }

		public String getContent() { return this.content ; }
	}
}
