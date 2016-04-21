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
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.NameTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class VNNameTokenAnalyzer implements TokenAnalyzer {
	static private HashSet<String> LASTNAMES  ;
	static {
		String[] LNAME = {
		  "nguyễn", "lý", "trần", "lê", "phạm", "phan", "hồ", "đặng", "bùi",
			"trương", "đỗ", "đào", "hoàng", "vũ", "châu", "mai", "triệu",
			"dương", "trịnh", "đoàn", "đinh", "trình", "lưu"
		} ;
		LASTNAMES = new HashSet<String>(LNAME.length + 10) ;
		for(String sel : LNAME) LASTNAMES.add(sel) ;
	}

	public IToken[] analyze(IToken[] token) throws TokenException {
		List<IToken> holder = new ArrayList<IToken>() ;
		int idx =  0;
		while(idx < token.length) {
			IToken tryToken = token[idx] ;
			String orig = tryToken.getOriginalForm() ;
			if(Character.isLowerCase(orig.charAt(0))) {
				holder.add(tryToken) ;
				idx++ ;
				continue ;
			}
			if(!LASTNAMES.contains(tryToken.getNormalizeForm())) {
				holder.add(tryToken) ;
				idx++ ;
				continue ;
			}
			int limitIdx = idx +1 ;
			while(limitIdx < token.length) {
				IToken nextToken = token[limitIdx] ;
				if(nextToken.getWord().length == 1) {
					String string = nextToken.getOriginalForm() ;
					char fistLetter = string.charAt(0) ;
					if(Character.isUpperCase(fistLetter)) {
						limitIdx++ ;
					} else {
						break ;
					}
				} else {
					break ;
				}
			}
			if(limitIdx - idx > 1) {
				Token newToken = new Token(token, idx, limitIdx) ;
				newToken.add(new NameTag(NameTag.VNNAME, newToken.getOriginalForm())) ;
				holder.add(newToken) ;
				idx = limitIdx ;
			} else {
				holder.add(tryToken) ;
				idx++ ;
			}
		}
		return holder.toArray(new IToken[holder.size()]) ;
  }
}