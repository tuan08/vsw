package org.vsw.sample.ws;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelSequence;
/**
 * This implementation won't support for the large dataset
 * @author tuan
 */
public class TokenFeaturesPipe extends Pipe {
	public TokenFeaturesPipe () {
		super(new Alphabet(), new LabelAlphabet());
	}

	public Instance pipe(Instance carrier) {
		if(isTargetProcessing()) {
			return pipeTrain(carrier) ;
		} else {
		  return pipeDecode(carrier);
		}
	}

	public Instance pipeTrain(Instance carrier) {
		Object inputData = carrier.getData();
		TokenFeatures[] tokens = TokenFeatures.tokenize((String)inputData);

		FeatureVector[] fvs = new FeatureVector[tokens.length];
		LabelAlphabet labels = (LabelAlphabet)getTargetAlphabet();
		LabelSequence target = new LabelSequence(labels, tokens.length);
		Alphabet dataAlphabetFeatures = getDataAlphabet();
		for (int l = 0; l < tokens.length; l++) {
			target.add(tokens[l].targetFeature);
			fvs[l] = tokens[l].getFeatureVector(dataAlphabetFeatures, true);
		}
		carrier.setData(new FeatureVectorSequence(fvs));
		carrier.setTarget(target);
		return carrier;
	}	
	
	public Instance pipeDecode(Instance carrier) {
		Object inputData = carrier.getData();
		TokenFeatures[] tokens = TokenFeatures.tokenize((String)inputData);

		FeatureVector[] fvs = new FeatureVector[tokens.length];
		Alphabet dataAlphabetFeatures = getDataAlphabet();
		for (int l = 0; l < tokens.length; l++) {
			fvs[l] = tokens[l].getFeatureVector(dataAlphabetFeatures, false);
		}
		carrier.setData(new FeatureVectorSequence(fvs));
		carrier.setTarget(new LabelSequence(getTargetAlphabet()));
		return carrier;
	}
}