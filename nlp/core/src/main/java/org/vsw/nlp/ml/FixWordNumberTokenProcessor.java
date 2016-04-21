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
package org.vsw.nlp.ml;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class FixWordNumberTokenProcessor extends TokenProcessor {
	public FixWordNumberTokenProcessor(String file, boolean saveChange) throws Exception  {
		super(file, saveChange) ;
	}
	
	protected IToken[] process(IToken[] token, int pos) {
		IToken sel =  token[pos] ;
		if(sel.getWord().length == 1) return new IToken[] { sel } ;
		boolean word = false, dateOrNumber = false ;
		for(String selWord : sel.getWord()) {
			if(isCandidateWord(selWord)) word = true ;
			if(isDateOrNumber(selWord)) dateOrNumber = true; 
		}
		if(word && dateOrNumber) {
			String[] oform = sel.getOriginalForm().split(" ") ;
			IToken[] newToken = new IToken[oform.length] ;
			for(int i = 0; i < oform.length; i++) {
				newToken[i] = new Token(oform[i]) ;
				newToken[i].add(new BoundaryTag(StringUtil.EMPTY_ARRAY)) ;
			}
			System.out.println("Fix: " + sel.getOriginalForm());
			return newToken ;
		}
		return new IToken[] { sel } ;
	}

	static String[] WORDS = {"ngày", "tháng", "năm", "chục", "trăm", "ngàn", "triệu", "tỷ", "tỉ"} ;
	boolean isCandidateWord(String string) {
		return StringUtil.isIn(string, WORDS) ;
	}
	
	boolean isDateOrNumber(String string) {
		int digit = 0, other = 0 ;
		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i) ;
			if(Character.isDigit(c)) {
				digit++ ;
			} else if(c == '-' || c  == '.' || c == '/' || c == ',') {
				other++ ;
			} else {
				return false ;
			}
		}
		return true ;
	}
	
	static public void main(String[] args) throws Exception {
		FixWordNumberTokenProcessor inspector = 
			new FixWordNumberTokenProcessor("d:/vswdata2/nlp/wtag/set27", true) ;
		inspector.process() ;
	}
}