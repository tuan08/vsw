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

import org.junit.Test;
import org.vsw.nlp.doc.io.RawDocumentReader;
import org.vsw.nlp.doc.io.RawDocumentSerializer;
import org.vsw.nlp.doc.io.RawDocumentWriter;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class RowDocumentUnitTest {
	@Test
	public void testDeserializer() throws Exception {
		RawDocumentWriter writer = new RawDocumentWriter("target/document.json") ;
		RawDocument doc = new RawDocument() ;
		doc.add("title",   "A title") ;
		doc.add("content", "A Content") ;
		writer.write(doc) ;
		writer.close() ;

		RawDocumentReader reader = new RawDocumentReader("target/document.json") ;
		doc = reader.read() ;
		System.out.println("doc: " + doc) ;
		
		RawDocumentSerializer serializer = new RawDocumentSerializer() ;
		byte[] data = serializer.serialize(doc) ;
		doc = serializer.deserialize(data) ;
		writer = new RawDocumentWriter(System.out) ;
		writer.write(doc) ;
	}
}