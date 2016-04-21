package org.vsw.nlp.index;

import java.io.File;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FSDocumentSearcher extends DocumentSearcher {
  private String indexName ;
  private String shardName ;
  private String indexDir  ;
  
  public FSDocumentSearcher(String baseDir, String indexName, String shardName) throws Exception {
  	super(null) ;
  	this.indexName = indexName ;
  	this.shardName = shardName ;
  	this.indexDir = baseDir + "/" + indexName + "/" + shardName ;
  	File file = new File(indexDir) ;
  	init(FSDirectory.open(file)) ;
  }
  
  public FSDocumentSearcher(Directory directory) throws Exception {
  	super(directory) ;
  }

  public String getShardName() { return this.shardName ; }

  public String getIndexName() { return this.indexName ; }

  public String getIndexDir() { return indexDir ; }
}