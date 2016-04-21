package org.vsw.nlp.ml.crf.per;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.ent.tag.EntityTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenSplitterAnalyzer;
import org.vsw.util.IOUtil;

public class PersonTaggerUnitTest {
  @Test
  public void test() throws Exception {
    String text = "Nhà sách Phương Nam";
  
    String text1 = IOUtil.getFileContenntAsString("src/test/resources/PERtagged/test.txt", "UTF-8"); 
    String text2 = "Nguyễn Du là một phố của Hà Nội" ;
    NLPResource resource = NLPResource.getInstance() ;
    final TokenAnalyzer[] tAnalyzer = { 
        new TokenSplitterAnalyzer(resource)
    };
    final TokenAnalyzer[] wAnalyzer = { 
        new PersonEntityTokenAnalyzer(resource),
    };
    final PersonDocumentReader reader = new PersonDocumentReader(resource){
      protected void onPostAnalyzeToken(Document doc) throws TokenException { 
        doc.analyze(tAnalyzer);
      }
      
      protected void onPostAnalyzeWord(Document doc) throws TokenException {
        doc.analyze(wAnalyzer);
      }
    };
    
    long start = System.currentTimeMillis() ;
    
    Document doc = reader.read(text1);
    IToken[] token = doc.getTokens();
    test(token);
    
    long time = System.currentTimeMillis() - start ;
    System.out.println("Analyze text in " + time + "ms");
    
    //PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
    //TabularTokenPrinter printer = new TabularTokenPrinter();
    //printer.print(out, token) ;
  }

  private void test(IToken[] token) throws Exception {
    for (int i = 0; i < token.length; i++) {
      EntityTag entity = (EntityTag) token[i].getFirstTagType(EntityTag.TYPE);
      if (entity != null) {
        System.out.println(entity.getTagValue() + " " + entity.getEntity());
      }
    }
  }
}
