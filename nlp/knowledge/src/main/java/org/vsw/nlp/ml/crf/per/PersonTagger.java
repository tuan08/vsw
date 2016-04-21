package org.vsw.nlp.ml.crf.per;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenSplitterAnalyzer;

public class PersonTagger {
  private PersonDocumentReader reader;
  
  public PersonTagger() throws Exception{
    init();
  }
  
  void init() throws Exception{
    final NLPResource resource = NLPResource.getInstance() ;
    final TokenAnalyzer[] tAnalyzer = { 
      new TokenSplitterAnalyzer(resource)
    };
    final TokenAnalyzer[] wAnalyzer = { 
        new PersonEntityTokenAnalyzer(resource),
    };
    reader = new PersonDocumentReader(NLPResource.getInstance()){
      protected void onPostAnalyzeToken(Document doc) throws TokenException { 
        doc.analyze(tAnalyzer);
      }
      
      protected void onPostAnalyzeWord(Document doc) throws TokenException {
        doc.analyze(wAnalyzer);
      }
    };
  }
  
  public IToken[] tagger(String content) throws TokenException{
    Document doc = reader.read(content);
    IToken[] token = doc.getTokens();
    return token;
  }
  
  public static void main(String[] args) {
  }
}