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
package org.vsw.nlp.ml;

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.TokenIterator;
import org.vsw.nlp.util.CharacterSet;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WTagTokenizer implements TokenIterator {
	static char[] SEPARATOR = CharacterSet.merge(CharacterSet.BLANK, new char[]{'\f', '\r', '\0', 13 }) ;
	
	private char[] separator ;
	private char[] ignoreSeparator ;
	
	private char[] buf ;
	private int currPos ;
	
	public WTagTokenizer(String string) {
		this.buf = string.toCharArray() ;
		this.separator = SEPARATOR ;
		this.ignoreSeparator = SEPARATOR ;
	}
	
	public IToken[] allTokens() throws TokenException {
		ArrayList<IToken> holder = new ArrayList<IToken>() ;
		IToken token = null ;
		while((token = next()) != null) holder.add(token) ;
		return holder.toArray(new IToken[holder.size()]) ;
	}
	
	public IToken next() throws TokenException {
	  if(currPos >= buf.length) return null ;
		while(currPos < buf.length && CharacterSet.isIn(buf[currPos], ignoreSeparator)) {
	  	currPos++ ;
	  }
		if(currPos >= buf.length) return null ;
		
		if(CharacterSet.isIn(buf[currPos], separator) || buf[currPos] == '\n') {
			String word = new String(new char[] {buf[currPos]}) ;
			currPos++ ;
			Token ret = new Token(word) ;
			ret.add(new BoundaryTag(StringUtil.EMPTY_ARRAY));
			return ret ;
		}
		int startPos = currPos ;
	  while(currPos < buf.length) {
	  	if(buf[currPos] == ':') {
	  		int nextPos = currPos + 1;
	  		if(nextPos < buf.length && buf[nextPos] == '{') {
	  			break ;
	  		} 
	  	}
	  	currPos++ ;
	  }
		char[] sub = new char[currPos - startPos] ;
	  System.arraycopy(buf, startPos, sub, 0, sub.length) ;
		Token ret = new Token(new String(sub), true);
		ret.add(getBoundaryTag()) ;
		return ret ;
	}
	
	private BoundaryTag getBoundaryTag() {
		int start = currPos, end = buf.length ;
		while(currPos < buf.length) {
			if(buf[currPos] == '{') {
				start = currPos + 1;
			} else if(buf[currPos] == '}') {
				end = currPos ;
				break ;
			}
			currPos++ ;
		}
		currPos++ ;
		char[] sub = new char[end - start] ;
	  System.arraycopy(buf, start, sub, 0, sub.length) ;
		List<String> params = StringUtil.split(sub, ',') ;
		return new BoundaryTag(params.toArray(new String[params.size()])) ;
	}
}