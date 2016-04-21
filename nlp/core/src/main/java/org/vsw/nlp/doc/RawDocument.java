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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.vsw.nlp.doc.io.DocumentReader;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class RawDocument extends LinkedHashMap<String, RawField> {
	public void add(String name, String data) {
		put(name, new RawField(name, data)) ;
	}
	
	public void add(RawField field) {
		put(field.getName(), field) ;
	}
	
	public RawField getField(String name) { return get(name) ; }
	
	public String getRawText() {
		StringBuilder b = new StringBuilder() ;
		Iterator<RawField> i = values().iterator() ;
		boolean first = true ;
		while(i.hasNext()) {
			if(!first) b.append("\n\n") ;
			b.append(i.next().toSectionText()) ;
			first = false; 
		}
		return b.toString() ;
	}
	
	public Document toDocument(DocumentReader reader) throws Exception {
		Document doc = new Document() ;
		Iterator<RawField> i = values().iterator() ;
		while(i.hasNext()) {
			RawField rfield = i.next() ;
			Field field = new Field(rfield.getName()) ;
			List<RawSection> rsections = rfield.getSections() ;
			for(int j = 0; j < rsections.size(); j++) {
				field.add(new Section(rsections.get(j).getSection())) ;
			}
			doc.add(field) ;
		}
		reader.process(doc) ;
		return doc ;
	}
}