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

import java.io.Serializable;

import org.vsw.nlp.token.IToken;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WTagInstanceFactory  implements Serializable {
	private LabelAlphabet targetLabelAlphabet ;
	private Alphabet dataAlphabet ;
	private TokenFeaturesGenerator featuresGenerator ;
	
	public WTagInstanceFactory (TokenFeaturesGenerator featuresGenerator) {
		this.dataAlphabet = new Alphabet() ;
		this.targetLabelAlphabet = new LabelAlphabet() ;
		this.featuresGenerator = featuresGenerator ;
	}
	
	public WTagInstanceFactory(Alphabet dataAlphabet, LabelAlphabet targetLabelAlphabet, 
			                         TokenFeaturesGenerator featuresGenerator) {
		this.dataAlphabet = dataAlphabet ;
		this.targetLabelAlphabet = targetLabelAlphabet ;
		this.featuresGenerator = featuresGenerator ;
	}
	
	public Alphabet getDataAlphabet() { return this.dataAlphabet ; }
	
	public Alphabet getTargetAlphabet() { return this.targetLabelAlphabet ; }
	
	public Instance createTrainInstance(IToken[] token) {
		TokenFeatures[] tokenFeatures = featuresGenerator.generate(token, true) ;
		FeatureVector[] fvs = new FeatureVector[tokenFeatures.length];
		LabelSequence target = new LabelSequence(targetLabelAlphabet, tokenFeatures.length);
		for (int l = 0; l < tokenFeatures.length; l++) {
			target.add(tokenFeatures[l].getTargetFeature());
			fvs[l] = tokenFeatures[l].getFeatureVector(dataAlphabet, true);
		}
		Instance carrier = new Instance(null, null, null, null) ;
		carrier.setData(new FeatureVectorSequence(fvs));
		carrier.setTarget(target);
		return carrier;
	}
	
	public Instance createDecodeInstance(IToken[] token) {
		TokenFeatures[] tokenFeatures = featuresGenerator.generate(token, false) ;
		FeatureVector[] fvs = new FeatureVector[tokenFeatures.length];
		for (int l = 0; l < tokenFeatures.length; l++) {
			fvs[l] = tokenFeatures[l].getFeatureVector(dataAlphabet, false);
		}
		Instance carrier = new Instance(null, null, null, null) ;
		carrier.setData(new FeatureVectorSequence(fvs));
		carrier.setTarget(new LabelSequence(targetLabelAlphabet));
		return carrier;
	}
}