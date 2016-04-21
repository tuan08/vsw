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

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.WTagInstanceFactory;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenCollectionAnalyzer;

import cc.mallet.fst.CRF;
import cc.mallet.fst.MaxLatticeDefault;
import cc.mallet.fst.Transducer;
import cc.mallet.types.Alphabet;
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
public class CRFSentenceSpliterAnalyzer implements TokenCollectionAnalyzer {
	private TokenFeaturesGenerator featuresGenerator ;
	private CRF crf ;
	private WTagInstanceFactory factory ;
	
	public CRFSentenceSpliterAnalyzer(NLPResource resource) {
		this("nlp/sentence.crf", resource) ;
	}
	
	public CRFSentenceSpliterAnalyzer(String crfModelFile, NLPResource resource) {
		featuresGenerator = SentTrainer.newTokenFeaturesGenerator() ;
    crf = resource.getObject(crfModelFile);
    Alphabet dataAlphabet = crf.getInputAlphabet() ;
    LabelAlphabet labelAlphabet = (LabelAlphabet) crf.getOutputAlphabet() ;
    factory = new WTagInstanceFactory(dataAlphabet, labelAlphabet, featuresGenerator) ;
	}
	
  public TokenCollection[] analyze(IToken[] token) throws TokenException {
  	if(token.length == 0) return new TokenCollection[0] ;
  	Instance instance = factory.createDecodeInstance(token) ;
    Sequence input = (Sequence) instance.getData();
    Sequence[] outputs = apply(crf, input, /*nBestOption.value*/ 1);
    for (int a = 0; a < outputs.length; a++) {
    	if (outputs[a].size() != input.size()) {
    		throw new TokenException("Failed to decode input sequence, answer " + a);
    	}
    }
    List<TokenCollection> holder = new ArrayList<TokenCollection>() ;
    int i = 0, start = 0;
    while(i < token.length) {
    	String target = outputs[0].get(i).toString() ;
    	if(target.equals("eos")) {
    		TokenCollection collection = new TokenCollection(token, start, ++i) ;
    		holder.add(collection) ;
    		start = i ;
    	} else {
    		i++ ;
    	}
    }
    if(start < i) holder.add(new TokenCollection(token, start, i)) ;
  	return holder.toArray(new TokenCollection[holder.size()]);
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
}