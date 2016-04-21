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
package org.vsw.nlp.token;

import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class PhraseIterator implements TokenSequenceIterator {
	private IToken[] tokens ;
	private int currentPos ;
	
	public PhraseIterator(IToken[] tokens) {
		this.tokens = tokens ;
	}
	
  public TokenCollection next() throws TokenException {
  	if(currentPos >= tokens.length) return null ;
  	int from = currentPos, count = 0 ;
  	while(currentPos < tokens.length) {
  		IToken sel = tokens[currentPos++] ;
  		String normForm = sel.getNormalizeForm() ;
  		if(normForm.length() == 1) {
  			char punc = normForm.charAt(0) ;
  			if(CharacterSet.isIn(punc, CharacterSet.END_SENTENCE)) {
  				break ;
  			}
  			if(count > 15 && (punc == ',' || CharacterSet.isIn(punc, CharacterSet.QUOTTATION))) {
  				break ;
  			}
  			if(count > 50) break ; 
  		}
  		count++ ;
  	}
  	return new TokenCollection(tokens, from, currentPos);
  }
}
