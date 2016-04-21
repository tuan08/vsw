package org.vsw.extract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.query.QueryDataField;
import org.vsw.nlp.query.QueryDataIterator;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.StringUtil;

public class SectionIterator implements QueryDataIterator {
	private Iterator<QueryData> iterator ;
	
	public SectionIterator(Document doc, TokenAnalyzer[] analyzer) throws Exception {
		this((String[])null, doc, analyzer) ;
	}
	
	public SectionIterator(String type, Document doc, TokenAnalyzer[] analyzer) throws Exception {
		this(new String[] {type}, doc, analyzer) ;
	}

	public SectionIterator(String[] type, Document doc, TokenAnalyzer[] analyzer) throws Exception {
		List<QueryData> sections = new ArrayList<QueryData>() ;
		QueryDataField tfield = new QueryDataField("title", doc.getTitle(), analyzer) ;
		for(Section sel : doc.getSection()) {
			if(type == null || StringUtil.isIn(sel.getType(), type)) {
				QueryData document = new QueryData() ;
				document.add(tfield) ;
				document.add("header",     sel.getHeader(), analyzer) ;
				document.add(sel.getType(),sel.getText(), analyzer) ;
				sections.add(document) ;
			}
		}
		this.iterator = sections.iterator() ;
	}
	
  public QueryData next() throws Exception {
  	if(iterator.hasNext()) return iterator.next() ;
  	return null ;
  }
}