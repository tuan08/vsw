package org.vsw.extract.mediawiki;

import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.Test;
import org.vsw.extract.Section;

public class MediawikiUnitTest {
	@Test
	public void test() throws Exception {
		XMLMediaWikiDumpReader xmlreader = 
			new XMLMediaWikiDumpReader("src/data/wikipedia/sample/viwiki.dump.xml") ;
		xmlreader.next() ;
		
		MediaWiki mwiki = xmlreader.nextWiki();
		//System.out.println(formater.getTextHolder().getText());
		Section[] section = mwiki.getWikiSection() ;
		PrintStream out = getUTF8SuportOutput() ;
		for(int i = 0; i < section.length; i++) {
			out.println("Section: " + section[i].getHeader());
			out.println(section[i].getText());
			out.println("---------------------------------------------------") ;
		}
	}
	
	@Test
	public void testWikiSection() {
		String TEXT = 
			"Unknown Section\n" + 
			"\\subsection{Tiểu sử }\n" + 
			"Mỹ Tâm tên khai sinh là Phan Thị Mỹ Tâm\n" +
			"\\subsection{Sự nghiệp âm nhạc}\n" + 
			"Mỹ Tâm tên khai sinh là Phan Thị Mỹ Tâm\n"
			;
		Section[] section = MediaWiki.formatSections(TEXT) ;
		Assert.assertEquals(3, section.length) ;
		
		Assert.assertEquals("abstract", section[0].getHeader()) ;
		Assert.assertEquals("Unknown Section", section[0].getText()) ;
		
		Assert.assertEquals("Tiểu sử", section[1].getHeader()) ;
		Assert.assertEquals("Mỹ Tâm tên khai sinh là Phan Thị Mỹ Tâm", section[1].getText()) ;
		
		Assert.assertEquals("Sự nghiệp âm nhạc", section[2].getHeader()) ;
		Assert.assertEquals("Mỹ Tâm tên khai sinh là Phan Thị Mỹ Tâm", section[2].getText()) ;
	}
	
	static public PrintStream getUTF8SuportOutput() throws Exception {
    PrintStream out = new PrintStream(System.out, true, "UTF-8") ;
    return out ;
  }
}
