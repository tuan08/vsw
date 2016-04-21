package org.vsw.nlp.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

public class QueryData {
	private Map<String, QueryDataField> fields ;
	
	public QueryData() {
		fields = new HashMap<String, QueryDataField>() ;
	}
	
	public void add(QueryDataField field) throws TokenException {
		fields.put(field.getName(), field) ;
	}
	
	public void add(String name, String data) throws TokenException {
		fields.put(name, new QueryDataField(name, data)) ;
	}
	
	public void add(String name, String data, TokenAnalyzer[] analyzer) throws TokenException {
		fields.put(name, new QueryDataField(name, data, analyzer)) ;
	}
	
	public QueryDataField getDocumentField(String name) {
		return fields.get(name) ;
	}
	
	public Map<String, QueryDataField> getDocumentFields() { return this.fields ; }

	public String toString() {
		StringBuilder b = new StringBuilder() ;
		Iterator<QueryDataField> i = fields.values().iterator() ;
		while(i.hasNext()) {
			QueryDataField field = i.next() ;
			b.append(field.toString()).append("\n") ;
		}
		return b.toString() ;
	}
}