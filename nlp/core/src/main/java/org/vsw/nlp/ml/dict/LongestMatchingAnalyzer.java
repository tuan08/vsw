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

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.dict.Entry;
import org.vsw.nlp.dict.WordTree;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.tag.PunctuationTag;
import org.vsw.nlp.token.tag.WordTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class LongestMatchingAnalyzer implements TokenAnalyzer {
	private Dictionaries dict ;
	
	public LongestMatchingAnalyzer(Dictionaries dict) {
		this.dict = dict ;
	}
	
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		List<IToken> newList = new ArrayList<IToken>() ;
		int position = 0 ;
		WordTree root = dict.getDictionaryLexicon().getWordTree() ;
		while(position < tokens.length) {
			IToken token = tokens[position] ;
			if(token.hasTagType(PunctuationTag.TYPE)){
				newList.add(token) ;
				position++ ;
				continue ;
			} 
			WordTree foundTree = root.matches(tokens, position) ;
			if(foundTree != null) {
				Entry entry = foundTree.getEntry() ;
				int newPosition = position + entry.getWord().length ;
				Token newToken = new Token(tokens, position, newPosition) ;
				newToken.add(entry.getTag()) ;
				newToken.add(WordTag.KNOWN) ;
				newList.add(newToken) ;
				position = newPosition ;
			} else {
				newList.add(token) ;
				position++ ;
			}
		}
		return newList.toArray(new Token[newList.size()]) ;
  }
}