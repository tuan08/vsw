package org.vsw.extract.mediawiki;

import org.junit.Test;
import org.vsw.extract.Document;
import org.vsw.extract.io.JSONWriter;

public class JSONWriterUnitTest {
	@Test
	public void test() throws Exception {
		XMLMediaWikiDumpReader reader = 
			new XMLMediaWikiDumpReader("src/data/wikipedia/sample/viwiki.dump.xml") ;

		Document document = null; 
		int count = 0 ;
		JSONWriter writer = new JSONWriter(System.out) ;
		while(count < 10 && (document = reader.next()) != null) {
			writer.write(document) ;
			count++ ;
		}
		reader.close() ;
		writer.close() ;
	}
}