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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;

import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DocumentSet;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.util.CommandParser;
import org.vsw.util.ConsoleUtil;
import org.vsw.util.IOUtil;

import cc.mallet.fst.CRF;
import cc.mallet.fst.MaxLatticeDefault;
import cc.mallet.fst.Transducer;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.Sequence;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class CRFTester {
	private String crfModel ;
	private TokenFeaturesGenerator featuresGenerator ;
	private CRF crf ;
	private WTagInstanceFactory factory ;
	protected CommandParser command ;

	public CRFTester(String crfModel) {
		this.crfModel = crfModel ;
	}

	public void init(String modelFile) throws Exception {
		init(new FileInputStream(modelFile)) ;
	}

	public void init(InputStream modelis) throws Exception {
		featuresGenerator = this.createTokenFeaturesGenerator()  ;
		ObjectInputStream s = new ObjectInputStream(modelis);
		crf = (CRF) s.readObject();
		s.close();
		factory = new WTagInstanceFactory(crf.getInputAlphabet(), (LabelAlphabet) crf.getOutputAlphabet(), featuresGenerator) ;
	}

	abstract protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception ;

	public void test(String[] file, boolean printToken) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		Log allLog = new Log(0, 0, 0) ;
		for(String sel : file) {
			Log log  = test(out, sel, printToken) ;
			allLog.merge(log) ;
		}
		DecimalFormat formater = new DecimalFormat("#.00%") ;
		double ratio = (double)allLog.hit/allLog.token ;
		System.out.println("Correct Ratio For All: " + formater.format(ratio) + ", " + allLog.hit + "/" + allLog.token);
	}

	public Log test(PrintStream out, String file, boolean printToken) throws Exception {
		out.println("Test File: " + file);
		out.println("Number of predicates: " + factory.getDataAlphabet().size());
		String sample = IOUtil.getFileContenntAsString(file, "UTF-8") ;
		if(sample.trim().length() == 0) return new Log(0, 0, 0);
		Document doc = featuresGenerator.getDocumentReader().read(sample) ;
		TokenCollection[] collection = doc.getTokenCollections() ;
		Log allLog = new Log(0,0,0) ;
		for(int i = 0; i < collection.length; i++) {
			IToken[] token = collection[i].getTokens() ;
			Log log = test(out, file, token, printToken) ;
			allLog.merge(log) ;
		}
		return allLog ;
	}

	public Log test(PrintStream out, String file, IToken[] token, boolean printToken) throws Exception {
		if(token.length == 0) return new Log(0, 0, 0) ;
		TokenFeatures[] tokenFeatures = featuresGenerator.createValueTarget(token) ;
		Instance instance = factory.createDecodeInstance(token) ;

		Sequence input = (Sequence) instance.getData();
		Sequence[] outputs = apply(crf, input, /*nBestOption.value*/ 1);
		for (int a = 0; a < outputs.length; a++) {
			if (outputs[a].size() != input.size()) {
				System.out.println("Failed to decode input sequence " + file + ", answer " + a);
				return new Log(0, 0, 0) ;
			}
		}

		int correctCount = 0 ;
		for (int j = 0; j < input.size(); j++) {
			String target = outputs[0].get(j).toString() ;
			String expectTarget = tokenFeatures[j].getTargetFeature() ; 
			if(target.equals(tokenFeatures[j].getTargetFeature()))  {
				correctCount++ ;
			} else {
				expectTarget += " X" ;
			}
			String[] column = {
					tokenFeatures[j].getFeatures()[0],
					target,
					expectTarget
			};
			if(printToken) print(out, 30, column) ;
		}
		DecimalFormat formater = new DecimalFormat("#.00%") ;
		String ratioStr = formater.format((double)correctCount/tokenFeatures.length) ;
		out.println("Correct ratio: " + correctCount + "/" + tokenFeatures.length + " " + ratioStr);
		out.println("-------------------------------------------------------------------");
		return new Log(token.length, correctCount, token.length - correctCount) ;
	}

	private Sequence[] apply(Transducer model, Sequence input, int nBest) {
		Sequence[] answers;
		if (nBest == 1) {
			answers  = new Sequence[] {model.transduce (input) };
		} else {
			MaxLatticeDefault lattice = new MaxLatticeDefault (model, input, null, /*cache-size*/ 100000);
			answers = lattice.bestOutputSequences(nBest).toArray(new Sequence[0]);
		}
		return answers;
	}

	private void print(PrintStream out, int width, String ... label) {
		for(int i = 0; i < label.length; i++) {
			out.print(label[i]);
			for(int j = label[i].length(); j < width; j++) {
				out.print(' ');
			}
		}
		out.println() ;
	}

	public void test(String[] args) throws Exception {
		command = new CommandParser("crftest:") ;
		command.addMandatoryOption("file", true, "The input test file/directory") ;
		command.addOption("model", true, "The crf model file") ;
		command.addOption("config", true, "The config name") ;
		if(!command.parse(args)) return ;
		command.printHelp() ;

		String crfModel = command.getOption("model", this.crfModel) ;
		String file = command.getOption("file", null) ;
		init(IOUtil.loadRes(crfModel)) ;

		File jfile = new File(file) ;
		if(jfile.isDirectory()) {
			DocumentSet set = new DocumentSet(file, ".*\\.(tagged|wtag)", null) ;
			test(set.getFiles(), false) ;
		} else {
			test(ConsoleUtil.getUTF8SuportOutput(), file, true) ;
		}
	}
	
	static public class Log {
		int token = 0;
		int hit   = 0;
		int miss  = 0;
		
		public Log(int token, int hit, int miss) {
			this.token = token ;
			this.hit   = hit ;
			this.miss  = miss ;
		}
		
		public void merge(Log log) {
			token += log.token ;
			hit   += log.hit ;
			miss  += log.miss ;
		}
	}
}