package org.headvances.hadoop.batchdb;

import org.apache.hadoop.io.Text;
import org.headvances.hadoop.util.HDFSUtil;
import org.junit.Test;
import org.vsw.util.FileUtil;
/**
 * Author : Tuan Nguyen
 *          tuan.nguyen@headvances.com
 * Apr 19, 2010  
 */
public class SegmentUnitTest {
  @Test
  public void test() throws Exception {
    String dblocation = "target/db" ;
    FileUtil.removeIfExist(dblocation) ;
    String segname = Segment.createSegmentName(0) ;
    Segment segment = 
      new Segment(HDFSUtil.getDaultConfiguration(), dblocation, segname) ;
    Segment.Writer writer = segment.getWriter() ;
    for(int i = 0; i < 100; i++) {
    	String url = "http://vnexpress.net/" + i ;
      Cell datum = new Cell() ;
      datum.addField("url", url) ;
      datum.addField("value", i) ;
      datum.addField("segment", 0) ;
      RowId key = new RowId(new Text(url), System.currentTimeMillis(), System.currentTimeMillis(), RowId.STORE_STATE);
      writer.append(key, datum); 
    }
    writer.close() ;
  }
}