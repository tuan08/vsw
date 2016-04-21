package org.headvances.hadoop.batchdb;

import java.text.DecimalFormat;

import org.apache.hadoop.io.Text;
import org.headvances.hadoop.util.HDFSUtil;
import org.junit.Assert;
import org.junit.Test;
import org.vsw.util.FileUtil;

public class HaiColumnFamilyDBUnitTest {
  static DecimalFormat NFORMATER = new DecimalFormat("0000000000000") ;

  @Test
  public void test() throws Exception {
    String dblocation = "target/db/partition-0" ;
    FileUtil.removeIfExist("target/db") ;
    ColumnDefinition columnDefinition = new ColumnDefinition("test") ;
    DatabaseConfiguration dbconfiguration = 
      new DatabaseConfiguration(new ColumnDefinition[] {columnDefinition}, null) ;
    dbconfiguration.setHadoopConfiguration(HDFSUtil.getDaultConfiguration()) ;
    final ColumnFamilyDB db = new ColumnFamilyDB(dblocation, dbconfiguration, columnDefinition) ;

    Segment segment0 = db.newSegment() ;
    Assert.assertEquals(0, segment0.getIndex()) ;
    populateSegmentData(segment0, 10, 1) ;

    Segment segment1 = db.newSegment() ;
    Assert.assertEquals(1, segment1.getIndex()) ;
    populateSegmentData(segment1, 20, 2) ;
    assertData(db, 15) ;
    
    db.autoCompact(null) ;
    assertData(db, 15) ;
    Assert.assertEquals(1, db.getSegments().length) ;
    
    Segment segment = db.getSegments()[0] ;
    Assert.assertEquals(0, segment.getIndex()) ;
    
  }
  
  private void assertData(ColumnFamilyDB db, int expectSize) throws Exception {
    ColumnFamilyIterator mitr = db.getColumnFamilyIterator() ;
    RowId pkey = null, key = new RowId() ;
    Cell pvalue = null, value  = new Cell() ;
    int count = 0 ;
    while(mitr.next()) {
      key = mitr.currentKey() ;
      value = mitr.currentValue() ;
      if(pkey != null) {
        Assert.assertTrue(pkey.compareTo(key) < 0) ;
      }
      if(value.getFieldAsInt("value") % 2 == 0) {
        Assert.assertEquals(0, value.getFieldAsInt("segment")) ;
      } else {
        Assert.assertEquals(1, value.getFieldAsInt("segment")) ;
      }
      pkey = key ; pvalue = value ;
      count++ ;
    }
    mitr.close() ;
    Assert.assertEquals(count, expectSize) ;
  }

  private void populateSegmentData(Segment seg, int size, int incr) throws Exception {
    Segment.Writer writer = seg.getWriter() ;
    for(int i = seg.getIndex(); i < size; i += incr) {
      Cell datum = createCell(seg.getIndex(), i) ;
      RowId key = new RowId(new Text(datum.getFieldAsString("url")), System.currentTimeMillis(), System.currentTimeMillis(), RowId.STORE_STATE) ;
      writer.append(key, datum); 
    }
    writer.close() ;
  }

  private Cell createCell(int segment, int value) {
    String url = "http://vnexpress.net/" + NFORMATER.format(value) ;
    Cell datum = new Cell() ;
    datum.addField("url", url) ;
    datum.addField("value", value);
    datum.addField("segment", segment) ;
    return datum ;
  }
}