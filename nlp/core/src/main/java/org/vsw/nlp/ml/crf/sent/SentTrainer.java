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
package org.vsw.nlp.ml.crf.sent;

import org.vsw.nlp.ml.crf.CRFTrainer;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.io.WTagDocumentReader;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class SentTrainer extends CRFTrainer {
	protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
		return newTokenFeaturesGenerator() ;
	}

	static public TokenFeaturesGenerator newTokenFeaturesGenerator() {
		WTagDocumentReader reader = new WTagDocumentReader() ;
		TokenFeaturesGenerator featuresGenerator = new TokenFeaturesGenerator(reader) ;
		featuresGenerator.add(new SentFeatureGenerator()) ;
		featuresGenerator.setTargetFeatureGenerator(new SentTargetFeatureGenerator()) ;
		return featuresGenerator ;
	}
	
  public static void main (String[] args) throws Exception {
  	args = new String[] {
  		"-sample", "d:/vswdata3/nlp/wtag/set1.finish",
  		"-save",   "target/sentence.crf"
  	};
  	SentTrainer trainer = new SentTrainer() ;
  	trainer.train(args) ;
  }
}