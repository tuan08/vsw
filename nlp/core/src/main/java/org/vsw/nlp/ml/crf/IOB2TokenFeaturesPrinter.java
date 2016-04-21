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

import java.io.PrintStream;

import org.vsw.nlp.doc.Document;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class IOB2TokenFeaturesPrinter {
	private PrintStream out ;
	private TokenFeaturesGenerator featuresGenerator ;
	
	public IOB2TokenFeaturesPrinter(PrintStream out, TokenFeaturesGenerator featuresGenerator) {
		this.out = out ;
		this.featuresGenerator = featuresGenerator ;
	}
	
	public void print(TokenCollection[] collection, boolean target) {
		for(int i = 0; i < collection.length; i++) {
			print(collection[i].getTokens(), target) ;
			if(i + 1 < collection.length) out.println() ;
		}
	}
	
	public void print(IToken[] token, boolean target) {
		TokenFeatures[] tokenFeatures = featuresGenerator.generate(token, target) ;
		for(int i = 0; i < tokenFeatures.length; i++) {
			String[] feature = tokenFeatures[i].getFeatures();
			for(int j = 0; j < feature.length; j++) {
				if(j > 0) out.append(' ') ;
				out.append(feature[j]) ;
			}
			out.append(' ').append(tokenFeatures[i].getTargetFeature()).println() ;
		}
	}
	
  public void print(Document doc, boolean target) {
  	print(doc.getTokenCollections(), target) ;
	}
}
