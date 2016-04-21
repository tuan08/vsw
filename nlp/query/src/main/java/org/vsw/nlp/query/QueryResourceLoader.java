package org.vsw.nlp.query;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.vsw.nlp.dict.Dictionaries;

public class QueryResourceLoader {
	private Dictionaries dict ;
	private List<Query> queries ;
	
	public QueryResourceLoader() throws Exception {
		this.dict = new Dictionaries() ;
		this.queries = new ArrayList<Query>() ;
	}
	
	public Dictionaries getDictionaries() { return this.dict ; }
	
	public List<Query> getQueries() { return this.queries ; }

	public Query[] getQueryArray() {
		return queries.toArray(new Query[queries.size()]) ;
	}
	
	public void load(String path, String type) throws IOException {
		if(path.startsWith("classpath:")) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
			load(cl.getResourceAsStream(path.substring("classpath:".length())), type) ;
		} else if(path.startsWith("file:")) {
			load(new FileInputStream(path.substring("file:".length())), type) ;
		} else {
		  load(new FileInputStream(path), type) ;
		}
	}

  public void load(InputStream is, String type) throws IOException {
  	JsonParser parser = Dictionaries.getJsonParser(is) ;
  	while(parser.nextToken() != null) {
  		if("lexicon".equals(type)) {
  		} else if("place".equals(type)) {
  		} else if("synset".equals(type)) {
  		} else if("product".equals(type)) {
  		} else {
  			throw new IOException("Unknown type " + type) ;
  		}
  	}
  	is.close() ;
  }
  
  public void load(String file) throws Exception {
  	load(new FileInputStream(file)) ;
  }
  
  public void load(InputStream is) throws Exception {
  	JsonParser parser = Dictionaries.getJsonParser(is) ;
  	ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
  	while(parser.nextToken() != null) {
  		JsonNode node = parser.readValueAsTree() ;
  		String type = node.get("otype").getTextValue() ;
  		if("query".equals(type)) {
  			Query query = mapper.readValue(node , Query.class);
  			query.compile() ;
  			queries.add(query) ;
  		} else {
  			throw new IOException("Unknown type " + type) ;
  		}
  	}
  	is.close() ;
  }
}