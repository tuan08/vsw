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
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.DigitTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class GroupTokenMergerAnalyzer implements TokenAnalyzer {
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		int i = 0 ;
		List<IToken> holder = new ArrayList<IToken>() ;
		while(i < tokens.length) {
			if(tokens[i].hasTagType(DigitTag.TYPE)) {
				int limit = i  ;
				while(limit < tokens.length && limit < i + 8 && tokens[limit].hasTagType(DigitTag.TYPE)) {
					limit++ ;
				}
				if(limit > i + 1) {
					IToken newToken = new Token(tokens, i, limit) ;
					holder.add(newToken);
					i = limit ;
					continue ;
				}
			} 
			holder.add(tokens[i]) ;
			i++ ;
		}
		return holder.toArray(new IToken[holder.size()]) ;
	}
}