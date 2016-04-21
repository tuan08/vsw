package org.vsw.nlp.query.eval;

import java.io.IOException;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.util.StringUtil;

public class Tag implements EvalExpression {
	private String[] tag;
	
	public Tag(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "tag"; }

  public void init(String paramexp) throws Exception {
  	tag = StringUtil.toStringArray(paramexp) ;
  }

  public Object eval(QueryContext context, QueryData doc) throws IOException {
	  for(String sel : tag) context.addQueryTag(sel) ;
  	return true ;
  }
}
