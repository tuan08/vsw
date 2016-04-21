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
public class SentenceTokenizer implements TokenSequenceIterator {
	private IToken[] tokens ;
	private int currentPos ;
	
	public SentenceTokenizer(IToken[] tokens) {
		this.tokens = tokens ;
	}
	
  public TokenCollection next() throws TokenException {
  	if(currentPos >= tokens.length) return null ;
  	int from = currentPos ;
  	while(currentPos < tokens.length) {
  		IToken sel = tokens[currentPos++] ;
  		String normForm = sel.getNormalizeForm() ;
  		if(normForm.length() == 1) {
  			if(CharacterSet.isIn(normForm.charAt(0), CharacterSet.END_SENTENCE)) {
  				break ;
  			}
  		}
  	}
  	return new TokenCollection(tokens, from, currentPos);
  }
}
