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
import org.vsw.nlp.token.tag.PhoneTag;
import org.vsw.nlp.token.tag.WordTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class VNPhoneTokenAnalyzer implements TokenAnalyzer {
	final static String[] IGNORE_TYPE = {WordTag.WLETTER.getOType(), WordTag.KNOWN.getOType()} ;
	
	public IToken[] analyze(IToken[] token) throws TokenException {
		List<IToken> holder = new ArrayList<IToken>() ;
		for(int i = 0; i < token.length; i++) {
			if(token[i].hasTagType(IGNORE_TYPE)) {
				holder.add(token[i]) ;
				continue ;
			}
			
			String norm = token[i].getNormalizeForm() ;
			if(norm.length() == 1 && norm.charAt(0) == '(') {
				if(i + 3 < token.length && 
					 token[i + 1].hasTagType(DigitTag.TYPE) && 
					 token[i + 2].getNormalizeForm().charAt(0) == ')') {
					String test = token[i + 1].getNormalizeForm() + token[i + 3].getNormalizeForm() ;
					test = PhoneNumberUtil.normalize(test) ;
					String areaCode = PhoneNumberUtil.getAreaCode(test) ;
					if(areaCode != null && test.length() >= 10 && test.length() < 12) {
						IToken newToken = new Token(token, i, i + 4) ;
						newToken.add(new PhoneTag(test, "VNPT", "line")) ;
						holder.add(newToken) ;
						i += 3 ;
						continue ;
					}
				}
			} 
			
			if(token[i].hasTagType(DigitTag.TYPE) && norm.length() < 5 && i + 1 < token.length) {
				String test = norm + token[i + 1].getNormalizeForm() ;
				String areaCode = PhoneNumberUtil.getAreaCode(test) ;
				if(areaCode != null) {
					IToken newToken = new Token(token, i, i + 2) ;
					newToken.add(new PhoneTag(test, "VNPT", "line")) ;
					holder.add(newToken) ;
					i += 1 ;
					continue ;
				}
			}
			
			if(norm.length() >= 7 && norm.length() < 15) {
				String string = PhoneNumberUtil.normalize(norm) ;
				if(string.length() == 7 || string.length() == 8) {
					token[i].add(new PhoneTag(string, "VNPT", "line")) ;
					holder.add(token[i]) ;
					continue ;
				}
				
				String areaCode = PhoneNumberUtil.getAreaCode(string) ;
				if(areaCode != null && string.length() < 12) {
					token[i].add(new PhoneTag(string, "VNPT", "line")) ;
					holder.add(token[i]) ;
					continue ;
				}
			}
			holder.add(token[i]) ;
		}
		return holder.toArray(new IToken[holder.size()]);
	}
}