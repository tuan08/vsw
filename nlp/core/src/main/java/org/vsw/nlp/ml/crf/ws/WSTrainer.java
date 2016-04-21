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
package org.vsw.nlp.ml.crf.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.ml.crf.CRFTrainer;
import org.vsw.nlp.ml.crf.CapLetterFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeatureGenerator;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.sent.CRFSentenceSpliterAnalyzer;
import org.vsw.nlp.ml.io.WTagDocumentReader;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenCollectionAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WSTrainer extends CRFTrainer {
	protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
		TokenFeaturesGenerator featuresGenerator = newTokenFeaturesGenerator() ;
		return featuresGenerator ;
	}

	static public TokenFeaturesGenerator newTokenFeaturesGenerator() throws Exception {
		return newTokenFeaturesGenerator(NLPResource.getInstance()) ;
	}
	
	static public TokenFeaturesGenerator newTokenFeaturesGenerator(NLPResource resource) {
		final TokenCollectionAnalyzer sentenceFilter = new TokenCollectionAnalyzer() {
			public TokenCollection[] analyze(IToken[] tokens) throws TokenException {
				if(tokens.length > 5) {
					return new TokenCollection[] { new TokenCollection(tokens)};
				}
				System.out.println("Remove not wellformed sentence: " + new TokenCollection(tokens).getOriginalForm());
				return new TokenCollection[] {};
			}
		};
		WTagDocumentReader reader = new WTagDocumentReader() {
			protected void onPostAnalyzeSentence(Document doc) throws TokenException { 
				doc.analyze(sentenceFilter) ;
			}
		};
		reader.setSentenceSpliter(new CRFSentenceSpliterAnalyzer(resource));
		reader.setWordAnalyzer(WS_ANALYZER) ;
		
		TokenFeaturesGenerator featuresGenerator = new TokenFeaturesGenerator(reader) ;
		featuresGenerator.add(new TokenFeatureGenerator()) ;
		featuresGenerator.add(new CapLetterFeatureGenerator()) ;
		featuresGenerator.add(new WSDictFeatureGenerator(resource)) ;
		featuresGenerator.setTargetFeatureGenerator(new WSTargetFeatureGenerator()) ;
		return featuresGenerator ;
	}
	
	static Pattern SPLITER = Pattern.compile(" ") ;
	static public WSTokenAnalyzer WS_ANALYZER = new WSTokenAnalyzer() ;
	
	static class WSTokenAnalyzer implements TokenAnalyzer {
    public IToken[] analyze(IToken[] token) throws TokenException {
    	List<IToken> holder = new ArrayList<IToken>() ;
  		BoundaryTag BW = new BoundaryTag(WSTargetFeatureGenerator.BWORD) ;
  		BoundaryTag IW = new BoundaryTag(WSTargetFeatureGenerator.IWORD) ;
  		for(int i = 0; i < token.length; i++) {
  			IToken sel = token[i] ;
  			if(sel.getWord().length == 1) {
  				sel.removeTagType(BoundaryTag.class) ;
  				sel.add(BW) ;
  				holder.add(sel) ;
  			} else {
  				String oform = sel.getOriginalForm() ;
  				String[] word = SPLITER.split(oform) ;
  				for(int j = 0; j < word.length; j++) {
  					if(word[j].length() == 0) continue ;
  					IToken newToken = new Token(word[j]) ;
  					newToken.add(CommonTokenAnalyzer.createTag(newToken.getNormalizeForm(), newToken.getOriginalForm())) ;
  					if(j == 0) newToken.add(BW) ;
  					else       newToken.add(IW) ;
  					holder.add(newToken) ;
  				}
  			}
  		}
  		return holder.toArray(new IToken[holder.size()]) ;
    }
	}
	
  public static void main (String[] args) throws Exception {
  	args = new String[] {
  		//"-sample", "src/test/resources/sample.tagged",
  		"-sample", "vlsptrain/tagged/100067.tagged",
  		"-save",   "target/vnword-train.crf"
  	};
  	WSTrainer trainer = new WSTrainer() ;
  	trainer.train(args) ;
  }
}