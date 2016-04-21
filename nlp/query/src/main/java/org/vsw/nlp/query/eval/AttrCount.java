package org.vsw.nlp.query.eval;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.util.StringMatcher;
import org.vsw.util.StringUtil;

public class AttrCount implements EvalExpression {
	private StringMatcher[] attrmatcher;
	
	public AttrCount(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "attrcount"; }

  public void init(String paramexp) throws Exception {
  	String[] array = StringUtil.toStringArray(paramexp) ;
  	attrmatcher = new StringMatcher[array.length] ;
  	for(int i = 0; i < attrmatcher.length; i++) {
  		attrmatcher[i] = new StringMatcher(array[i]) ;
  	}
  }

  public Object eval(QueryContext context, QueryData doc) throws IOException {
	  int count = 0 ;
  	for(int i = 0; i < attrmatcher.length; i++) {
  		count += count(attrmatcher[i], context.getQueryAttributes()) ;
  		count += count(attrmatcher[i], context.getAttributes()) ;
	  }
  	return (double)count ;
  }
  
  private int count(StringMatcher matcher, Map<String, String[]> attrs) {
  	int count  = 0 ;
  	Iterator<String> i = attrs.keySet().iterator() ;
  	while(i.hasNext()) {
  		if(matcher.matches(i.next())) count++ ;
  	}
  	return count ;
  }
}
