package org.vsw.extract.mediawiki;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

public class XmlParser {
	//@Test
	public void test() throws Exception {
		FileInputStream fileInputStream = new FileInputStream("d:/viwiki-20101031-pages-articles.xml");
		XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fileInputStream);
		int count = 0 ;
		while(count < 1 && xmlStreamReader.hasNext()) {
			printEventInfo(xmlStreamReader);
			count++ ;
		}
		xmlStreamReader.close();
	}
	
	private void printEventInfo(XMLStreamReader reader) throws Exception {
		int eventCode = reader.next();
		switch (eventCode) {
			case XMLStreamConstants.START_ELEMENT :
				System.out.println("event = START_ELEMENT " +reader.getLocalName());
				printElement("", reader) ;
				break;
			case XMLStreamConstants.END_ELEMENT :
				System.out.println("event = END_ELEMENT " + reader.getLocalName());
				break;
			case XMLStreamConstants.PROCESSING_INSTRUCTION :
				System.out.println("event = PROCESSING_INSTRUCTION");
				break;
			case XMLStreamConstants.CHARACTERS :
				System.out.println("event = XMLStreamConstants.CHARACTERS") ;
				if(reader.hasText())
				  System.out.println("  " + reader.getText());
				break;
			case XMLStreamConstants.COMMENT :
				System.out.println("event = XMLStreamConstants.COMMENT");
				break;
			case XMLStreamConstants.SPACE :
				System.out.println("event = XMLStreamConstants.SPACE");
				break;
			case XMLStreamConstants.START_DOCUMENT :
				System.out.println("event = XMLStreamConstants.START_DOCUMENT");
				break;
			case XMLStreamConstants.END_DOCUMENT :
				System.out.println("event = XMLStreamConstants.END_DOCUMENT");
				break;
			case XMLStreamConstants.ENTITY_REFERENCE :
				System.out.println("event = XMLStreamConstants.ENTITY_REFERENCE");
				break;
			case XMLStreamConstants.ATTRIBUTE :
				System.out.println("event = XMLStreamConstants.ATTRIBUTE");
				break;
			case XMLStreamConstants.DTD :
				System.out.println("event = XMLStreamConstants.DTD");
				break;
			case XMLStreamConstants.CDATA :
				System.out.println("event = XMLStreamConstants.CDATA");
				break;
			case XMLStreamConstants.NAMESPACE :
				System.out.println("event = XMLStreamConstants.NAMESPACE");
				break;
			case XMLStreamConstants.NOTATION_DECLARATION :
				System.out.println("event = XMLStreamConstants.NOTATION_DECLARATION");
				break;
			case XMLStreamConstants.ENTITY_DECLARATION :
				System.out.println("event = XMLStreamConstants.ENTITY_DECLARATION");
				break;
			default:
		}
	}
	
	private void printElement(String parentEle, XMLStreamReader reader) throws Exception {
		String lname = reader.getLocalName() ;
		int eventCode = reader.next();
		while(reader.hasNext()) {
			switch (eventCode) {
				case XMLStreamConstants.START_ELEMENT :
					System.out.println("event = START_ELEMENT " +reader.getLocalName());
					break;
				case XMLStreamConstants.END_ELEMENT :
					System.out.println("event = END_ELEMENT " + reader.getLocalName());
					if(lname.equals(reader.getLocalName())) return ;
					break;
				case XMLStreamConstants.PROCESSING_INSTRUCTION :
					System.out.println("event = PROCESSING_INSTRUCTION");
					break;
				case XMLStreamConstants.CHARACTERS :
					System.out.println("event = XMLStreamConstants.CHARACTERS") ;
					if(reader.hasText())
					  System.out.println("  " + reader.getText());
					break;
				case XMLStreamConstants.COMMENT :
					System.out.println("event = XMLStreamConstants.COMMENT");
					break;
				case XMLStreamConstants.SPACE :
					System.out.println("event = XMLStreamConstants.SPACE");
					break;
				case XMLStreamConstants.START_DOCUMENT :
					System.out.println("event = XMLStreamConstants.START_DOCUMENT");
					break;
				case XMLStreamConstants.END_DOCUMENT :
					System.out.println("event = XMLStreamConstants.END_DOCUMENT");
					break;
				case XMLStreamConstants.ENTITY_REFERENCE :
					System.out.println("event = XMLStreamConstants.ENTITY_REFERENCE");
					break;
				case XMLStreamConstants.ATTRIBUTE :
					System.out.println("event = XMLStreamConstants.ATTRIBUTE");
					break;
				case XMLStreamConstants.DTD :
					System.out.println("event = XMLStreamConstants.DTD");
					break;
				case XMLStreamConstants.CDATA :
					System.out.println("event = XMLStreamConstants.CDATA");
					break;
				case XMLStreamConstants.NAMESPACE :
					System.out.println("event = XMLStreamConstants.NAMESPACE");
					break;
				case XMLStreamConstants.NOTATION_DECLARATION :
					System.out.println("event = XMLStreamConstants.NOTATION_DECLARATION");
					break;
				case XMLStreamConstants.ENTITY_DECLARATION :
					System.out.println("event = XMLStreamConstants.ENTITY_DECLARATION");
					break;
				default:
			}
			eventCode = reader.next();
		}
	}
}
