package org.vsw.nlp.index;

import java.io.File;

import org.apache.lucene.store.FSDirectory;
/**
 * Author : Tuan Nguyen
 *          tuan.nguyen@headvances.com
 * Jun 1, 2010  
 */
public class FSDocumentIndexer extends DocumentIndexer {
  private String indexName ;
  private String shardName ;
  private String indexDir ;
  
  public FSDocumentIndexer(String baseDir, String indexName, String shardName) throws Exception {
    super(null, false) ;
  	this.indexName = indexName ;
    this.shardName = shardName ;
    this.indexDir = baseDir + "/" + indexName + "/" + shardName ;
    File file = new File(indexDir) ;
    boolean autocreate = !file.exists() ;
    init(FSDirectory.open(file), autocreate) ;
  }
  
  public String getShardName() { return this.shardName ; }
  
  public String getIndexName() { return this.indexName ; }

  public String getIndexDir()  { return this.indexDir ; }
}