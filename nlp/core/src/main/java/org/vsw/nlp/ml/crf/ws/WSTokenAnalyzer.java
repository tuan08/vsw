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

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.WTagInstanceFactory;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

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
public class WSTokenAnalyzer implements TokenAnalyzer {
	private TokenFeaturesGenerator featuresGenerator ;
	private CRF crf ;
	private WTagInstanceFactory factory ;
	
	public WSTokenAnalyzer(NLPResource resource) {
		this(null, resource) ;
	}
	
	public WSTokenAnalyzer(String crfModelFile, NLPResource resource)  {
		if(crfModelFile == null) crfModelFile = "classpath:nlp/vnword.crf" ;
		featuresGenerator = WSTrainer.newTokenFeaturesGenerator(resource) ;
    crf = resource.getObject(crfModelFile) ;
    Alphabet dataAlphabet = crf.getInputAlphabet() ;
    LabelAlphabet labelAlphabet = (LabelAlphabet) crf.getOutputAlphabet() ;
    factory = new WTagInstanceFactory(dataAlphabet, labelAlphabet, featuresGenerator) ;
	}
	
  public IToken[] analyze(IToken[] token) throws TokenException {
  	if(token.length == 0) return token ;
    Instance instance = factory.createDecodeInstance(token) ;
    Sequence input = (Sequence) instance.getData();
    Sequence[] outputs = apply(crf, input, /*nBestOption.value*/ 1);
    for (int a = 0; a < outputs.length; a++) {
    	if (outputs[a].size() != input.size()) {
    		throw new TokenException("Failed to decode input sequence, answer " + a);
    	}
    }
    
    List<IToken> holder = new ArrayList<IToken>() ;
    int i = 0;
    while(i < token.length) {
    	String target = outputs[0].get(i).toString() ;
    	if(WSTargetFeatureGenerator.BWORD.equals(target)) {
    		int start = i ;
    		i++ ;
    		while(i < token.length && 
    				  WSTargetFeatureGenerator.IWORD.equals(outputs[0].get(i).toString())) {
    			i++ ;
    		}
    		if(i - start == 1) {
    			holder.add(token[start]);
    		} else {
    			IToken newToken = new Token(token, start, i) ;
    			holder.add(newToken);
    		}
    	} else {
    		holder.add(token[i]) ;
    		i++ ;
    	}
    }
  	return holder.toArray(new IToken[holder.size()]);
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
