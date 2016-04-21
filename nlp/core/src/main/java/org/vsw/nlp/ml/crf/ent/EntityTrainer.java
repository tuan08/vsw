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
package org.vsw.nlp.ml.crf.ent;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.ml.crf.CRFTrainer;
import org.vsw.nlp.ml.crf.CapLetterFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
import org.vsw.nlp.ml.crf.ws.UnknownWordTokenSplitter;
import org.vsw.nlp.ml.dict.DictionaryTaggingAnalyzer;
import org.vsw.nlp.ml.io.WTagDocumentReader;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class EntityTrainer extends CRFTrainer {
	protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
		String configName = command.getOption("config", "np") ;
		EntitySetConfig config = EntitySetConfig.getConfig(configName) ;
		return newTokenFeaturesGenerator(NLPResource.getInstance(), config) ;
	}

	static public TokenFeaturesGenerator newTokenFeaturesGenerator(NLPResource resource, EntitySetConfig config) throws Exception {
		TokenFeaturesGenerator featuresGenerator = createTokenFeaturesGenerator(resource, config) ;
		final SentenceFilter filter = new SentenceFilter(config) ;
		final TokenAnalyzer posAnalyzer = new POSTokenAnalyzer(resource) ;
		WTagDocumentReader reader = new WTagDocumentReader(resource) {
			protected void onPostAnalyzeSentence(Document doc) throws TokenException { 
				doc.analyze(filter) ;
			}
			protected void onPostAnalyzeWord(Document doc) throws TokenException { 
				doc.analyze(posAnalyzer) ;
			}
		};
		featuresGenerator.setDocumentReader(reader);
		return featuresGenerator ;
	}
	
	static public TokenFeaturesGenerator createTokenFeaturesGenerator(NLPResource resource, EntitySetConfig config) throws Exception {
		TokenFeaturesGenerator featuresGenerator = new TokenFeaturesGenerator() ;
		featuresGenerator.add(new TokenFeatureGenerator()) ;
		featuresGenerator.add(new CapLetterFeatureGenerator()) ;
		featuresGenerator.add(new MeaningFeatureGenerator(resource, config)) ;
		featuresGenerator.setTargetFeatureGenerator(new EntityTargetFeatureGenerator(config)) ;
		return featuresGenerator ;
	}
	
	static public DocumentReader createEntityTagger(NLPResource resource) throws Exception {
		final TokenAnalyzer[] wanalyzer = new TokenAnalyzer[] {
			new DictionaryTaggingAnalyzer(resource),
			new UnknownWordTokenSplitter(resource),
			new POSTokenAnalyzer(resource),
		  new NPEntityTokenAnalyzer(resource),
			new NumEntityTokenAnalyzer(resource)
		};
		DocumentReader reader = new DefaultDocumentReader(resource) {
			protected void onPostAnalyzeWord(Document doc) throws TokenException { 
				doc.analyze(wanalyzer) ;
			}
		};
		return reader ;
	}
	
  public static void main (String[] args) throws Exception {
  	args = new String[] {
  		"-sample", "src/test/resources/tagged",
  		"-save",   "target/entity.crf"
  	};
  	EntityTrainer trainer = new EntityTrainer() ;
  	trainer.train(args) ;
  }
}