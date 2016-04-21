package org.vsw.nlp.query.eval;

import java.io.IOException;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;

public class Msg implements EvalExpression {
	private String msg ;
	
	public Msg(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "msg"; }

  public void init(String paramexp) throws Exception {
  	msg = paramexp ;
  }

  public Object eval(QueryContext context, QueryData doc) throws IOException {
  	System.out.println(msg);
  	return true ;
  }
}