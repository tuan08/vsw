package org.vsw.nlp.ml.crf.qtag;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.CRFTrainer;
import org.vsw.nlp.ml.crf.CapLetterFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.ent.EntitySetConfig;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
import org.vsw.nlp.ml.io.WTagDocumentReader;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

public class QTAGTrainer extends CRFTrainer {
  protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
    String configName = command.getOption("config", "qtag") ;
    EntitySetConfig config = EntitySetConfig.getConfig(configName) ;
    return newTokenFeaturesGenerator(NLPResource.getInstance(), config) ;
  }

  static public TokenFeaturesGenerator newTokenFeaturesGenerator(NLPResource resource, EntitySetConfig config) throws Exception {
    TokenFeaturesGenerator featuresGenerator = newTokenFeaturesGenerator(config) ;
    if(resource == null) resource = NLPResource.getInstance();
    final TokenAnalyzer posAnalyzer = new POSTokenAnalyzer(resource) ;
    WTagDocumentReader reader = new WTagDocumentReader(resource) {      
      protected void onPostAnalyzeWord(Document doc) throws TokenException { 
        doc.analyze(posAnalyzer) ;
      }
    };
    featuresGenerator.setDocumentReader(reader);
    return featuresGenerator ;
  }
  
  static public TokenFeaturesGenerator newTokenFeaturesGenerator(EntitySetConfig config) throws Exception {
    TokenFeaturesGenerator featuresGenerator = new TokenFeaturesGenerator() ;
    featuresGenerator.add(new CapLetterFeatureGenerator()) ;
    featuresGenerator.add(new NGramFeatureGenerator()) ;
    featuresGenerator.setTargetFeatureGenerator(new QTAGTargetFeatureGenerator(config)) ;
    return featuresGenerator ;
  }
  
  public static void main (String[] args) throws Exception {
    args = new String[] {
      "-sample", "d:/vswdata3/nlp/qtag",
      "-save",   "src/main/resources/models/qtag.crf"
    };
    QTAGTrainer trainer = new QTAGTrainer() ;
    trainer.train(args) ;
  }
}