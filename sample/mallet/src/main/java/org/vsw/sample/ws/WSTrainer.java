package org.vsw.sample.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.regex.Pattern;

import cc.mallet.fst.CRF;
import cc.mallet.fst.CRFTrainerByThreadedLabelLikelihood;
import cc.mallet.fst.Transducer;
import cc.mallet.fst.TransducerEvaluator;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.LineGroupIterator;
import cc.mallet.types.Alphabet;
import cc.mallet.types.InstanceList;

public class WSTrainer {
  public static CRF train(InstanceList training) {
  	TransducerEvaluator eval = null ;
  	Pattern forbiddenPat = Pattern.compile("\\s");
    Pattern allowedPat = Pattern.compile(".*");
    CRF crf = new CRF(training.getPipe(), (Pipe)null);
    String startName =
    	crf.addOrderNStates(training, /*order*/new int[]{1}, null, /*defaultLabel*/ "O", forbiddenPat, allowedPat, /*connected*/ true);
    for (int i = 0; i < crf.numStates(); i++)
    	crf.getState(i).setInitialWeight (Transducer.IMPOSSIBLE_WEIGHT);
    crf.getState(startName).setInitialWeight(0.0);
    System.out.println("Training on " + training.size() + " instances");
    
    CRFTrainerByThreadedLabelLikelihood crft = new CRFTrainerByThreadedLabelLikelihood(crf,/*numThreads*/ 4);
    crft.setGaussianPriorVariance(10.0);

    crft.setUseSparseWeights(true);
    crft.setUseSomeUnsupportedTrick(true);

    for (int i = 1; i <= /*iterations*/ 500; i++) {
    	boolean converged = crft.train (training, 1);
    	if (converged) break;
    }
    crft.shutdown();
    return crf;
  }

  public static void main (String[] args) throws Exception {
  	args = new String[] {
  	  "--train", "true", "--threads", "4", "--iterations", "500",
  	  "--model-file", "target/vnword-segment.model", "src/test/resources/simple.train.iob2"	
  	};
  	
    Reader trainingFile = new FileReader(new File("src/test/resources/simple.ws.train"));
    Pipe  pipe = new TokenFeaturesPipe();
    pipe.getTargetAlphabet().lookupIndex(/*defaultOption.value*/ "O");
    pipe.setTargetProcessing(true);

    InstanceList trainingData = new InstanceList(pipe);
    trainingData.addThruPipe(new LineGroupIterator(trainingFile, Pattern.compile("^\\s*$"), true));
    
    System.out.println("Number of features in training data: " + pipe.getDataAlphabet().size());
    System.out.println("Number of predicates: " + pipe.getDataAlphabet().size());
    
    if(pipe.isTargetProcessing()) {
      Alphabet targetLabels = pipe.getTargetAlphabet();
      StringBuilder buf = new StringBuilder("Labels:");
      for (int i = 0; i < targetLabels.size(); i++) {
        buf.append(" ").append(targetLabels.lookupObject(i).toString());
      }
      System.out.println(buf.toString());
    }

    CRF crf = train(trainingData);
    //Save training model
    ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream("target/vnword-segment.model"));
    s.writeObject(crf);
    s.close();
  }
}