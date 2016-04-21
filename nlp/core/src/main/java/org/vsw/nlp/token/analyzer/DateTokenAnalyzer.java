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

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.DateTag;
import org.vsw.nlp.token.tag.WordTag;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DateTokenAnalyzer implements TokenAnalyzer {
	public IToken[] analyze(IToken[] token) throws TokenException {
		for(int i = 0; i < token.length; i++) {
			if(token[i].hasTagType(WordTag.LETTER)) ;
			analyze(token, i) ;
		}
		return token ;
	}
	
	public void analyze(IToken[] tokens, int position) {
		IToken token = tokens[position] ;
		if(token.hasTagType(WordTag.WLETTER.getOType())) return ;
		char[] buf = token.getNormalizeForm().toCharArray() ;

		if(buf.length > 10) return  ;
		if(buf.length < 6) return ;
		int separatorCounter = 0 ;
		char separator = '\0' ;
		for(int i = 0; i < buf.length; i++) {
			char c = buf[i] ;
			if(Character.isDigit(c)) continue ;
			if(c == '-' || c == '/' || c == '.') {
				if(separator == '\0') separator = c ;
				else if(separator != c) return  ;
				separatorCounter++ ;
				continue ;
			}
		}
		if(separatorCounter != 2) return  ;
		List<String> array = StringUtil.split(buf, separator) ;
		if(array.size() != 3) return  ;

		try {
			short firstNum = Short.parseShort(array.get(0)) ;
			if(firstNum == 0) return  ;
			short secondNum = Short.parseShort(array.get(1)) ;
			if(secondNum == 0) return  ;
			short thirdNum = Short.parseShort(array.get(2)) ;
			if(thirdNum == 0) return  ;

			// YYYY/MM/DD pattern
			if(firstNum > 31 && secondNum <= 12 && thirdNum <= 31) {
				DateTag date = createDate(thirdNum, secondNum, firstNum);
				if(date != null) {
					token.add(date);
					return  ;
				}
			}
			//MM/DD/YYYY pattern
			if(firstNum >= 1 && firstNum <= 12) {
				if(secondNum > 12){
					DateTag date = createDate(secondNum, firstNum, thirdNum);
					if(date != null) {
						token.add(date);
						return  ;
					}
				}
			}
			// default DD/MM/YYYY pattern
			if(firstNum <= 31 && secondNum <= 12) {
				DateTag date = createDate(firstNum, secondNum, thirdNum);
				if(date != null) {
					token.add(date);
					return ;
				}
			}
		} catch (Throwable t) { }
	}
	
	DateTag createDate(short dd, short mm, short yyyy) {
		if(yyyy >= 1 && yyyy <=20) yyyy += 2000 ;
		else if( yyyy >= 90 && yyyy <= 99) yyyy += 1900 ;
		else if( yyyy < 1000  || yyyy > 9999) return null ;
		return new DateTag(dd + "/" + mm + "/" + yyyy) ;
	}
}