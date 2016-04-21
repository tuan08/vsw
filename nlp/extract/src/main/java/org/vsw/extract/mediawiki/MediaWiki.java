package org.vsw.extract.mediawiki;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.vsw.extract.Document;
import org.vsw.extract.Section;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.mediawiki.MediaWikiParser;
import org.wikimodel.wem.tex.TexSerializer;

public class MediaWiki {
	static IWikiParser parser = new MediaWikiParser() ;
	
	private String url ;
	private String title ;
	private InfoBox infoBox ;
	private Section[] section ;
	private HashSet<String> instructions = new HashSet<String>() ;
	
	private Throwable error ;
	private String cacheText ;

	public MediaWiki() {
	}
	
	public MediaWiki(MediaWiki other, boolean copySection) {
		this.url = other.getUrl() ;
		this.title = other.getTitle() ;
		this.infoBox = other.infoBox ;
		this.instructions = other.instructions ;
		if(copySection) {
			this.section = other.section ;
		}
	}
	
	public MediaWiki(String title, String wikiText) throws Exception {
		wikiText = wikiText.trim() ;
		this.url = "http://vi.wikipedia.org/wiki/" + title.replace(' ', '_') ;
		this.title = title; 
		this.infoBox = InfoBox.parse(wikiText) ;

		if(wikiText.startsWith("#")) {
			int idx = wikiText.indexOf('[') ;
			if(idx < 0) idx = wikiText.indexOf(' ') ;
			if(idx < 0) {
				if(wikiText.length() > 15) idx = 15 ;
				else idx = wikiText.length() ;
			}
			if(idx > 15) idx =  15 ;
			String ins = wikiText.substring(0, idx).toLowerCase() ;
			instructions.add("Page:" + ins) ;
		}
		
		try {
			if(wikiText.length() > 0) {
				TextHolder tholder = new TextHolder() ;
				TexSerializer listener = new TexSerializer(tholder) ;
				StringReader reader = new StringReader(wikiText);
				parser.parse(reader, listener);
				this.section = formatSections(tholder.getText()) ;
			} else {
				this.section = new Section[] {} ;
			}
		} catch(WikiParserException ex) {
			this.error = ex ;
			this.section = new Section[] {} ;
		}
	}
	
	public String getUrl() { return this.url ; }
	public void setUrl(String url) { this.url = url ; }
	
	public String getTitle() { return this.title ; }
	public void   setTitle(String s) { this.title =  s ; }
	
	public InfoBox getInfoBox() { return this.infoBox ; }
	public void setInfoBox(InfoBox ibox) { this.infoBox = ibox ; }
	
	@JsonIgnore
	public Throwable getError() { return this.error ; }
	public boolean hasError() { return error != null ; }

	public HashSet<String> getInstructions() { return this.instructions ; }
	public void setInstructions(HashSet<String> set) { this.instructions = set ; }
	
	public Document toDocument() {
		Document doc = new Document(this.url, this.title) ;
		doc.addSection(this.section) ;
		if(this.infoBox != null) {
			doc.addSection(this.infoBox.toSection()) ;
			
		}
		doc.addSection(this.section) ;
		return doc ;
	}
	
	@JsonIgnore
	public String getText() {
		if(cacheText != null) return cacheText ;
		StringBuilder b = new StringBuilder() ;
		for(int i = 0 ; i < section.length; i++) {
			if(i > 0) b.append("\n") ;
			b.append(section[i].getHeader()).append("\n") ;
			b.append(section[i].getText()).append("\n") ;
		}
		this.cacheText = b.toString() ;
		return cacheText ;
	}
	
	public Section[] getWikiSection() { return this.section ; }
	public void setWikiSection(Section[] section) { this.section = section ; }
	
	final static public Pattern SUBSECTION_PATTERN = Pattern.compile("\\\\subsection") ;
	static public Section[] formatSections(String text) {
		int idx = 0 ;
		int startSection = 0 ;
		Matcher dmatcher = SUBSECTION_PATTERN.matcher(text) ;
		List<Section> list = new ArrayList<Section>() ;
		String defaultHeader = "abstract" ;
		while(idx >= 0) {
			if(dmatcher.find(idx)) {
				idx = dmatcher.start() ;
				String section = text.substring(startSection, idx) ;
				list.add(createSection(defaultHeader, section)) ;
				startSection = dmatcher.end() ;
				idx = startSection ;
				defaultHeader = null ;
			} else {
				idx = -1 ;
			}
		}
		String section = text.substring(startSection, text.length()) ;
		list.add(createSection(defaultHeader, section)) ;
		return list.toArray(new Section[list.size()]) ;
	}
	
	static Section createSection(String defaultHeader, String text) {
		String[] line = text.split("\n") ;
		line[0] = line[0].trim() ;
		List<String> paragraph = new ArrayList<String>() ;
		String header = null ;
		for(int i = 0; i < line.length; i++) {
			line[i] = line[i].trim() ;
			if(i == 0 && line[i].startsWith("{") && line[i].endsWith("}")) {
				header = line[i].substring(1, line[i].length() - 1).trim() ;
			} else {
				paragraph.add(line[i]) ;
			}
		}
		if(header == null) header = defaultHeader ;
		return new Section("section", header, paragraph) ;
	}
}
