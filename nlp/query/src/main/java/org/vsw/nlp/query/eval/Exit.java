package org.vsw.nlp.query.eval;

import java.io.IOException;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;

public class Exit implements EvalExpression {
	private boolean clear = false;
	
	public Exit(String exp) throws Exception {
		init(exp) ;
	}
	
  public String getName() { return "exit"; }

  public void init(String exp) throws Exception  {
    if(exp == null) return  ;
    if(exp.equals("clear")) clear = true ;
  }
  
  public Object eval(QueryContext context, QueryData doc) throws IOException {
  	context.setComplete(true) ;
  	if(clear) context.clear() ;
  	return clear ;
  }
}