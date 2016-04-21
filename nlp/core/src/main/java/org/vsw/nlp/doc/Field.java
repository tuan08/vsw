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
public class Field {
	private String   name ;
	private List<Section> sections = new ArrayList<Section>(5);

	public Field(String name) {
		this.name = name ;
	}
	
	public String getName() { return this.name ; }
	
	public void add(Section section) {
		sections.add(section) ;
	}
	
	public void add(TokenCollection collection) {
		sections.add(new Section(collection)) ;
	}
	
	public List<Section> getSections() { return sections ; }
	
	public void analyze(TokenAnalyzer[] analyzer) throws TokenException {
		for(int i = 0; i < sections.size(); i++) {
			sections.get(i).analyze(analyzer) ;
		}
	}

	public void analyze(TokenAnalyzer analyzer) throws TokenException {
		for(int i = 0; i < sections.size(); i++) {
			sections.get(i).analyze(analyzer) ;
		}
	}
	
	public void analyze(TokenCollectionAnalyzer analyzer) throws TokenException {
		for(int i = 0; i < sections.size(); i++) {
			sections.get(i).analyze(analyzer) ;
		}
	}
	
	public void getTokens(List<IToken> holder) {
		for(int i = 0; i < sections.size(); i++) {
			sections.get(i).getTokens(holder) ;
		}
	}
	
	public void getTokenCollections(List<TokenCollection> holder) {
		for(int i = 0; i < sections.size(); i++) {
			sections.get(i).getTokenCollections(holder) ;
		}
	}
}
