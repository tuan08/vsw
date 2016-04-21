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
package org.vsw.nlp.ml.crf.per;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.ml.crf.CRFTrainer;
import org.vsw.nlp.ml.crf.CapLetterFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.ent.EntityTargetFeatureGenerator;
import org.vsw.nlp.ml.crf.per.NGramFeatureGenerator;

public class PersonTrainer extends CRFTrainer {
  protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
    String configName = command.getOption("config", "np") ;
    PersonSetConfig config = PersonSetConfig.getConfig(configName) ;
    return newTokenFeaturesGenerator(NLPResource.getInstance(), config) ;
  }

  static public TokenFeaturesGenerator newTokenFeaturesGenerator(NLPResource resource, PersonSetConfig config) throws Exception {
    TokenFeaturesGenerator featuresGenerator = newTokenFeaturesGenerator(config) ;
    if(resource == null) resource = NLPResource.getInstance(); 
    WTagDocumentReader reader = new WTagDocumentReader(resource);
    featuresGenerator.setDocumentReader(reader);
    return featuresGenerator ;
  }
  
  static public TokenFeaturesGenerator newTokenFeaturesGenerator(PersonSetConfig config) throws Exception {
    TokenFeaturesGenerator featuresGenerator = new TokenFeaturesGenerator() ;
    featuresGenerator.add(new CapLetterFeatureGenerator()) ;
    featuresGenerator.add(new NameDictFeatureGenerator()) ;
    featuresGenerator.add(new NGramFeatureGenerator()) ;
    featuresGenerator.setTargetFeatureGenerator(new EntityTargetFeatureGenerator(config)) ;
    return featuresGenerator ;
  } 
	
  public static void main (String[] args) throws Exception {
  	args = new String[] {
  		"-sample", "src/test/java/resources/PERtagged",
  		"-save",   "src/main/java/models/per.crf"
  	};
  	PersonTrainer trainer = new PersonTrainer() ;
  	trainer.train(args) ;
  }
}