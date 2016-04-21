package org.vsw.extract.io;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vsw.extract.Document;

public class MultiFileJSONWriter {
	private Map<String, JSONWriter> map = new HashMap<String, JSONWriter>() ;

	public void write(String file, Document doc) throws Exception {
		JSONWriter serializer = map.get(file) ;
		if(serializer == null) {
			serializer = new JSONWriter(file) ;
			map.put(file, serializer);
		}
		serializer.write(doc) ;
	}
	
	public void close() throws Exception {
		Iterator<JSONWriter> i = map.values().iterator() ;
		while(i.hasNext()) i.next().close() ;
	}
} 