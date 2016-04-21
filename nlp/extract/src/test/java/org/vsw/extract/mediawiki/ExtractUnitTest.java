package org.vsw.extract.mediawiki;

import org.junit.Test;
import org.vsw.extract.Document;
import org.vsw.extract.Extractor;
import org.vsw.extract.SectionIterator;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.query.QueryResourceLoader;

public class ExtractUnitTest {
	@Test
	public void test() throws Exception {
		QueryResourceLoader loader = new QueryResourceLoader() ;
		loader.load("classpath:nlp/vietnamese.lexicon.json", "lexicon") ;
		loader.load("classpath:nlp/data.place.json", "place") ;
		loader.load("classpath:nlp/mobile.product.json", "product") ;
		loader.load("src/data/wikipedia/query/split/people.json") ;
		
		Extractor extractor = new Extractor(loader); 
		
		XMLMediaWikiDumpReader reader = 
			//new XMLMediaWikiDumpReader("d:/viwiki-20101031-pages-articles.xml") ;
		  new XMLMediaWikiDumpReader("src/data/wikipedia/sample/viwiki.dump.xml") ;
		
		Document document = null ;
		int count = 0 ;
		while(count < 10 && (document = reader.next()) != null) {
			System.out.println(count + ". " + document.getTitle()) ;
			QueryContext context = new QueryContext() ;
			SectionIterator sitr = 
				new SectionIterator("infobox", document, extractor.getTokenAnalyzer()) ;
			QueryData qdata = null ;
			while((qdata = sitr.next()) != null) {
			  extractor.process(context, qdata) ;
			}
			if(context.getTags().size() > 0) {
				System.out.println(context.toString()) ;
			}
			count++ ;
		}
		reader.close() ;
	}
}