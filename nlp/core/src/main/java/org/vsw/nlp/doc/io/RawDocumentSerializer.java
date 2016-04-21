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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.vsw.nlp.doc.RawDocument;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class RawDocumentSerializer {
	private ObjectMapper mapper ;
	
	public RawDocumentSerializer() {
		mapper = new ObjectMapper(); // can reuse, share globally
		mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false) ;
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL) ;
	}
	
	public byte[] serialize(RawDocument rdoc) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream() ;
		mapper.writeValue(os, rdoc);
		os.close() ;
		return os.toByteArray() ;
	}
	
	public RawDocument deserialize(byte[] data) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(data) ;
		return mapper.readValue(is , RawDocument.class);
	}
}
