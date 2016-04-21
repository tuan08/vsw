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
package org.vsw.nlp.dict;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.junit.Test;
import org.vsw.nlp.meaning.Entity;
import org.vsw.nlp.topic.Place;
import org.vsw.util.StringUtil;

public class PlaceConverter {
	@Test
	public void run() throws Exception {
		FileInputStream is = new FileInputStream("src/data/nlp/vn.place.json") ;
		JsonParser parser = getJsonParser(is) ;
		int idx = 0 ;
		Context context = new Context() ;
		PrintStream out = new PrintStream(new FileOutputStream("vn.place.json"),true, "UTF-8") ;
		
		MappingJsonFactory factory = new MappingJsonFactory();
		//factory.getCodec().configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true) ;
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonGenerator generator = factory.createJsonGenerator(new PrintWriter(out)) ;
		generator.setPrettyPrinter(new DefaultPrettyPrinter()) ;
		
		HashSet<String> names = new HashSet<String>() ;
		while(parser.nextToken() != null) {
  		Place place = parser.readValueAs(Place.class) ;
  		context.set(place) ;
  		Entity meaning = new Entity() ;
  		meaning.setOType("place") ;
  		meaning.setUDescription(context.mudesc) ;
  		meaning.setName(context.mname) ;
  		meaning.setTopic(context.mtopic) ;
  		generator.writeObject(meaning) ;
  		
  		if(context.personName != null) names.add(context.personName) ;
  		idx++ ;
  	}
  	out.close() ;
  	
  	PrintStream perOut = new PrintStream(new FileOutputStream("vn.person.json"),true, "UTF-8") ;
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonGenerator perGenerator = factory.createJsonGenerator(new PrintWriter(perOut)) ;
		perGenerator.setPrettyPrinter(new DefaultPrettyPrinter()) ;
		Iterator<String> i = names.iterator() ;
		while(i.hasNext()) {
			Entity meaning = new Entity() ;
			meaning.setOType("person") ;
			meaning.setName(i.next()) ;
			perGenerator.writeObject(meaning) ;
		}
  	perOut.close() ;
	}
	
	static JsonParser getJsonParser(InputStream is) throws JsonParseException, IOException {
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonParser jp = factory.createJsonParser(is);
		return jp ;
	}
	
	static class Context {
		static private HashSet<String> LASTNAMES  ;
		static {
			String[] LNAME = {
			  "nguyễn", "lý", "trần", "lê", "phạm", "phan", "hồ", "đặng", "bùi",
				"trương", "đỗ", "đào", "hoàng", "vũ", "châu", "mai", "triệu",
				"dương", "trịnh", "đoàn", "đinh", "trình", "lưu"
			} ;
			LASTNAMES = new HashSet<String>(LNAME.length + 10) ;
			for(String sel : LNAME) LASTNAMES.add(sel) ;
		}
		
		private Entry   province ;
		private Entry   city ;
		private Entry   district ;
		private Entry   quarter ;
		private Entry   street ;
		private Entry   name ;
		
		String mudesc ;
		String mname ;
		String mtopic ;

		String  personName ;
		
		Context() throws IOException {
		}
		
		public void set(Place place) {
			province = create("tỉnh", place.getProvince()) ;
			city =     create(city, place.getCityType(), place.getCity()) ;
			district = create(district, place.getDistrictType(), place.getDistrict()) ;
			quarter  = create(quarter, place.getQuarterType(), place.getQuarter()) ;
			street   = create(street, place.getStreetType(), place.getStreet()) ;
			name   = create(name, place.getNameType(), place.getName()) ;
			StringBuilder b = new StringBuilder() ;
			add(b, street) ;
			add(b, quarter) ;
			add(b, district) ;
			add(b, city) ;
			add(b, province) ;
			if(b.length() > 0) b.append(", ") ;
			b.append("Việt Nam") ;
			
			this.mname = null ; this.mtopic = null; this.mudesc = null ;
			this.personName = null;
			String pname = name.toString() ;
			String uname = name.name ;
			if(name.prefix != null) uname = pname.substring(name.prefix.length() + 1) ;
			if(!LASTNAMES.contains(uname.split(" ")[0].toLowerCase())) {
				this.mname = name.format(uname);
			} else {
				personName = uname ;
			}
		  if(this.mname == null) this.mtopic = pname ;
			
			mudesc = pname + " thuộc " + b.toString() ;
		}
		
		void add(StringBuilder b, Entry entry) {
			if(entry == null) return ;
			if(name.equals(entry)) return ;
			if(b.length() > 0) b.append(", ") ;
			b.append(entry.toString()) ;
		}
		
		private Entry create(String prefix, String value) {
			if(value == null) return null ;
			return new Entry(prefix, value) ;
		}
		
		private Entry create(Entry old, String prefix, String value) {
			if(value == null) return null ;
			if(prefix == null && old != null) {
				if(old.name.equals(value)) prefix = old.prefix ;
			}
			return new Entry(prefix, value) ;
		}
	}
	
	static class Entry {
		String prefix ;
		String name; 
		
		Entry(String prefix, String name) {
			this.prefix = prefix ;
			this.name   = name ;
		}
		
		public boolean equals(Entry other) {
			if(prefix != null && other.prefix != null) {
				if(!prefix.equals(other.prefix)) return false ;
			}
			return name.equals(name) ;
		}
		
		static String[] IGNORE_WITH_PREFIX = {
			"đường", "phố", "phường", "xã", "thôn", "quận", "huyện", "thị trấn", "thành phố", "thị xã",
			"tập thể", "làng"
		} ;
		public String toString() { 
			if(prefix == null) return format(name) ;
			if(name.startsWith(prefix)) return format(name) ;
			for(String sel : IGNORE_WITH_PREFIX) {
				if(name.startsWith(sel)) return format(name) ;
			}
			return prefix + " " + format(name) ;
		}
		
		private String format(String name) {
			String[] word = name.split(" ") ;
			for(int i = 0; i < word.length; i++) {
				if(i == 0 && StringUtil.isIn(word[i], IGNORE_WITH_PREFIX)) continue ;
				if(word[i].length() == 0) continue ;
				char c = word[i].charAt(0) ;
				c = Character.toUpperCase(c) ;
				word[i] = c + word[i].substring(1) ;
			}
			name = StringUtil.joinStringArray(word, " ") ;
			name = name.replace("Thành Phố", "thành phố") ;
			name = name.replace("Thị Xã", "thị xã") ;
			name = name.replace("Thị Trấn", "thị trấn") ;
			return name  ;
		}
	}
}
