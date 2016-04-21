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
package org.vsw.nlp.ml.dict;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.DictionaryLexicon;
import org.vsw.nlp.dict.DictionaryVNName;
import org.vsw.nlp.dict.Entry;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.tag.WordTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DictionaryTaggingAnalyzer implements TokenAnalyzer {
	private DictionaryLexicon dictLexicon ;
	private DictionaryVNName vnnames  ;
	
	public DictionaryTaggingAnalyzer(NLPResource resource) {
		this.dictLexicon = resource.getDictionaries().getDictionaryLexicon();
		this.vnnames = resource.getDictionaries().getDictionaryVNName() ;
	}
	
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		for(int i = 0; i < tokens.length; i++) {
			Entry entry = dictLexicon.getEntry(tokens[i].getNormalizeForm()) ;
			if(entry != null) {
				tokens[i].removeTagType(WordTag.class) ;
				tokens[i].add(entry.getTag()) ;
			}
		}
		return tokens ;
  }
}