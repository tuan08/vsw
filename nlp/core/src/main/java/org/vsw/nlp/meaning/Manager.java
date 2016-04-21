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
package org.vsw.nlp.meaning;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public interface Manager<T extends Model> {
	final static public String[] LEXICON_RESOURCES = {
		"classpath:nlp/vietnamese.lexicon.json"
	};
	
	final static public String[] SYNSET_RESOURCES = {
		"classpath:nlp/entity.hint.synset.json"
	};
	
	final static public String[] MEANING_RESOURCES = {
		"classpath:nlp/meaning/vnleader.person.json",
		"classpath:nlp/meaning/vncongty.org.json",
		"classpath:nlp/meaning/vn.person.json",
		"classpath:nlp/meaning/country.json",
		"classpath:nlp/meaning/vn.place.json"
	};
	
	public T[] getModifieds() ;
	public void save(T object) throws Exception ;
	public void remove(T object) throws Exception ;
	public void commit() throws Exception ;
	public Iterator<T> iterator() throws Exception ;
	public void reload() throws Exception ;
	
	static public class Util {
		static public JsonParser getJsonParser(InputStream is) throws JsonParseException, IOException {
			MappingJsonFactory factory = new MappingJsonFactory();
			factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
			JsonParser jp = factory.createJsonParser(is);
			return jp ;
		}
		
		static public JsonGenerator getJsonGenerator(PrintStream out) throws JsonParseException, IOException {
			MappingJsonFactory factory = new MappingJsonFactory();
			factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
			factory.getCodec().getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL) ;
			JsonGenerator generator = factory.createJsonGenerator(new PrintWriter(out)) ;
			DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter() ;
			prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter()) ;
			generator.setPrettyPrinter(prettyPrinter) ;
			return generator ;
		}
	}
}