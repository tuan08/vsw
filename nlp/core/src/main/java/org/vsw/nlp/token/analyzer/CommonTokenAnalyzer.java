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
import org.vsw.nlp.token.UnitAbbreviation;
import org.vsw.nlp.token.tag.CharacterTag;
import org.vsw.nlp.token.tag.DigitTag;
import org.vsw.nlp.token.tag.EmailTag;
import org.vsw.nlp.token.tag.NumberTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.token.tag.URLTag;
import org.vsw.nlp.token.tag.WordTag;
import org.vsw.nlp.token.util.NumberUtil;
import org.vsw.nlp.util.VietnameseUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CommonTokenAnalyzer implements TokenAnalyzer {
	final static public CommonTokenAnalyzer INSTANCE = new CommonTokenAnalyzer() ;
	
	static private String[] suffix ;
	static {
		suffix = UnitAbbreviation.getUnits(UnitAbbreviation.ALL) ;
	}

	public IToken[] analyze(IToken[] tokens) throws TokenException {
		List<IToken> holder = new ArrayList<IToken>(tokens.length + 25) ;
		for(IToken sel : tokens) {
			if(sel.getNormalizeForm().length() == 0) continue ;
			if(sel instanceof TokenCollection) {
				holder.add(sel) ;
			} else {
				sel.add(createTag(sel.getNormalizeForm(), sel.getOriginalForm())) ;
				holder.add(sel) ;
			}
		}
		return holder.toArray(new IToken[holder.size()]);
	}

	static public Tag createTag(String nform, String oform) {
		char[] characters = nform.toCharArray();
		byte viletter = 0, letter = 0, digit = 0, colon = 0, dot = 0, 
		comma = 0, fslash = 0, bslash = 0, dash = 0, percent = 0, at = 0, nline = 0, other = 0 ;
		for (char sel : characters) {
			if(sel >= 'a' && sel <= 'z') letter++;
			else if (sel >= '0' && sel <= '9') digit++;
			else if (sel == '.')  dot++;
			else if (sel == ',')  comma++;
			else if (sel == ':')  colon++;
			else if (sel == '%')  percent++;
			else if (sel == '/')  fslash++;
			else if (sel == '-')  dash++;
			else if (sel == '@')  at++;
			else if (sel == '\\') bslash++;
			else if (sel == '\n') nline++;
			else if (VietnameseUtil.isVietnameseCharacter(sel)) viletter++;
			else if (Character.isLetter(sel)) letter++;
			else other++ ;
		}
		int letterCount  = (letter + viletter) ;
		if(digit == characters.length) {
			return new DigitTag(nform) ;
		} else if(letterCount == characters.length) {
			return WordTag.WLETTER ;
		} else if(digit + dot + comma == characters.length && 
				      Character.isDigit(characters[characters.length - 1])) {
			Double value = NumberUtil.parseRealNumber(characters) ;
			if(value != null) return new NumberTag(value) ;
		} else if(dot > 0) {
			boolean endWithKnowDomain = URLTag.isEndWithKnownDomain(nform) ;
			if(endWithKnowDomain && at == 1) return new EmailTag(oform) ;
			if(endWithKnowDomain) return new URLTag(oform) ;
			if(URLTag.isStartWithKnownProtocol(nform)) return new URLTag(oform) ;
		} 
		
		List<CharacterTag.CharDescriptor> holder = new ArrayList<CharacterTag.CharDescriptor>() ;
		if(letterCount > 0) holder.add(new CharacterTag.CharDescriptor('l', (byte)letterCount)) ;
		if(viletter > 0) holder.add(new CharacterTag.CharDescriptor('V', viletter)) ;
		if(digit > 0) holder.add(new CharacterTag.CharDescriptor('d', digit)) ;
		if(nline > 0) holder.add(new CharacterTag.CharDescriptor('n', nline)) ;
		if(dot > 0)  holder.add(new CharacterTag.CharDescriptor('.', dot)) ;
		if(comma > 0)  holder.add(new CharacterTag.CharDescriptor(',', comma)) ;
		if(colon > 0)  holder.add(new CharacterTag.CharDescriptor(':', colon)) ;
		if(percent > 0) holder.add(new CharacterTag.CharDescriptor('%', percent)) ;
		if(fslash > 0) holder.add(new CharacterTag.CharDescriptor('/', fslash)) ;
		if(dash > 0)  holder.add(new CharacterTag.CharDescriptor('-', dash)) ;
		if(bslash > 0) holder.add(new CharacterTag.CharDescriptor('\\', bslash)) ;

		String foundSuffix = null ;
		if(digit > 0) {
			for(String selSuffix : suffix) {
				if(nform.endsWith(selSuffix)) {
					foundSuffix = selSuffix ;
					break ;
				}
			}
		}
    return new CharacterTag(holder, foundSuffix) ;
	}
}