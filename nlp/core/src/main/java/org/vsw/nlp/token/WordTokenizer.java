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

import java.util.ArrayList;

import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WordTokenizer implements TokenIterator {
	static char[] DEFAULT_SEPARATOR = 
		CharacterSet.merge(CharacterSet.BLANK, CharacterSet.BRACKET, 
                       CharacterSet.NEW_LINE, CharacterSet.QUOTTATION, 
                       new char[] {'|'}) ;
	static char[] DEFAULT_IGNORE_SEPARATOR = 
		CharacterSet.merge(CharacterSet.BLANK, new char[]{'\f', '\r', '\0', 13 }) ;
	
	private char[] separator ;
	private char[] ignoreSeparator ;
	
	private char[] buf ;
	private int currPos ;
	
	public WordTokenizer(String string) {
		this.buf = string.toCharArray() ;
		this.separator = DEFAULT_SEPARATOR ;
		this.ignoreSeparator = DEFAULT_IGNORE_SEPARATOR ;
	}
	
	public WordTokenizer(String string, char[] separator, char[] ignoreChar) {
		this.buf = string.toCharArray() ;
		this.separator = separator ;
		this.ignoreSeparator = ignoreChar ;
	}
	
	public IToken next() throws TokenException {
	  if(currPos >= buf.length) return null ;
		while(currPos < buf.length && CharacterSet.isIn(buf[currPos], ignoreSeparator)) {
	  	currPos++ ;
	  }
		if(currPos >= buf.length) return null ;
		if(CharacterSet.isIn(buf[currPos], separator)) {
			String word = new String(new char[] {buf[currPos]}) ;
			currPos++ ;
			return new Token(word) ;
		}
		int startPos = currPos ;
	  while(currPos < buf.length) {
	  	if(CharacterSet.isIn(buf[currPos], separator)) {
	  		break ;
	  	} else {
	  		currPos++ ;
	  	}
	  }
	  char[] sub = new char[currPos - startPos] ;
	  System.arraycopy(buf, startPos, sub, 0, sub.length) ;
		return new Token(new String(sub));
  }
	
	public IToken[] allTokens() throws TokenException {
		ArrayList<IToken> holder = new ArrayList<IToken>() ;
		IToken token = null ;
		while((token = next()) != null) holder.add(token) ;
		return holder.toArray(new IToken[holder.size()]) ;
	}
}
