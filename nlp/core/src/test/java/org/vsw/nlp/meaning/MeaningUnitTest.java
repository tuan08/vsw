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

import java.io.ByteArrayOutputStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.junit.Test;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class MeaningUnitTest {
	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		ObjectWriter writer = mapper.prettyPrintingWriter(new DefaultPrettyPrinter()) ;
		
		Entity meaning = new Entity() ;
		meaning.setName("Headvances") ;
		meaning.setType(new String[] {"công ty"}) ;
		meaning.setVariant(new String[] {"headvances.com"}) ;
		meaning.put("created", "21/9/2007") ;
		meaning.put("founder", new String[] { "Nguyễn Tuấn Anh", "Nguyễn Anh Tiến"}) ;
		ByteArrayOutputStream os = new ByteArrayOutputStream() ;
		writer.writeValue(os, meaning) ;
		
		String data = new String(os.toByteArray()) ;
		System.out.println(data);
	
		Entity dobject = mapper.readValue(data, Entity.class);
		
		os = new ByteArrayOutputStream() ;
		writer.writeValue(os, dobject) ;
		System.out.println("..............................................") ;
		System.out.println(new String(os.toByteArray()));
	}
}