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
import java.util.List;

import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TokenCollection extends IToken {
	private IToken[] token ;
	
	public TokenCollection(IToken token) {
		this.token = new IToken[] { token } ;
	}
	
	public TokenCollection(IToken[] token) {
		this.token = token ;
	}

	/**
	 * Create a token sequence that is a sub set of token. The token sequence begins at the 
	 * specified from and extends to the token at index to - 1. Thus the length of the 
	 * token sequence is to - from. 
	 */
	public TokenCollection(IToken[] token, int from, int to) {
		this.token = new IToken[to - from] ;
		System.arraycopy(token, from, this.token, 0, this.token.length) ;
	}
	
	public IToken[] getTokens() { return this.token ; }
	
	public IToken[] getSingleTokens() { 
		List<IToken> holder = new ArrayList<IToken>(token.length) ;
		for(int i = 0; i < token.length; i++) {
			if(token[i] instanceof TokenCollection) {
				TokenCollection collection = (TokenCollection) token[i] ;
				for(IToken sel : collection.getTokens()) holder.add(sel) ;
			} else {
				holder.add(token[i]) ;
			}
		}
		return holder.toArray(new IToken[holder.size()]); 
	}
	
	public String[] getWord() { 
		List<String> holder = new ArrayList<String>() ;
		for(int i = 0; i < token.length; i++) {
			for(String sel : token[i].getWord()) holder.add(sel) ;
		}
		return holder.toArray(new String[holder.size()]) ; 
	}
	
	public String getOriginalForm() { 
		return getOriginalForm(0, token.length) ;
	}
	
	public String getOriginalForm(int from, int to) { 
		StringBuilder b = new StringBuilder() ;
		for(int i = from; i < to; i++) {
			String origForm = token[i].getOriginalForm() ;
			if(origForm.length() == 1) {
			  if(b.length() > 0) {
			  	b.append(' ') ;
			  }
			} else {
				if(b.length() > 0) b.append(' ') ;
			}
			b.append(origForm) ;
		}
		return b.toString() ;
	}
	
	public String getSegmentTokens() { 
		return this.getSegmentTokens(0, token.length) ;
	}
	
	public String getSegmentTokens(int from, int to) { 
		StringBuilder b = new StringBuilder() ;
		for(int i = from; i < to; i++) {
			String origForm = token[i].getOriginalForm() ;
			if(b.length() > 0) b.append(" | ") ;
			b.append(origForm) ;
		}
		return b.toString() ;
	}
	
	public String getNormalizeForm() { 
		StringBuilder b = new StringBuilder() ;
		for(int i = 0; i < token.length; i++) {
			String normForm = token[i].getNormalizeForm() ;
			if(normForm.length() == 1) {
				char punc = normForm.charAt(0) ;
			  if(CharacterSet.isIn(punc, CharacterSet.END_SENTENCE)) {
			  } else if(CharacterSet.isIn(punc, CharacterSet.QUOTTATION)) {
			  } else if(b.length() > 0) {
			  	b.append(' ') ;
			  }
			} else {
				if(b.length() > 0) b.append(' ') ;
			}
			b.append(normForm) ;
		}
		return b.toString() ;
	}

	public char[] getNormalizeFormBuf() { 
		return getNormalizeForm().toCharArray() ; 
	}
	
	public void analyze(TokenAnalyzer analyzer) throws TokenException {
		token = analyzer.analyze(token) ;
	}
	
	public void analyze(TokenAnalyzer[] analyzer) throws TokenException {
		for(TokenAnalyzer sel : analyzer) {
			this.token = sel.analyze(this.token) ;
		}
	}
}