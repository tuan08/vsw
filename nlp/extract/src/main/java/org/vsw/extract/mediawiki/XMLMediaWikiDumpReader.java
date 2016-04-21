package org.vsw.extract.mediawiki;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.vsw.extract.Document;
import org.vsw.extract.io.Reader;

public class XMLMediaWikiDumpReader implements Reader {
	private XMLStreamReader reader ;
	
	public XMLMediaWikiDumpReader(String file) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(file);
		reader = XMLInputFactory.newInstance().createXMLStreamReader(fileInputStream, "UTF-8");
	}
	
	public Document next() throws Exception {
		MediaWiki mwiki = nextWiki() ;
		if(mwiki != null) return mwiki.toDocument() ;
		return null ;
	}
	
	public MediaWiki nextWiki() throws Exception {
		Map<String, String> holder = new HashMap<String, String>() ;
		startElement("page") ;
		int eventCode = reader.getEventType() ;
		while(reader.hasNext()) {
			if(eventCode == XMLStreamConstants.START_ELEMENT) {
				String localName = reader.getLocalName() ;
				if("title".equals(localName)) {
					holder.put("page.title", reader.getElementText()) ;
				} else if("text".equals(localName) || eventCode == XMLStreamConstants.CDATA) {
					holder.put("page.text", reader.getElementText()) ;
				}
			} else if(eventCode == XMLStreamConstants.END_ELEMENT) {
				if("page".equals(reader.getLocalName())) {
					String title = holder.get("page.title") ;
					String text = holder.get("page.text") ;
					if(text == null) text = "" ;
					MediaWiki mwiki = new MediaWiki(title, text) ;
					return mwiki ;
				}
			}
			eventCode = reader.next();
		}
		return null ;
	}
	
	public void close() throws Exception {
		reader.close() ;
	}
	
	void startElement(String name) throws Exception {
		while(reader.hasNext()) {
			int eventCode = reader.next();
			if(eventCode == XMLStreamConstants.START_ELEMENT) {
				if(name.equals(reader.getLocalName())) return ;
			}
		}
	}

	void endElement(String name) throws Exception {
		while(reader.hasNext()) {
			int eventCode = reader.next();
			if(eventCode == XMLStreamConstants.END_ELEMENT) {
				if(name.equals(reader.getLocalName())) return ;
			}
		}
	}
}
