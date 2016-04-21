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
package org.vsw.knowledge.ie.candidate;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.codehaus.jackson.JsonGenerator;
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
public class CandidatesWriter {
	private JsonGenerator generator ;
	
	public CandidatesWriter(String file) throws Exception {
		PrintStream out = new PrintStream(new FileOutputStream(file), true, "UTF-8") ;
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		factory.getCodec().getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL) ;
		generator = factory.createJsonGenerator(new PrintWriter(out)) ;
		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter() ;
		prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter()) ;
		generator.setPrettyPrinter(prettyPrinter) ;
	}
	
	public void write(Candidates candidate) throws Exception {
		generator.writeObject(candidate) ;
	}
	
	public void close() throws Exception {
		generator.close() ;
	}
}
