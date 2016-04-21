package org.vsw.extract.rdf;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import org.vsw.extract.Document;
import org.vsw.nlp.query.QueryContext;

public class PlaceRDFWriter implements RDFWriter {
	private PrintStream out ;
	
	public PlaceRDFWriter(String file) throws Exception {
		out = new PrintStream(new FileOutputStream(file), true, "UTF-8") ;
		out.print("@prefix dc:        <http://purl.org/dc/elements/1.1/> .\n") ;
		out.print("\n");
		out.print("@prefix place:    <http://vsw.org/place/> .\n") ;
		out.print("\n");
	}
	
  public void write(Document document, QueryContext context) throws Exception {
  	Map<String, String[]> attrs = context.getAttributes("place:") ;
  	if(attrs.size() < 1) return ;
  	String[] titles = attrs.remove("place:title") ;
  	String title = document.getTitle() ;
  	if(titles != null) title = titles[0] ;
  	
  	StringBuilder b = new StringBuilder() ;
  	for(char sel : title.toCharArray()) {
  		if(sel == ' ' || sel == ')') continue ;
  		if(sel == '(') b.append('-') ;
  		else b.append(sel) ;
  	}
  	out.append("place:").append(b.toString()).append('\n');
  	out.append("  ").append("wikipedia:title").append(":  \"").append(title).append("\" ;\n") ;
  	out.append("  ").append("wikipedia:url").append(":  \"").append(document.getUrl()).append("\" ;\n") ;
  	Iterator<Map.Entry<String, String[]>> i = attrs.entrySet().iterator() ;
  	while(i.hasNext()) {
  		Map.Entry<String, String[]> entry = i.next() ;
  		String key = entry.getKey() ;
  		String pname = key.substring("place:".length()) ;
  		out.append("  ").append(pname).append(":  \"").append(entry.getValue()[0]).append("\" ;\n") ;
  	}
  	out.print("  .\n") ;
  }

  public void close() throws Exception {
  	out.close() ;
  }
}