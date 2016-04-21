package org.vsw.nlp.index;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
/**
 * Author : Tuan Nguyen
 *          tuan.nguyen@headvances.com
 * Jun 1, 2010  
 */
public class DocumentIndexer {
  private Directory   directory ;
  private IndexWriter indexWriter ;
  private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

  public DocumentIndexer() throws Exception {
  	init(new RAMDirectory(), true) ;
  }
  
  public DocumentIndexer(Directory directory, boolean autocreate) throws Exception {
  	if(directory != null) init(directory, autocreate) ;
  }
  
  protected void init(Directory directory, boolean autocreate) throws Exception {
    this.directory =  directory ;
    if(IndexWriter.isLocked(directory)) IndexWriter.unlock(directory) ;
    this.indexWriter =
      new IndexWriter(directory, analyzer, autocreate, new MaxFieldLength(IndexWriter.DEFAULT_MAX_FIELD_LENGTH));
    this.indexWriter.setRAMBufferSizeMB(256) ;
  }

  public Directory getDirectory() { return this.directory ; }
  
  public void index(IndexDocument idoc) throws IOException {
    indexWriter.addDocument(idoc.getIndexDocument()) ;
  }
  
  public void commit() throws IOException {
    indexWriter.commit() ;
    indexWriter.optimize() ;
  }
  
  public void close() throws IOException {
    indexWriter.close(true) ;
  }
}