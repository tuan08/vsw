package org.vsw.nlp.index ;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/**
 * Emits the entire input as a single token.
 */
public class WordTokenStream extends TokenStream {
  static Pattern SPLITTER = Pattern.compile("\\.\\.") ;
  
  private TermAttribute termAtt;
  private String[] token ;
  private int pos = 0 ;
  
  public WordTokenStream(String token) {
    this.token = new String[] { token } ;
    DocumentQueryParser.normalize(this.token) ;
    termAtt = addAttribute(TermAttribute.class);
  }
  
  public WordTokenStream(String[] token) {
    this.token =  token  ;
    DocumentQueryParser.normalize(this.token) ;
    termAtt = addAttribute(TermAttribute.class);
  }
  
  public final boolean incrementToken() throws IOException {
    clearAttributes();
    if(pos == token.length) return false ;
    String term = token[pos++] ;
    termAtt.setTermBuffer(term) ;
    termAtt.setTermLength(term.length());
    return true;
  }
  
  public void reset() throws IOException {
    pos = 0 ;
  }
  
  public void close() throws IOException { }
}