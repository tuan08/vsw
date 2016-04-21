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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.SerializationConfig;
import org.vsw.nlp.doc.RawDocument;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class RawDocumentReader {
	private JsonParser parser ;
	
	public RawDocumentReader(String file) throws Exception {
		this(new FileInputStream(file)) ;
	}
	
	public RawDocumentReader(InputStream is) throws Exception {
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.getCodec().configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false) ;
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
		parser = factory.createJsonParser(new BufferedReader(new InputStreamReader(is, "UTF-8")));
	}
	
	public RawDocument read() throws Exception {
		if(parser.nextToken() == null) return null;
		return parser.readValueAs(RawDocument.class) ;
	}
	
	public void close() throws Exception {
		parser.close() ;
	}
}
