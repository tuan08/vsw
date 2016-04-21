package org.vsw.nlp.ml.crf.qtag;

import org.vsw.nlp.token.tag.Tag;

public class QuestionTag extends Tag{
  final static public String TYPE = "qtag" ;
  
  private String tagValue ;
  
  public QuestionTag(String tagValue) {
    this.tagValue = tagValue ;
  }
  
  public String getTagValue() { return this.tagValue ; }
  
  public String getOType() { return TYPE ; }

  public boolean isTypeOf(String type) {
    return TYPE.equals(type);
  }
}
