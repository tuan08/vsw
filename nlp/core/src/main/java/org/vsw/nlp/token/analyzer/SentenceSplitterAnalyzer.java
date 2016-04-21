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
package org.vsw.nlp.token.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.CharacterTag;
import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class SentenceSplitterAnalyzer implements TokenCollectionAnalyzer {
	final static public SentenceSplitterAnalyzer INSTANCE = new SentenceSplitterAnalyzer() ;
	
	public TokenCollection[] analyze(IToken[] tokens) throws TokenException {
  	List<TokenCollection> holder = new ArrayList<TokenCollection>() ;
  	int i  = 0, start = 0 ;
  	while(i < tokens.length) {
  		if(isEndSentence(tokens, i)) {
  			TokenCollection collection = new TokenCollection(tokens, start, ++i) ;
  			holder.add(collection) ;
  			start = i ;
  		} else {
  			i++ ;
  		}
  	}
  	if(i > start) {
  		TokenCollection collection = new TokenCollection(tokens, start, i) ;
			holder.add(collection) ;
  	}
  	return holder.toArray(new TokenCollection[holder.size()]);
  }
	
	
	static char[] END_SENT_CHAR = {';', '?', '!'} ;
	boolean isEndSentence(IToken[] token, int pos) {
		if(pos + 1 == token.length) return true ; 
		CharacterTag ctag = (CharacterTag)token[pos].getFirstTagType(CharacterTag.TYPE) ;
		if(ctag == null) return false ;
		char[] characters = token[pos].getNormalizeFormBuf() ;
		if(characters.length == 1) {
			if(CharacterSet.isIn(characters[0], END_SENT_CHAR)) return true ;
			if(characters[0] == '.') {
				String nToken = token[pos + 1].getOriginalForm() ;
				char nextFirstChar = nToken.charAt(0) ;
				if(Character.isUpperCase(nextFirstChar) || Character.isDigit(nextFirstChar)) {
					return true ;
				}
			}
		} else if(characters.length == 3) {
			if("...".equals(token[pos].getNormalizeForm())) return true ;
		}
		return false ;
	}
}