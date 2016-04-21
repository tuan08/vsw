package org.vsw.nlp.query.eval;

import java.io.IOException;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;


public class Continue implements EvalExpression {

	public Continue() {} 
	
  public String getName() { return "continue"; }

  public void init(String paramexp) throws Exception {
  }
  
  public Object eval(QueryContext context, QueryData doc) throws IOException {
    context.setContinue(true) ;
	  return true ;
  }
}
