package org.vsw.extract.io;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.vsw.extract.Document;

public class JSONWriter {
	private JsonGenerator generator ;
	
	public JSONWriter(String file) throws Exception {
		this(new FileOutputStream(file)) ;
	}
	
	public JSONWriter(OutputStream out) throws Exception {
		MappingJsonFactory factory = new MappingJsonFactory();
		Writer writer = new OutputStreamWriter(out, "UTF-8") ;
		this.generator = factory.createJsonGenerator(writer) ;

		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter()) ;
		this.generator.setPrettyPrinter(prettyPrinter) ;
	}
	
	public void write(Document object) throws Exception {
		generator.writeObject(object) ;
	}
	
	public void close() throws Exception {
		generator.close() ;
	}
}
