package org.vsw.extract.entity;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class JSONWriter<T> {
	private Class<T> classType ;
	private JsonGenerator generator ;
	
	public JSONWriter(String file, Class<T> classType) throws Exception {
		this(new FileOutputStream(file), classType) ;
	}
	
	public JSONWriter(OutputStream out, Class<T> classType) throws Exception {
		this.classType = classType ;
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.getCodec().getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		Writer writer = new OutputStreamWriter(out, "UTF-8") ;
		this.generator = factory.createJsonGenerator(writer) ;

		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentObjectsWith(new DefaultPrettyPrinter.Lf2SpacesIndenter()) ;
		prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter()) ;
		
		MinimalPrettyPrinter mprinter = new MinimalPrettyPrinter() ;
		mprinter.setRootValueSeparator("\n") ;
		
		this.generator.setPrettyPrinter(prettyPrinter) ;
	}
	
	public void write(T object) throws Exception {
		generator.writeObject(object) ;
	}
	
	public void close() throws Exception {
		generator.close() ;
	}
}
