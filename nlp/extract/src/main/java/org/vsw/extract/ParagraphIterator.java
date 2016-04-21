package org.vsw.extract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.query.QueryDataField;
import org.vsw.nlp.query.QueryDataIterator;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.StringUtil;

public class ParagraphIterator implements QueryDataIterator {
private Iterator<QueryData> iterator ;
	
	public ParagraphIterator(Document doc, TokenAnalyzer[] analyzer) throws Exception {
		this((String[])null, doc, analyzer) ;
	}
	
	public ParagraphIterator(String type, Document doc, TokenAnalyzer[] analyzer) throws Exception {
		this(new String[] {type}, doc, analyzer) ;
	}

	public ParagraphIterator(String[] type, Document doc, TokenAnalyzer[] analyzer) throws Exception {
		List<QueryData> holder = new ArrayList<QueryData>() ;
		QueryDataField tfield = new QueryDataField("title", doc.getTitle(), analyzer) ;
		for(Section sel : doc.getSection()) {
			if(type == null || StringUtil.isIn(sel.getType(), type)) {
				QueryDataField hfield = new QueryDataField("header", sel.getHeader(), analyzer) ;
				List<String> paragraph = sel.getParagraph() ;
				for(int i = 0; i < paragraph.size(); i++) {
					QueryData document = new QueryData() ;
					document.add(tfield) ;
					document.add(hfield) ;
					document.add(sel.getType(), paragraph.get(i), analyzer) ;
					holder.add(document) ;
				}
			}
		}
		this.iterator = holder.iterator() ;
	}
	
  public QueryData next() throws Exception {
  	if(iterator.hasNext()) return iterator.next() ;
  	return null ;
  }
}
