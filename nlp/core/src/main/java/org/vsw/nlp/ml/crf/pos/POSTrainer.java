package org.vsw.nlp.ml.crf.pos;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.ml.crf.CRFTrainer;
import org.vsw.nlp.ml.crf.CapLetterFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.io.WTagDocumentReader;

public class POSTrainer extends CRFTrainer {
  
  protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
    return newTokenFeaturesGenerator(NLPResource.getInstance()) ;
  }
  
  static TokenFeaturesGenerator newTokenFeaturesGenerator(NLPResource resource) throws Exception {
  	WTagDocumentReader reader = new WTagDocumentReader(resource) ;
  	TokenFeaturesGenerator fgenerator = newTokenFeaturesGenerator() ;
  	fgenerator.setDocumentReader(reader) ;
  	return  fgenerator ;
  }
  
  static TokenFeaturesGenerator newTokenFeaturesGenerator() {
  	TokenFeaturesGenerator featuresGenerator = new TokenFeaturesGenerator() ;
    featuresGenerator.add(new TokenFeatureGenerator()) ;
    featuresGenerator.add(new POSDictFeatureGenerator()) ;
    featuresGenerator.add(new CapLetterFeatureGenerator()) ;
    featuresGenerator.setTargetFeatureGenerator(new POSTargetFeatureGenerator()) ;
    return featuresGenerator ;
  }

  public static void main (String[] args) throws Exception {
    args = new String[] {
      "-sample", "src/test/resources/POStagged",
      "-save",   "target/pos.-2.VDICT.crf"
    };
    POSTrainer trainer = new POSTrainer() ;
    trainer.train(args) ;
  }
}