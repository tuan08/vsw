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
package org.vsw.nlp.index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class VNTokenizer {
	private DocumentReader reader ;
	private HashSet<String> filterWords ;
	
	public VNTokenizer(DocumentReader reader, String[] filterWord) {
		this.reader = reader ;
		this.filterWords = new HashSet<String>() ;
		for(String sel : filterWord) filterWords.add(sel.toLowerCase()) ;
	}
	
	public String[] split(String text) throws TokenException {
		if(text == null) return null ;
		IToken[] token = reader.read(text).getTokens() ;
		List<String> holder = new ArrayList<String>() ;
		for(int i = 0; i < token.length; i++) {
			String nform = token[i].getNormalizeForm() ;
			if(filterWords.contains(nform)) continue ;
			holder.add(nform) ;
		}
		return holder.toArray(new String[holder.size()]) ;
	}
	
	public String[] split(IToken[] token) throws TokenException {
		if(token == null) return null ;
		List<String> tholder = new ArrayList<String>() ;
		for(int i = 0; i < token.length; i++) {
			tholder.add(token[i].getNormalizeForm()) ;
		}
		return tholder.toArray(new String[token.length]) ;
	}
}