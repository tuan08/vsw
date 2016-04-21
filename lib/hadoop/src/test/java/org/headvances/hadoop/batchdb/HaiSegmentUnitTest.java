package org.headvances.hadoop.batchdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.apache.hadoop.io.Text;
import org.headvances.hadoop.util.HDFSUtil;
import org.junit.Test;
import org.vsw.util.FileUtil;

public class HaiSegmentUnitTest {
  private String dblocation = "target/db" ;

  @Test
  public void test() throws Exception {
    FileUtil.removeIfExist(dblocation) ;

    assertString(new String[] { "0", "9", "1", "3" }, "first", 4) ;
    assertText(new Text[] { new Text("0"), new Text("9"), new Text("1"), new Text("3") }, "second",  4) ;
    assertInt(new int[] { 0, 9, 1, 3 }, "third", 4) ;
    assertLong(new long[] { 0L, 9L, 1L, 3L }, "fourth", 4) ;
  }
  
  private void assertInt(int[] keys, String prefix, int expected) throws IOException {
    String segname = Segment.createSegmentName(0) ;
    Segment segment =  new Segment(HDFSUtil.getDaultConfiguration(), dblocation, segname) ;
    
    List<byte[]> holder = new ArrayList<byte[]>() ;
    for(int i = 0; i < keys.length; i++) {
      holder.add(Bytes.toBytes(keys[i])) ;
    }
    assertBytes(holder.toArray(new byte[holder.size()][]), prefix, segment, expected) ;
  }
  
  private void assertLong(long[] keys, String prefix, int expected) throws IOException {
    String segname = Segment.createSegmentName(1) ;
    Segment segment =  new Segment(HDFSUtil.getDaultConfiguration(), dblocation, segname) ;
    
    List<byte[]> holder = new ArrayList<byte[]>() ;
    for(int i = 0; i < keys.length; i++) {
      holder.add(Bytes.toBytes(keys[i])) ;
    }
    assertBytes(holder.toArray(new byte[holder.size()][]), prefix, segment, expected) ;
  }
  
  private void assertText(Text[] keys, String prefix, int expected) throws IOException {
    String segname = Segment.createSegmentName(2) ;
    Segment segment =  new Segment(HDFSUtil.getDaultConfiguration(), dblocation, segname) ;
    
    List<byte[]> holder = new ArrayList<byte[]>() ;
    for(int i = 0; i < keys.length; i++) {
      holder.add(TextUtil.getBytes(keys[i])) ;
    }
    assertBytes(holder.toArray(new byte[holder.size()][]), prefix, segment, expected) ;
  }
  
  private void assertString(String[] keys, String prefix, int expected) throws IOException {
    String segname = Segment.createSegmentName(3) ;
    Segment segment =  new Segment(HDFSUtil.getDaultConfiguration(), dblocation, segname) ;
    
    List<byte[]> holder = new ArrayList<byte[]>() ;
    for(int i = 0; i < keys.length; i++) {
      holder.add(Bytes.toBytes(keys[i])) ;
    }
    assertBytes(holder.toArray(new byte[holder.size()][]), prefix, segment, expected) ;
  }
  
  
  private void assertBytes(byte[][] keys, String prefix, Segment segment, int expected) throws IOException {
    Segment.Writer writer = segment.getWriter() ;
    List<String> holder = new ArrayList<String>() ;
    for(int i = 0; i < keys.length; i++) {
      holder.add(Bytes.toString(keys[i])) ;
      RowId id = new RowId(keys[i], System.currentTimeMillis(), System.currentTimeMillis(), RowId.STORE_STATE) ;
      Cell value = new Cell() ;
      value.setName(holder.get(i)) ;
      if(prefix != null) value.addField("value", prefix + " " + holder.get(i)) ;
      else value.addField("value", holder.get(i)) ;
      value.setKey(id) ;
      writer.append(id, value) ;
    }
    writer.close() ;
    assertData(holder, prefix, segment, expected) ;
  }
  
  private void assertData(List<String> holder, String prefix, Segment segment, int expected) throws IOException {
    MultiSegmentIterator iterator = new MultiSegmentIterator(new Segment[] { segment }) ;
    Collections.sort(holder) ;
    int count = 0 ;
    while(iterator.next()) {
      RowId id = iterator.currentKey() ;
      Cell cell = iterator.currentValue() ;
      Assert.assertEquals(holder.get(count), Bytes.toString(id.getKey())) ;
      if(prefix != null) Assert.assertEquals(prefix + " " + holder.get(count), cell.getFieldAsString("value")) ;
      else Assert.assertEquals(holder.get(count), cell.getFieldAsString("value")) ;
      count++ ;
    };
    Assert.assertEquals(expected, count) ;
  }
}
