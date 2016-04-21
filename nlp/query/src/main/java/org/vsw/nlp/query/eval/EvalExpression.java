package org.vsw.nlp.query.eval;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;

public interface EvalExpression {
	public String getName() ;
	public void init(String paramexp) throws Exception  ;
	public Object eval(QueryContext context, QueryData doc) throws Exception ;
}
