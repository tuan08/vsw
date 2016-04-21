package org.vsw.nlp.query.eval;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.util.StringMatcher;
import org.vsw.util.StringUtil;

public class TagCount implements EvalExpression {
	private StringMatcher[] tagmatcher;
	
	public TagCount(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "tagcount"; }

  public void init(String paramexp) throws Exception {
  	String[] array = StringUtil.toStringArray(paramexp) ;
  	tagmatcher = new StringMatcher[array.length] ;
  	for(int i = 0; i < tagmatcher.length; i++) {
  		tagmatcher[i] = new StringMatcher(array[i]) ;
  	}
  }

  public Object eval(QueryContext context, QueryData doc) throws IOException {
	  int count = 0 ;
  	for(int i = 0; i < tagmatcher.length; i++) {
  		count += count(tagmatcher[i], context.getQueryTags()) ;
  		count += count(tagmatcher[i], context.getTags()) ;
	  }
  	return (double)count ;
  }
  
  private int count(StringMatcher matcher, HashSet<String> tag) {
  	int count  = 0 ;
  	Iterator<String> i = tag.iterator() ;
  	while(i.hasNext()) {
  		if(matcher.matches(i.next())) count++ ;
  	}
  	return count ;
  }
}
