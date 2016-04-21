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
package org.vsw.nlp.topic.storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.MappingJsonFactory;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class InputStreamTopicIterator implements TopicIterator {
	private JsonParser parser ;
	
	public InputStreamTopicIterator(String file) throws IOException {
		this(new FileInputStream(file)) ;
	}
	
  public InputStreamTopicIterator(InputStream is) throws IOException {
  	MappingJsonFactory factory = new MappingJsonFactory();
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		parser = factory.createJsonParser(is);
	}
	
  public <T> T next(Class<T> type) throws JsonProcessingException, IOException {
  	if(parser.nextToken() == null) return null ;
  	return parser.readValueAs(type);
  }
  
  public JsonNode next() throws JsonProcessingException, IOException {
  	if(parser.nextToken() == null) return null ;
  	return parser.readValueAsTree();
  }
}