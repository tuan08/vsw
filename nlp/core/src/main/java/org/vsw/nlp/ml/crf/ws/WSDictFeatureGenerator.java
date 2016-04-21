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

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.WordTree;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WSDictFeatureGenerator implements FeatureGenerator {
	private WordTree wtree  ;
	
	public WSDictFeatureGenerator(NLPResource resource) {
		this.wtree = resource.getDictionaries().getDictionaryLexicon().getWordTree() ;
	}
	
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		generate(token, pos,  -4, "p4:",  holder) ;
		generate(token, pos,  -3, "p3:",  holder) ;
		generate(token, pos,  -2, "p2:",  holder) ;
		generate(token, pos,  -1, "p1:",  holder) ;
		generate(token, pos,   0, "p0:" , holder) ;
		generate(token, pos,   1, "n1:" , holder) ;
		generate(token, pos,   2, "n2:" , holder) ;
		generate(token, pos,   3, "n3:" , holder) ;
		generate(token, pos,   4, "n4:" , holder) ;
		holder.addFeature(token[pos].getNormalizeForm()) ;
	}
	
  void generate(IToken[] token, int currPos, int pos, String prefix, FeatureHolder holder) {
		int start = currPos + pos ;
		if(start < 0) return ;
		
		addFeature(token, start, start + 1, prefix, holder) ;
		addFeature(token, start, start + 2, prefix, holder) ;
		addFeature(token, start, start + 3, prefix, holder) ;
		addFeature(token, start, start + 4, prefix, holder) ;
	}
	
	private void addFeature(IToken[] token, int pos, int limit, String prefix, FeatureHolder holder) {
		if(limit > token.length) return ;
		WordTree found = wtree.find(token, pos, limit) ;
		if(found == null) return ;
		String[] word = found.getEntry().getWord() ;
		StringBuilder b = new StringBuilder() ;
		b.append(prefix);
		for(int i = 0; i < word.length; i++) {
			if(i > 0) b.append('+') ;
			b.append(word[i]) ;
		}
		holder.addFeature(b.toString()) ;
	}
}