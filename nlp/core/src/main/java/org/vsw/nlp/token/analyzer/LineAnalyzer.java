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
import org.vsw.nlp.util.CharacterSet;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class LineAnalyzer implements TokenCollectionAnalyzer {
	final static public LineAnalyzer INSTANCE = new LineAnalyzer() ;
	
  public TokenCollection[] analyze(IToken[] tokens) throws TokenException {
  	List<TokenCollection> holder = new ArrayList<TokenCollection>() ;
  	int i  = 0, start = 0 ;
  	while(i < tokens.length) {
  		char[] buf = tokens[i].getNormalizeFormBuf() ;
  		if(buf.length == 1 && CharacterSet.isIn(buf[0], CharacterSet.NEW_LINE)) {
  			if(i > start) {
  				TokenCollection collection = new TokenCollection(tokens, start, i) ;
  				holder.add(collection) ;
  			}
  			i++ ;
  			while(i < tokens.length) {
  				buf = tokens[i].getNormalizeFormBuf() ;
  	  		if(buf.length != 1 && !CharacterSet.isIn(buf[0], CharacterSet.NEW_LINE)) {
  	  			break ;
  	  		}
  	  		i++ ;
  			}
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
}