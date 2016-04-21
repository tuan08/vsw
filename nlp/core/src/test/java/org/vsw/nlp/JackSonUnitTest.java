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
package org.vsw.nlp;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
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
public class JackSonUnitTest {
	@Test
	public void testDeserializer() throws Exception {
		String JSON_OBJECT =
		"{" +
		"  \"name\" : { \"first\" : \"Joe\", \"last\" : \"Sixpack\" }," +
		"  \"gender\" : \"MALE\"," +
		"  \"verified\" : false," +
		"  \"userImage\" : \"Rm9vYmFyIQ==\"" +
	  "}" ;

		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		User user = mapper.readValue(JSON_OBJECT , User.class);
	
		LinkedHashMap<String,Object> userData = mapper.readValue(JSON_OBJECT , LinkedHashMap.class);
		System.out.println("name: " + userData.get("name"));
		
		FileOutputStream os = new FileOutputStream("target/test.jsob") ;
		mapper.writeValue(os, user);
		os.close() ;

		MappingJsonFactory factory = new MappingJsonFactory();
		factory.getCodec().configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true) ;
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonParser jp = 
			factory.createJsonParser(new StringReader(JSON_OBJECT + "\n" + JSON_OBJECT));
		while(jp.nextToken() != null) {
			user = jp.readValueAs(User.class) ;
			System.out.println(user.getName().getFirst());
		}
		
		JsonGenerator generator = factory.createJsonGenerator(new PrintWriter(System.out)) ;
		generator.setPrettyPrinter(new DefaultPrettyPrinter()) ;
		for(int i = 0 ; i < 2; i++) {
			generator.writeObject(user) ;
			generator.writeObject(userData) ;
		}
	}
	
	static public class User {
		public enum Gender { MALE, FEMALE };

		public static class Name {
			private String _first, _last;

			public String getFirst() { return _first; }
			public String getLast() { return _last; }

			public void setFirst(String s) { _first = s; }
			public void setLast(String s) { _last = s; }
		}

		private Gender _gender;
		private Name _name;
		private boolean _isVerified;
		private byte[] _userImage;

		public Name getName() { return _name; }
		public boolean isVerified() { return _isVerified; }
		public Gender getGender() { return _gender; }
		public byte[] getUserImage() { return _userImage; }

		public void setName(Name n) { _name = n; }
		public void setVerified(boolean b) { _isVerified = b; }
		public void setGender(Gender g) { _gender = g; }
		public void setUserImage(byte[] b) { _userImage = b; }
	}
}