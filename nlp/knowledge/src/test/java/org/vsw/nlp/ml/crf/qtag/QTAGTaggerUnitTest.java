package org.vsw.nlp.ml.crf.qtag;

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
import org.vsw.nlp.ml.dict.DictionaryTaggingAnalyzer;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TabularTokenPrinter;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.ConsoleUtil;

public class QTAGTaggerUnitTest {
  @Test
  public void test() throws Exception {
    String text = "Ai được coi là truyền nhân của maradona";
    String text2 = "Ai là người đầu tiên lên mặt trăng";
    NLPResource resource = NLPResource.getInstance() ;
    final TokenAnalyzer[] posAnalyzer = {
      new DictionaryTaggingAnalyzer(resource),
      new POSTokenAnalyzer(resource),
      new QTAGTokenAnalyzer(resource)
    };
    DefaultDocumentReader reader = new DefaultDocumentReader(resource) {
      protected void onPostAnalyzeWord(Document doc) throws TokenException { 
        doc.analyze(posAnalyzer);
      }
    };
    
    Document doc = reader.read(text2);
    IToken[] token = doc.getTokens();
    PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
    TabularTokenPrinter printer = new TabularTokenPrinter();
    printer.print(out, token) ;
  }
}