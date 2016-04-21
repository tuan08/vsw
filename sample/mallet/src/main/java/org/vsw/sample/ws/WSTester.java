package org.vsw.sample.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.regex.Pattern;

import cc.mallet.fst.CRF;
import cc.mallet.fst.MaxLatticeDefault;
import cc.mallet.fst.Transducer;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.LineGroupIterator;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;

public class WSTester {
  public static Sequence[] apply(Transducer model, Sequence input, int nBest) {
  	Sequence[] answers;
  	if (nBest == 1) {
  		answers  = new Sequence[] {model.transduce (input) };
  	} else {
  		//--cache-size  100000
  		MaxLatticeDefault lattice = new MaxLatticeDefault (model, input, null, /*cache-size*/ 100000);
  		answers = lattice.bestOutputSequences(nBest).toArray(new Sequence[0]);
  	}
  	return answers;
  }

  public static void main (String[] args) throws Exception {
  	//--include-input true --model-file target/vnword-segment.model src/test/resources/test.iob2"
    //test file 
    Reader testFile = new FileReader(new File("src/test/resources/simple.ws.test"));
    //--model-file target/vnword-segment.model
    ObjectInputStream s = new ObjectInputStream(new FileInputStream("target/vnword-segment.model"));
    //CRF == training model
    CRF crf = (CRF) s.readObject();
    s.close();
    
    Pipe  pipe = crf.getInputPipe() ;
    pipe.setTargetProcessing(false);
    
    InstanceList testData = new InstanceList(pipe);
    testData.addThruPipe(new LineGroupIterator(testFile, Pattern.compile("^\\s*$"), true));
    System.out.println("Number of predicates: " + pipe.getDataAlphabet().size());
    
    //Whether to include the input features when printing decoding output
    // --include-input
    for (int i = 0; i < testData.size(); i++) {
    	Sequence input = (Sequence)testData.get(i).getData();
    	//How many answers to output
    	//--n-best=1  ; default value is 1
    	Sequence[] outputs = apply(crf, input, /*nBestOption.value*/ 1);
    	int k = outputs.length;
    	boolean error = false;
    	for (int a = 0; a < k; a++) {
    		if (outputs[a].size() != input.size()) {
    			System.out.println("Failed to decode input sequence " + i + ", answer " + a);
    			error = true;
    		}
    	}
    	if (!error) {
    		for (int j = 0; j < input.size(); j++) {
    			StringBuilder b = new StringBuilder();
    			FeatureVector fv = (FeatureVector)input.get(j);
    			b.append(fv.toString(true));
    			b.setLength(b.length() - 1) ;
    			b.append("[") ;
    			for (int a = 0; a < k; a++)
    				b.append(outputs[a].get(j).toString());
    			b.append("]") ;
    			System.out.println(b.toString());
    		}
    		System.out.println();
    	}
    }
  }
}