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

import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TokenFeaturesGenerator {
	protected FeatureGenerator[] fgenerator ;
	protected FeatureGenerator   targetFeatureGenrator ;
	private DocumentReader reader ;

	public TokenFeaturesGenerator() {} 

	public TokenFeaturesGenerator(DocumentReader reader) {
		this.reader = reader ;
	}

	public DocumentReader getDocumentReader() { return this.reader ; }
	public void setDocumentReader(DocumentReader reader) { this.reader = reader ; }

	public void add(FeatureGenerator fgenerator) {
		if(this.fgenerator == null) {
			this.fgenerator = new FeatureGenerator[] {fgenerator} ;
		} else {
			FeatureGenerator[] temp = new FeatureGenerator[this.fgenerator.length + 1] ;
			System.arraycopy(this.fgenerator, 0, temp, 0, this.fgenerator.length) ;
			temp[this.fgenerator.length] = fgenerator ;
			this.fgenerator = temp ;
		}
	}

	public void setTargetFeatureGenerator(FeatureGenerator fgenerator) {
		this.targetFeatureGenrator = fgenerator ;
	}

	public TokenFeatures[] generate(IToken[] token, boolean generateTarget) {
		TokenFeatures[] tokenFeatures = new TokenFeatures[token.length] ;
		FeatureHolder featureHolder = new FeatureHolder() ;
		for(int i = 0; i < token.length; i++) {
			if(ignoreToken(token[i])) {
				onIgnoreToken(token[i], featureHolder, generateTarget) ;
			} else {
				for(int j = 0; j < fgenerator.length; j++) {
					fgenerator[j].generate(token, i, featureHolder) ;
				}
				if(generateTarget && this.targetFeatureGenrator != null) {
					this.targetFeatureGenrator.generate(token, i, featureHolder) ;
				}
			}
			tokenFeatures[i] = new TokenFeatures(featureHolder.getFeatures(), featureHolder.getTargetFeature()) ;
			featureHolder.reset() ;
		}
		return tokenFeatures ;
	}

	public TokenFeatures[] createValueTarget(IToken[] token) {
		TokenFeatures[] tfeatures = new TokenFeatures[token.length] ;
		FeatureHolder featureHolder = new FeatureHolder() ;
		for(int i = 0; i < token.length; i++) {
			this.targetFeatureGenrator.generate(token, i, featureHolder) ;
			tfeatures[i] = new TokenFeatures(new String[]{token[i].getOriginalForm()}, featureHolder.getTargetFeature()) ;
			featureHolder.reset() ;
		}
		return tfeatures ;
	}

	protected void onIgnoreToken(IToken token, FeatureHolder featureHolder,  boolean target) {
		featureHolder.addFeature("token:ignore") ;
		if(target && this.targetFeatureGenrator != null) {
			featureHolder.addFeature("O") ;
		}
	}
	
	protected boolean ignoreToken(IToken token) { return false ; }
}
