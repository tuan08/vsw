package org.vsw.extract.io;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.vsw.extract.Document;

public class JSONReader implements Reader {
	private JsonParser parser ;
	
	public JSONReader(String file) throws Exception {
		this(new FileInputStream(file)) ;
	}
	
	public JSONReader(InputStream in) throws Exception {
		MappingJsonFactory factory = new MappingJsonFactory();
		parser = factory.createJsonParser(new InputStreamReader(in, "UTF-8"));
	}
	
	public Document next() throws Exception {
		if(parser.nextToken() != null) {
			return parser.readValueAs(Document.class) ;
		}
		return null ;
	}
	
	public void close() throws Exception {
		parser.close() ;
	}
}