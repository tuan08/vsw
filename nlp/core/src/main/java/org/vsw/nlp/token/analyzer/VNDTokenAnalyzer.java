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
import org.vsw.nlp.token.tag.CurrencyTag;
import org.vsw.nlp.token.tag.DigitTag;
import org.vsw.nlp.token.tag.NumberTag;
import org.vsw.nlp.token.util.NumberUtil;
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
public class VNDTokenAnalyzer implements TokenAnalyzer {
	final static public String[] VND_TOKENS =   { 
		"vnd", "đồng", "dong", "đ", "đ/m2", "đ/km", "₫", "₫/m2", "₫/km"
	} ;
	final static public String[] NGAN_TOKENS =  { "ngàn", "ngan" } ;
	final static public String[] TRIEU_TOKENS = { "triệu", "trieu", "tr" } ;
	final static public String[] TY_TOKENS =    { "tỷ", "ty" } ;
	
	final static public String[][] ALL_TOKENS = {VND_TOKENS, NGAN_TOKENS, TRIEU_TOKENS, TY_TOKENS} ;
	
	public IToken[] analyze(IToken[] token) throws TokenException {
		for(int i = 0; i < token.length; i++) {
			if(detectByNumberToken(token, i)) continue ;
			else if(detectByUnknownToken(token, i)) continue ;
		}
		return token;
	}

	private boolean detectByNumberToken(IToken[] token, int pos) {
		double amount = getNumberValue(token[pos]) ;
		if(amount > 0) {
			if(pos + 1 >= token.length) return false ;
			String nextToken = token[pos + 1].getNormalizeForm() ;
			if(nextToken.length() == 1 && CharacterSet.isIn(nextToken.charAt(0), CharacterSet.BRACKET)) {
				if(pos + 2 >= token.length) return false ;
				nextToken = token[pos + 2].getNormalizeForm() ;
			}
			setCurrencyTag(token[pos], amount, nextToken) ;
			return true ;
		}
		return false; 
	}
	
	private boolean detectByUnknownToken(IToken[] token, int pos) {
		String norm = token[pos].getNormalizeForm() ;
		for(int i = 0; i < ALL_TOKENS.length; i++) {
			for(int j = 0; j < ALL_TOKENS[i].length; j++) {
				if(norm.endsWith(ALL_TOKENS[i][j])) {
					String number = norm.substring(0, norm.length() - ALL_TOKENS[i][j].length()) ;
					Double value = NumberUtil.parseRealNumber(number.toCharArray()) ;
					if(value == null) return false ;
					setCurrencyTag(token[pos], value, ALL_TOKENS[i][j]) ;
					return true ;
				}
			}
		}
		return false; 
	}
	
	private double getNumberValue(IToken token) {
		DigitTag digitTag = (DigitTag) token.getFirstTagType(DigitTag.TYPE) ;
		if(digitTag != null) {
			Long value = digitTag.getLongValue() ;
			if(value == null) return -1d ;
			return value.doubleValue() ;
		}
	  NumberTag number = (NumberTag) token.getFirstTagType(NumberTag.TYPE) ;
	  if(number != null) return number.getValue() ;
	  return -1d ;
	}
	
	private void setCurrencyTag(IToken token, double amount, String unit) {
		if(StringUtil.isIn(unit, VND_TOKENS)) token.add(new CurrencyTag(amount , "vnd")) ;
		else if(StringUtil.isIn(unit, NGAN_TOKENS)) token.add(new CurrencyTag(amount * 1000, "vnd")) ;
		else if(StringUtil.isIn(unit, TRIEU_TOKENS)) token.add(new CurrencyTag(amount * 1000000, "vnd")) ;
		else if(StringUtil.isIn(unit, TY_TOKENS)) token.add(new CurrencyTag(amount * 1000000000, "vnd")) ;
	}
}