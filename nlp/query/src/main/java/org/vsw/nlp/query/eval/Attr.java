package org.vsw.nlp.query.eval;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.util.StringUtil;

public class Attr implements EvalExpression {
	private Map<String, String[]> attrs = new HashMap<String, String[]>();
	
	public Attr(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "attr"; }

  public void init(String paramexp) throws Exception {
  	String[] attrExp = StringUtil.toStringArray(paramexp) ;
  	for(int i = 0; i < attrExp.length; i++) {
  		String sel = attrExp[i].trim() ;
  		int idx = sel.indexOf('=') ;
  		String name = sel.substring(0, idx).trim() ;
  		String value = sel.substring(idx + 1).trim() ;
  		attrs.put(name, new String[] {value}) ;
  	}
  }

  public Object eval(QueryContext context, QueryData doc) throws IOException {
  	Iterator<Map.Entry<String, String[]>> i = attrs.entrySet().iterator() ;
  	while(i.hasNext()) {
  		Map.Entry<String, String[]> entry = i.next() ;
  		context.set(entry.getKey(), entry.getValue(), true, false) ;
  	}
  	return true ;
  }
}