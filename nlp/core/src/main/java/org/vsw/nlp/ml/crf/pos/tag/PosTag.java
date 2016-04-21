package org.vsw.nlp.ml.crf.pos.tag;

import org.vsw.nlp.token.tag.Tag;

public class PosTag extends Tag{
  final static public String TYPE = "pos" ;
  
  private String tagValue ;
  
  public PosTag(String tagValue) {
    this.tagValue = tagValue ;
  }
  
  public String getTagValue() { return this.tagValue ; }
  
  public String getOType() { return TYPE ; }

  public boolean isTypeOf(String type) {
    return TYPE.equals(type);
  }
}
