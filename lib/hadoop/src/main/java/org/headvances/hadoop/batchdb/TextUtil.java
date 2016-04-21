package org.headvances.hadoop.batchdb;

import org.apache.hadoop.io.Text;

public class TextUtil {

  public static byte[] getBytes(Text text) {
    byte[] value = null ;
    if(text.getBytes().length > text.getLength()) {
      value = new byte[text.getLength()] ;
      System.arraycopy(text.getBytes(), 0, value, 0, value.length) ;
    } else value = text.getBytes() ;
    return value ;
  }
}
