/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.nlp.ml.crf;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.doc.io.DocumentSet;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.util.CommandParser;
import org.vsw.util.IOUtil;

import cc.mallet.fst.CRF;
import cc.mallet.fst.CRFTrainerByThreadedLabelLikelihood;
import cc.mallet.fst.Transducer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.InstanceList;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class CRFTrainer {
  private CRF crf ;
  protected CommandParser command ;
  
	public CRF train(InstanceList training, int iterration) {
  	this.crf = new CRF(training.getDataAlphabet(), training.getTargetAlphabet());
  	
  	Pattern forbiddenPat = null ;//Pattern.compile("\\s");
    Pattern allowedPat = null; //Pattern.compile(".*");
    String startName =
    	crf.addOrderNStates(training, /*order*/new int[]{1}, null, /*defaultLabel*/ "O", forbiddenPat, allowedPat, /*connected*/ true);
    for (int i = 0; i < crf.numStates(); i++) {
    	crf.getState(i).setInitialWeight (Transducer.IMPOSSIBLE_WEIGHT);
    }
    crf.getState(startName).setInitialWeight(0.0);
    System.out.println("Training on " + training.size() + " instances");
    
    CRFTrainerByThreadedLabelLikelihood crfTrainer = 
    	new CRFTrainerByThreadedLabelLikelihood(crf,/*numThreads*/ 14);
    crfTrainer.setGaussianPriorVariance(10.0);

    crfTrainer.setUseSparseWeights(true);
    crfTrainer.setUseSomeUnsupportedTrick(true);
    crfTrainer.train (training, iterration);
    crfTrainer.shutdown();
    return crf;
  }
	
	public void save(String file) throws Exception {
		ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(file));
    s.writeObject(crf);
    s.close();
	}
	
	
	abstract protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception ;

	public void train(String[] samples, String save, int iteration) throws Exception {
		long startTime = System.currentTimeMillis() ;
		
  	TokenFeaturesGenerator featuresGenerator = createTokenFeaturesGenerator() ;
    WTagInstanceFactory factory = new WTagInstanceFactory(featuresGenerator) ;
    InstanceList trainingData = 
    	new InstanceList(factory.getDataAlphabet(), factory.getTargetAlphabet());
    DocumentReader docReader = featuresGenerator.getDocumentReader() ;
    for(String file : samples) {
    	System.out.println("Add Train Sample: " + file);
    	String sampleData = IOUtil.getFileContenntAsString(file, "UTF-8") ;
    	if(sampleData.trim().length() == 0) continue ;
    	Document doc = docReader.read(sampleData) ;
    	TokenCollection[] collection = doc.getTokenCollections() ;
    	for(int i = 0; i < collection.length; i++) {
    		IToken[] token = collection[i].getTokens() ;
    		if(token.length == 0) continue ;
    		trainingData.add(factory.createTrainInstance(token)) ;
    	}
    }
    
    System.out.println("Number of features in training data: " + factory.getDataAlphabet().size());
    System.out.println("Number of predicates: " + factory.getDataAlphabet().size());
    Alphabet targetLabels = factory.getTargetAlphabet();
    StringBuilder buf = new StringBuilder("Target Labels:");
    for (int i = 0; i < targetLabels.size(); i++) {
    	buf.append(" ").append(targetLabels.lookupObject(i).toString());
    }
    System.out.println(buf.toString());

    train(trainingData, iteration);
    save(save);
    double time = (System.currentTimeMillis() - startTime)/(double)1000 ;
    System.out.println("Train in " + time + "s");
	}
	
	public void train(String[] args) throws Exception {
		command = new CommandParser("crftrain:") ;
    command.addMandatoryOption("sample", true, "The sampple file or directory, the file extension should be iob2 or tagged") ;
    command.addMandatoryOption("save", true, "The output model file") ;
    command.addOption("iteration", true, "The maximum number of iteration, default is 500") ;
    command.addOption("config", true, "The config name") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;
    
    String sample = command.getOption("sample", null) ;
    String save   = command.getOption("save", null) ;
    int iteration = Integer.parseInt(command.getOption("iteration", "1000")); 
    DocumentSet set = new DocumentSet(sample, ".*\\.(tagged|wtag)", null);
    train(set.getFiles(), save, iteration) ;
  }
}