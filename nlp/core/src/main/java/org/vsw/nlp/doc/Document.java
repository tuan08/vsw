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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class Document {
	private String url ;
	
	private Map<String, Field>  fields = new HashMap<String, Field>() ;

	public String getUrl() { return this.url ; }
	public void   setUrl(String url) { this.url = url ; }
	
	public void add(Field field) { fields.put(field.getName(), field) ; }
	
	public Field getField(String name) { return fields.get(name) ; }
	
	public Map<String, Field> getFields() { return this.fields ; }
	
	public void analyze(TokenAnalyzer[] analyzer) throws TokenException {
		Iterator<Map.Entry<String, Field>> i = fields.entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, Field> entry = i.next() ;
			entry.getValue().analyze(analyzer) ;
		}
	}

	public void analyze(TokenAnalyzer analyzer) throws TokenException {
		Iterator<Map.Entry<String, Field>> i = fields.entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, Field> entry = i.next() ;
			entry.getValue().analyze(analyzer) ;
		}
	}
	
	public void analyze(TokenCollectionAnalyzer analyzer) throws TokenException {
		Iterator<Map.Entry<String, Field>> i = fields.entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, Field> entry = i.next() ;
			entry.getValue().analyze(analyzer) ;
		}
	}
	
	public IToken[] getTokens() {
		List<IToken> holder = new ArrayList<IToken>() ;
		Iterator<Map.Entry<String, Field>> i = fields.entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, Field> entry = i.next() ;
			entry.getValue().getTokens(holder) ;
		}
		return holder.toArray(new IToken[holder.size()]) ;
	}
	
	public TokenCollection[] getTokenCollections() {
		List<TokenCollection> holder = new ArrayList<TokenCollection>() ;
		Iterator<Map.Entry<String, Field>> i = fields.entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, Field> entry = i.next() ;
			entry.getValue().getTokenCollections(holder) ;
		}
		return holder.toArray(new TokenCollection[holder.size()]) ;
	}
}