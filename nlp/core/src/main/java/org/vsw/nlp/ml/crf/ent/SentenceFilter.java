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

import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.analyzer.TokenCollectionAnalyzer;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class SentenceFilter implements TokenCollectionAnalyzer {
	static TokenCollection[] EMPTY = {} ;
	
	private String[] selFeature ;
	
	public SentenceFilter(EntitySetConfig config) {
		selFeature = config.tagPrefix ;
	}
	
	public TokenCollection[] analyze(IToken[] tokens) {
	  if(hasTagFeature(tokens)) {
	  	return new TokenCollection[] { new TokenCollection(tokens) } ;
	  } else {
	  	if(Math.random() < 0.2d) {
	  		return new TokenCollection[] { new TokenCollection(tokens) } ;
	  	}
		  return EMPTY ;
	  }
  }
	
	boolean hasTagFeature(IToken[] tokens) {
		for(int i = 0; i < tokens.length; i++) {
			BoundaryTag btag = tokens[i].getFirstTagType(BoundaryTag.class) ;
			if(btag == null) continue ;
			String[] feature = btag.getFeatures() ;
			for(String selFeature : this.selFeature) {
				for(int j = 0; j < feature.length; j++) {
					if(feature[j].startsWith(selFeature)) return true ;
				}
			}
		}
		return false ;
	}
}
