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
package org.vsw.nlp.doc.io;

import java.util.Iterator;
import java.util.List;

import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.Field;
import org.vsw.nlp.doc.RawDocument;
import org.vsw.nlp.doc.RawField;
import org.vsw.nlp.doc.RawSection;
import org.vsw.nlp.doc.Section;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class RawDocumentDocumentIterator {
	private DocumentReader reader ;
	private RawDocumentReader rawDocReader ;
	
	public RawDocumentDocumentIterator(DocumentReader reader, String file) throws Exception {
		this.reader = reader ;
		this.rawDocReader = new RawDocumentReader(file) ;
	}
	
	public Document next() throws Exception {
		RawDocument rdoc = rawDocReader.read() ;
		if(rdoc == null) {
			rawDocReader.close() ;
			return null ;
		}
		Document doc = new Document() ;
		Iterator<RawField> i = rdoc.values().iterator() ;
		while(i.hasNext()) {
			RawField rfield = i.next() ;
			List<RawSection> sections = rfield.getSections() ;
			Field field = new Field(rfield.getName()) ;
			for(int k = 0; k < sections.size(); k++) {
				RawSection rsection = sections.get(k) ;
				field.add(new Section(rsection.getSection())) ;
			}
			doc.add(field) ;
		}
		reader.process(doc) ;
		return doc ;
	}
	
}