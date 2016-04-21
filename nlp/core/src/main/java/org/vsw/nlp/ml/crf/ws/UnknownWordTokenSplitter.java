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
package org.vsw.nlp.ml.crf.ws;

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.dict.DictionaryLexicon;
import org.vsw.nlp.dict.DictionaryVNName;
import org.vsw.nlp.dict.Entry;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.Token;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.tag.InfoTag;
import org.vsw.nlp.token.tag.LexiconTag;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class UnknownWordTokenSplitter implements TokenAnalyzer {
	private Dictionaries dictionaries ;
	private DictionaryLexicon dictLexicon ;
	private DictionaryVNName vnnames  ;
	
	public UnknownWordTokenSplitter(NLPResource resource) {
		this.dictionaries = resource.getDictionaries() ;
		this.dictLexicon = dictionaries.getDictionaryLexicon();
		this.vnnames = dictionaries.getDictionaryVNName() ;
	}
	
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		List<IToken> holder = new ArrayList<IToken>() ;
		for(int i = 0; i < tokens.length; i++) {
			LexiconTag ltag = tokens[i].getFirstTagType(LexiconTag.class) ;
			if(ltag != null || tokens[i].getWord().length == 1) {
				holder.add(tokens[i]) ;
			} else {
				List<String> strings = StringUtil.split(tokens[i].getOriginalForm(), ' ') ;
				String[] oword = strings.toArray(new String[strings.size()]) ;
				split(holder, tokens[i], oword) ;
			}
		}
		return holder.toArray(new IToken[holder.size()]) ;
  }
	
	private boolean split(List<IToken> holder, IToken token, String[] oword) {
		int icap = 0, vnlast = 0, vnname = 0 ;
		String[] nword = token.getWord() ;
		for(int i = 0; i < oword.length; i++) {
			if(Character.isUpperCase(oword[i].charAt(0))) {
				icap++  ;
				if(vnnames.containLastName(nword[i], false)) vnlast++ ;
				else if(vnnames.containVNName(nword[i], false)) vnname++ ;
			}
		}
		
		if(icap == nword.length && vnlast + vnname == nword.length) {
			holder.add(token) ;
			return true ;
		}
		
		if(vnname + vnlast > 0) {
			int splitAt = -1 ;
			for(int i = 0; i < oword.length - 1; i++) {
				boolean curr = Character.isUpperCase(oword[i].charAt(0)) ;
				boolean next = Character.isUpperCase(oword[i + 1].charAt(0)) ;
				if(curr != next) {
					splitAt = i ;
					break ;
				}
			}
			
			if(splitAt > 0) {
				IToken newToken = new Token(oword, 0, splitAt + 1) ;
				Entry entry = dictLexicon.getEntry(newToken.getNormalizeForm()) ;
				if(entry != null) newToken.add(entry.getTag()) ;
				newToken.add(new InfoTag("split from " + token.getOriginalForm())) ;
				holder.add(newToken) ;
				
				newToken = new Token(oword, splitAt + 1, oword.length) ;
				entry = dictLexicon.getEntry(newToken.getNormalizeForm()) ;
				if(entry != null) newToken.add(entry.getTag()) ;
				newToken.add(new InfoTag("split from " + token.getOriginalForm())) ;
				holder.add(newToken) ;
				return true ;
			}
		}
		
		//usually non vietnamese lexicon
		for(int i = 0; i < oword.length; i++) {
			IToken newToken = new Token(oword[i]) ;
			Entry entry = dictLexicon.getEntry(newToken.getNormalizeForm()) ;
			if(entry != null) newToken.add(entry.getTag()) ;
			newToken.add(new InfoTag("split from " + token.getOriginalForm())) ;
			holder.add(newToken) ;
		}
		return true  ;
	}
}