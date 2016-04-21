package org.vsw.nlp.classification.svm;

import org.vsw.nlp.classification.ClassifiedDocumentReader;
import org.vsw.nlp.classification.VuClassifiedDocumentReader;
import org.vsw.nlp.classification.svm.lib.svm_train;
import org.vsw.nlp.ml.crf.ent.EntityTagger;
import org.vsw.nlp.ml.crf.ws.WordSegmenter;
import org.vsw.util.CommandParser;

public class SVMTrainer {
	static public void train(String[] args) throws Exception {
		CommandParser command = new CommandParser("crftrain:") ;
    command.addMandatoryOption("dataDir", true, "The directory that store the train data") ;
    command.addMandatoryOption("reader", true, "The Reader implementation") ;
    command.addOption("ofeature", true, "The output feature file") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;
    
    String dataDir = command.getOption("dataDir", null) ;
    String readerType = command.getOption("reader", null);
    String ofeature = command.getOption("ofeature", "svm-features.txt");
    ClassifiedDocumentReader reader = null ;
    if("vu".equals(readerType)) {
    	reader = new VuClassifiedDocumentReader(dataDir) ;
    }
    EntityTagger entityTagger = new EntityTagger(new WordSegmenter()) ;
		FeatureGenerator generator = new FeatureGenerator(entityTagger) ;
		generator.generate(ofeature, reader) ;
		
		String[] svmargs = new String[] { "-b", "0", ofeature };
		svm_train.main(svmargs) ;
	}
	
  static public void main(String[] args) throws Exception {
		
	}
}
