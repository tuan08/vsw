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
import java.util.HashSet;
import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.PunctuationTag;
import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class PunctuationTokenAnalyzer implements TokenAnalyzer {
  final static public PunctuationTokenAnalyzer INSTANCE = new PunctuationTokenAnalyzer() ;
	static HashSet<String> ABBRS = new HashSet<String>() ;
  static {
  	ABBRS.add("mr.") ;
  	ABBRS.add("dr.") ;
  	ABBRS.add("prof.") ;
  	ABBRS.add("dep.") ;
  	ABBRS.add("tp.") ;
  	ABBRS.add("n.") ;
  	ABBRS.add("q.") ;
  }
  
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		List<IToken> holder = new ArrayList<IToken>(tokens.length + 25) ;
  	for(IToken sel : tokens) {
  		if(sel instanceof TokenCollection) {
  			holder.add(sel) ;
  			continue ;
  		} 
  		
  		String normForm = sel.getNormalizeForm() ;
  		if(normForm.length() == 0) continue ;

  		if(normForm.length() > 1) {
  			char lastChar = normForm.charAt(normForm.length() -1 ) ;
  			if(lastChar == ',' || lastChar == ':' ||
  				 isSplitEndSentence(lastChar, normForm) && !ABBRS.contains(normForm)) {
  				int to = normForm.length()- 1 ;
  				if(normForm.endsWith("...")) to = normForm.length() - 3 ;
  				String origForm = sel.getOriginalForm() ;
  				holder.add(new Token(origForm.substring(0, to))) ;
  				Token puncToken = new Token(new String(origForm.substring(to))) ;
  				puncToken.add(PunctuationTag.INSTANCE) ;
  				holder.add(puncToken) ;
  			} else {
  				holder.add(sel) ;
  			}
  		} else {
  			char c = normForm.charAt(0) ;
  			if(PunctuationTag.isPunctuation(c)) {
  				sel.add(PunctuationTag.INSTANCE) ;
  				holder.add(sel) ;
  			} else {
  				holder.add(sel) ;
  			}
  		}
  	}
  	return holder.toArray(new IToken[holder.size()]);
  }
	
	private boolean isSplitEndSentence(char lastChar, String normForm) {
		if(!CharacterSet.isIn(lastChar, CharacterSet.END_SENTENCE)) return false ;
		if(lastChar == '.') {
			int limit = 5 ;
			if(normForm.length() - 1 < limit) limit = normForm.length() - 1 ;
			for(int i = 0; i < limit; i++) {
				if(Character.isUpperCase(normForm.charAt(i))) return false ;
			}
		}
		return true ;
	}
}
