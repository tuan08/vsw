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
package org.vsw.nlp.token.util;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NumberUtil {
	final static public java.lang.Double parseRealNumber(char[] buf) {
		if(buf == null  || buf.length == 0 || !Character.isDigit(buf[0])) return null ;
		int fractionPosition = -1 ;
		int dotCounter = 0, commaCounter = 0;
		int digitCount = 0 ;
		for(int i = 0; i < buf.length; i++) {
			char c = buf[i] ;
			if(c == '.') {
				fractionPosition = i ; dotCounter++ ;
				continue ;
			} else if(c == ',') {
				fractionPosition = i ; commaCounter++ ;
				continue ;
			} else if(c >= '0' && c <= '9') {
				digitCount++ ; continue ;
			}
			return null ;
		}
		if(digitCount == 0) return null ;
		if(dotCounter > 1 && commaCounter > 1) return null ;
		String string = new String(buf) ;
		if(fractionPosition < 0) return java.lang.Double.parseDouble(string) ;
		if(string.endsWith("000")) fractionPosition = -1 ;
		if((commaCounter > 0 && dotCounter == 0) || (commaCounter == 0 && dotCounter > 0)) {
			if(string.length() - fractionPosition < 5) {
				if(string.endsWith(".00") && string.length() < 5) fractionPosition = -1 ;
				else if(string.endsWith(",00") && string.length() < 5) fractionPosition = -1 ;
				else if(string.length() - fractionPosition == 4) fractionPosition = -1 ;
			}
		}
		StringBuilder b = new StringBuilder() ;
		for(int i = 0; i < buf.length; i++) {
			char c = buf[i] ;
			if(Character.isDigit(c)) {
				b.append(c) ;
			} else {
				if(i == fractionPosition) b.append(".") ;
			}
		}
		if(b.length() == 0) return null ;
		try {
			return java.lang.Double.parseDouble(b.toString()) ;
		} catch(Throwable t) {
			System.out.println("Cannot parse " + new String(buf));
			t.printStackTrace() ;
			return null ;
		}
	}
}
