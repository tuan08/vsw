package org.vsw.nlp.index;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class DocumentSearcher {
  private Directory     directory     ;
  private IndexSearcher indexSearcher ;
  private Analyzer      analyzer      ;
  
  public DocumentSearcher(Directory directory) throws Exception {
  	if(directory != null) init(directory) ;
  }
  
  protected void init(Directory directory) throws Exception {
    this.directory =  directory ;
    this.analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT) ; 
    this.indexSearcher = new IndexSearcher(directory, true) ;
  }
  
  
  public Directory getDirectory() { return this.directory ; }
  
  public Analyzer getAnalyzer() { return this.analyzer ; }
  
  public TopDocsCollector query(String query) throws Exception {
    TopScoreDocCollector collector = TopScoreDocCollector.create(1000, false) ;
    DocumentQueryParser parser = new DocumentQueryParser(analyzer) ;
    indexSearcher.search(parser.parse(query), null, collector) ;
    return collector ;
  }
  
  public TopDocsCollector query(String query,  int limit) throws Exception {
    //System.out.println("Query: " + query) ;
    //System.out.println("Filter: " + StringUtil.joinStringArray(filter)) ;
    TopScoreDocCollector collector = TopScoreDocCollector.create(limit, false) ;
    if((query == null || query.length() == 0)) return collector ;
    
    DocumentQueryParser parser = new DocumentQueryParser(analyzer) ;
    //parser.setDefaultOperator(Operator.OR) ;
    Query pquery = parser.parse(query) ;
    System.out.println("Lucene Query: " + pquery);
    indexSearcher.search(pquery, null, collector) ;
    return collector ;
  }
  
  
  public TopDocsCollector query(Query query,  int limit) throws Exception {
    TopScoreDocCollector collector = TopScoreDocCollector.create(limit, false) ;
    if(query == null) return collector ;
    //System.out.println("Query: " + combine);
    indexSearcher.search(query, null, collector) ;
    return collector ;
  }

  public String getDocumentFieldValue(int docId, String fname) throws Exception {
    Document doc = indexSearcher.getIndexReader().document(docId) ;
    System.out.println("doc: " + doc.getBinaryValue("content:store")) ;
    if(doc == null) return null ;
    return doc.get(fname) ;
  }
  
  public byte[] getDocumentFieldValueAsBytes(int docId, String fname) throws Exception {
    Document doc = indexSearcher.getIndexReader().document(docId) ;
    if(doc == null) return null ;
    return doc.getBinaryValue(fname);
  }
  
  public void close() throws IOException {
    indexSearcher.close() ;
  }
}