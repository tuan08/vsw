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
package org.vsw.nlp.doc;

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.WordTokenizer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenCollectionAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Section {
	private TokenCollection[] collection ;
	
	public Section(String text) throws TokenException {
		this.collection = 
			new TokenCollection[] {new TokenCollection(new WordTokenizer(text).allTokens()) } ;
	}
	
	public Section(IToken[] token) {
		this.collection = new TokenCollection[] {new TokenCollection(token) } ;
	}
	
	public Section(TokenCollection collection) {
		this.collection = new TokenCollection[] { collection } ;
	}
	
	public Section(TokenCollection[] collection) {
		this.collection = collection  ;
	}
	
	public TokenCollection[] getTokenCollection() { return this.collection ;}
	
	public void analyze(TokenAnalyzer[] analyzer) throws TokenException {
		for(TokenAnalyzer sel : analyzer) {
			for(int i = 0; i < collection.length; i++) {
				collection[i] = new TokenCollection(sel.analyze(collection[i].getTokens())) ;
			}
		}
	}

	public void analyze(TokenAnalyzer analyzer) throws TokenException {
		for(int i = 0; i < collection.length; i++) {
			collection[i] = new TokenCollection(analyzer.analyze(collection[i].getTokens())) ;
		}
	}

	public void analyze(TokenCollectionAnalyzer analyzer) throws TokenException {
		if(collection.length == 0) return ;
		List<TokenCollection> holder = new ArrayList<TokenCollection>() ;
		for(int i = 0; i < collection.length; i++) {
			IToken[] token = collection[i].getTokens() ;
			if(token.length == 0) continue ;
			TokenCollection[] result = analyzer.analyze(collection[i].getTokens()) ;
			for(TokenCollection sel : result) holder.add(sel) ;
		}
		this.collection = holder.toArray(new TokenCollection[holder.size()]) ;
	}
	
	public void getTokens(List<IToken> holder) {
		for(int i = 0; i < collection.length; i++) {
			for(IToken sel : collection[i].getTokens()) {
				holder.add(sel) ;
			}
		}
	}
	
	public void getTokenCollections(List<TokenCollection> holder) {
		for(int i = 0; i < collection.length; i++) {
			holder.add(collection[i]) ;
		}
	}
}
