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

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.DictionaryLexicon;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.tag.CharacterTag;
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
public class TokenSplitterAnalyzer implements TokenAnalyzer {
	static HashSet<String> ABBRS = new HashSet<String>() ;
  static {
  	ABBRS.add("mr.") ;
  	ABBRS.add("dr.") ;
  	ABBRS.add("prof.") ;
  	ABBRS.add("dep.") ;
  	ABBRS.add("tp.") ;
  	ABBRS.add("n.") ;
  	ABBRS.add("q.") ;
  	ABBRS.add("cty.") ;
  	ABBRS.add("co.") ;
  	ABBRS.add("inc.") ;
  }
  
  private DictionaryLexicon dictLexicon ;
  
  public TokenSplitterAnalyzer(NLPResource resource) {
  	dictLexicon = resource.getDictionaries().getDictionaryLexicon() ;
  }
  
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		List<IToken> holder = new ArrayList<IToken>(tokens.length + 2) ;
  	for(IToken sel : tokens) {
  		CharacterTag ctag = sel.getFirstTagType(CharacterTag.class) ;
  		if(ctag == null) {
  			holder.add(sel) ;
  		} else if(sel.getNormalizeForm().length() == 1) {
  			holder.add(sel) ;
  		} else if(dictLexicon.getEntry(sel.getNormalizeForm()) != null) {
  			holder.add(sel) ;
  		} else {
  			if(ctag.getSuffix() != null) {
  				splitTokenBySuffix(holder, sel, ctag) ;
  			} else {
  				splitTokenByPunc(holder, sel, ctag) ;
  			}
  		}
  	}
  	return holder.toArray(new IToken[holder.size()]);
  }
	
	private void splitTokenBySuffix(List<IToken> holder, IToken token, CharacterTag ctag) {
		String suffix = ctag.getSuffix() ;
		CharacterTag.CharDescriptor digitDesc = ctag.getCharDescriptors('d') ;
		if(digitDesc != null) {
			String oform = token.getOriginalForm() ;
			String number = oform.substring(0, oform.length() - suffix.length()) ;
			suffix = oform.substring(oform.length() - suffix.length()) ;
			Double numberValue = NumberUtil.parseRealNumber(number.toCharArray()); 
			if(numberValue != null) {
				IToken numberToken = new Token(number) ;
				numberToken.add(new NumberTag(numberValue)) ;
				holder.add(numberToken) ;
				Token suffixToken = new Token(suffix) ; 
				suffixToken.add(CommonTokenAnalyzer.createTag(suffixToken.getNormalizeForm(), suffixToken.getOriginalForm())) ;
				holder.add(suffixToken) ;
			} else {
				holder.add(token) ;
			}
		}
	}

	static char[] SPLIT_LAST_CHAR = {':', ',', '!', '?', ';', '…', '-'} ;
	private void splitTokenByPunc(List<IToken> holder, IToken token, CharacterTag ctag) {
		String oform = token.getOriginalForm() ;
		char lastChar = oform.charAt(oform.length() - 1) ;
		char firstChar = oform.charAt(0) ;

		if(oform.length() > 3) {
			if(oform.endsWith("...")) {
				splitToken(holder, oform, oform.length() - 3) ;
				return ;
			}
			
			if(oform.startsWith("...")) {
				splitToken(holder, oform, 3) ;
				return ;
			}
		}
		
		if(CharacterSet.isIn(lastChar, SPLIT_LAST_CHAR)) {
			splitToken(holder, oform, oform.length() -1) ;
			return ;
		}
		
		if(lastChar == '.') {
			String nform = token.getNormalizeForm() ;
			if(oform.length() == 2 && Character.isUpperCase(oform.charAt(0))) {
				holder.add(token) ;
				return ;
			}
			if(ABBRS.contains(nform)) {
				holder.add(token) ;
				return ;
			}
			if(dictLexicon.getEntry(nform) != null) {
				holder.add(token) ;
				return ;
			}
			splitToken(holder, oform, oform.length() -1) ;
			return ;
		}
		
		if(CharacterSet.isIn(firstChar, SPLIT_LAST_CHAR) || firstChar == '.') {
			splitToken(holder, oform, 1) ;
			return ;
		}
		
		if(lastChar == '.') {
			holder.add(token) ;
			return ;
		}
		
		char[] buf = oform.toCharArray() ;
		int digit = 0, letter = 0, dot = 0, comma = 0, dash = 0, other = 0 ;
		for(char c : buf) {
			if(c >= '0' && c <= '9') digit++ ;
			else if(c == '.') dot++ ;
			else if(c == ',')  comma++ ;
			else if(c == '-')  dash++  ;
			else if(Character.isLetter(c)) letter++ ;
			else other++ ;
		}
		
		if(letter > 0) {
			if(dash > 0 && splitByPunctuation(holder, oform, '-')) return ;
			if(comma > 0 && splitByPunctuation(holder, oform, ',')) return ;
			if(dot > 0 && splitByPunctuation(holder, oform, '.')) return ;
		}
		holder.add(token) ;
	}
	
	private boolean splitByPunctuation(List<IToken> holder, String token, char punc) {
		List<String> word = StringUtil.split(token, punc) ;
		int meaningCount = 0;
		for(int i = 0; i < word.size(); i++) {
			String selWord = word.get(i) ;
			if(selWord.length() < 2) continue ;
			if(dictLexicon.getEntry(selWord) != null) meaningCount++ ;
		}
		double ratio = (double)meaningCount/word.size(); 
		if(ratio >= 0.5) {
			int limit  = word.size() ;
			Token puncToken = new Token("" + punc) ;
			puncToken.add(CommonTokenAnalyzer.createTag(puncToken.getNormalizeForm(), puncToken.getOriginalForm())) ;
			for(int i = 0; i < limit; i++) {
				IToken newToken = new Token(word.get(i)) ;
				newToken.add(CommonTokenAnalyzer.createTag(newToken.getNormalizeForm(), newToken.getOriginalForm())) ;
				holder.add(newToken) ;
				if(i + 1 < limit) holder.add(puncToken) ;
			}
			return true ;
		}
		return false ;
	}
	
	private void splitToken(List<IToken> holder, String oform, int pos) {
		String lpart = oform.substring(0, pos) ;
		Token lToken = new Token(lpart) ; 
		lToken.add(CommonTokenAnalyzer.createTag(lToken.getNormalizeForm(), lToken.getOriginalForm())) ;
		holder.add(lToken) ;

		String rpart = oform.substring(pos) ;
		Token rToken = new Token(rpart) ; 
		rToken.add(CommonTokenAnalyzer.createTag(rToken.getNormalizeForm(), rToken.getOriginalForm())) ;
		holder.add(rToken) ;
	}
}