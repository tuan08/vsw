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

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.EmailTag;
import org.vsw.nlp.token.tag.WordTag;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class EmailTokenAnalyzer implements TokenAnalyzer {
	public IToken[] analyze(IToken[] token) throws TokenException {
		for(int i = 0; i < token.length; i++) {
			if(token[i].hasTagType(WordTag.LETTER)) ;
			analyze(token, i) ;
		}
		return token ;
	}
	
	public void analyze(IToken[] tokens, int pos) {
		if(tokens[pos].hasTagType(WordTag.LETTER)) return  ;
  	IToken token = tokens[pos] ;
  	char[] buf = token.getNormalizeFormBuf() ;
    int atCounter = 0 ;
    int dotCounter = 0 ;
    int lastDotPos = 0;
    for(int i = 0; i < buf.length; i++) {
      char c = buf[i] ;
      if(c == '.') {
      	if(atCounter > 0) {
      		dotCounter++ ;
      		lastDotPos = i ;
      	}
        continue ;
      }
      if(c == '@') {
        atCounter++ ;
        continue ;
      }
      if(c >= 'a' && c <= 'z') continue ;
      if(c >= 'A' && c <= 'Z') continue ;
      if(c >= '0' && c <= '9') continue ;
      if(c == '_' || c == '-') continue ;
      return  ;
    }
    if(atCounter != 1) return ;
    if(dotCounter > 0) {
    	for(int i = lastDotPos + 1; i < buf.length; i++) {
    		char c = buf[i] ;
    		if(c >= 'a' && c <= 'z') continue ;
    		if(c >= 'A' && c <= 'Z') continue ;
    		return  ;
    	}
    }
    token.add(new EmailTag(token.getNormalizeForm())) ;
    return ;
	}
}