package org.vsw.nlp.index;

import junit.framework.Assert;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocsCollector;
import org.junit.Before;
import org.junit.Test;
import org.vsw.util.DateUtil;
import org.vsw.util.FileUtil;
import org.vsw.util.StringUtil;
/**
 * Author : Tuan Nguyen
 *          tuan.nguyen@headvances.com
 * Jun 1, 2010  
 */
public class DocumentIndexerAndSearcherUnitTest {
  static byte[] DATA_BIN = "hà nội thủ đô việt nam".getBytes(StringUtil.UTF8) ;
  
	@Before
  public void setup() throws Exception {
    FileUtil.removeIfExist("target/index") ;
  }
  
	@Test
  public void test() throws Exception {
    DocumentIndexer indexer = new FSDocumentIndexer("target", "index", "shard0") ;
    int NUM_DOC = 10 ;
    index(indexer, NUM_DOC) ;
    DocumentSearcher searcher = new DocumentSearcher(indexer.getDirectory()) ;
    search(searcher, NUM_DOC) ;
	}
	
	void index(DocumentIndexer indexer, int NUM_DOC) throws Exception {
    for(int i = 1; i <= NUM_DOC; i++) {
    	IndexDocument idoc = new IndexDocument() ;
      idoc.add("field", new String[] { "hanoi", "hà nội", "việt nam", "doc " + i}) ;
      idoc.add("field:int", i) ;
      idoc.add("field:long", (i * 1l)) ;
      idoc.add("field:float", (i * 1f)) ;
      idoc.add("field:dbl", (i * 1d)) ;
      idoc.add("field:date", DateUtil.parseCompactDate( i + "/1/2011")) ;
      idoc.add("field:bin", DATA_BIN) ;
    	indexer.index(idoc) ;
    }
    indexer.commit() ;
    indexer.close() ;
	}
	
	void search(DocumentSearcher searcher, int NUM_DOC) throws Exception {
    expect(searcher, "field:doc.1", 1) ;
    expect(searcher, "field:hanoi", NUM_DOC) ;
    expect(searcher, "field:hà.nội", NUM_DOC) ;
    expect(searcher, "field:việt.nam", NUM_DOC) ;
    expect(searcher, "+field:(hà.nội việt.nam)", NUM_DOC) ;
    
    QueryParser custom = new DocumentQueryParser(searcher.getAnalyzer()) ;
   
    expect(searcher, custom.parse("field\\:int:[1 TO 1]"),  1) ;
    expect(searcher, custom.parse("field\\:int:[1 TO 2]"), 2) ;
    expect(searcher, custom.parse("field\\:int:[11 TO 20]"), 0) ;
    expect(searcher, custom.parse("field\\:int:{0 TO 1}"), 0) ;
    expect(searcher, custom.parse("field\\:int:{1 TO 4}"), 2) ;
    expect(searcher, custom.parse("field\\:int:{10 TO 15}"), 0) ;
    
    expect(searcher, custom.parse("field\\:long:[1 TO 1]"),  1) ;
    expect(searcher, custom.parse("field\\:long:[1 TO 2]"), 2) ;
    expect(searcher, custom.parse("field\\:long:[11 TO 20]"), 0) ;
    expect(searcher, custom.parse("field\\:long:{0 TO 1}"), 0) ;
    expect(searcher, custom.parse("field\\:long:{1 TO 4}"), 2) ;
    expect(searcher, custom.parse("field\\:long:{10 TO 15}"), 0) ;
    
    expect(searcher, custom.parse("field\\:float:[1.0 TO 1.0]"),  1) ;
    expect(searcher, custom.parse("field\\:float:[1.0 TO 2.0]"), 2) ;
    expect(searcher, custom.parse("field\\:float:[11.0 TO 20.0]"), 0) ;
    expect(searcher, custom.parse("field\\:float:{0.0 TO 1.0}"), 0) ;
    expect(searcher, custom.parse("field\\:float:{1.0 TO 4.0}"), 2) ;
    expect(searcher, custom.parse("field\\:float:{10.0 TO 15.0}"), 0) ;
    
    expect(searcher, custom.parse("field\\:dbl:[1.0 TO 1.0]"),  1) ;
    expect(searcher, custom.parse("field\\:dbl:[1.0 TO 2.0]"), 2) ;
    expect(searcher, custom.parse("field\\:dbl:[11.0 TO 20.0]"), 0) ;
    expect(searcher, custom.parse("field\\:dbl:{0.0 TO 1.0}"), 0) ;
    expect(searcher, custom.parse("field\\:dbl:{1.0 TO 4.0}"), 2) ;
    expect(searcher, custom.parse("field\\:dbl:{10.0 TO 15.0}"), 0) ;
    
    expect(searcher, custom.parse("field\\:date:[1/1/2011 TO 1/1/2011]"), 1) ;
    expect(searcher, custom.parse("field\\:date:{1/1/2011 TO 4/1/2011}"), 3) ;
    searcher.close() ;
	}
	
  private void expect(DocumentSearcher searcher, String query, int expect) throws Exception{
    TopDocsCollector collector = searcher.query(query) ;
    Assert.assertEquals(expect, collector.getTotalHits()) ;
  }
  
  private void expect(DocumentSearcher searcher, Query query, int expect) throws Exception{
    TopDocsCollector collector = searcher.query(query, 1000) ;
    Assert.assertEquals(expect, collector.getTotalHits()) ;
  }
}
