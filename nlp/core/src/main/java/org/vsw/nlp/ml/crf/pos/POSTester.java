package org.vsw.nlp.ml.crf.pos;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.ml.crf.CRFTester;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;

public class POSTester extends CRFTester {

  public POSTester() throws Exception {
    this("nlp/pos.crf");
  }

  public POSTester(String crfModel) throws Exception {
    super(crfModel);
  }

  protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
    return  POSTrainer.newTokenFeaturesGenerator(NLPResource.getInstance()) ;
  }

  public static void main(String[] args) throws Exception {
    args = new String[] { "-file",
        "src/test/resources/POStagged/POSsample.txt" };
    POSTester tester = new POSTester();
    tester.test(args);
  }
}