package org.vsw.nlp.query.eval;

import org.vsw.nlp.query.MatchRule;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;

public class Match implements EvalExpression {
	private MatchRule mrule  ;
	
	public Match(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "match"; }

  public void init(String paramexp) throws Exception {
  	mrule = new MatchRule(paramexp) ;
  }

  public Object eval(QueryContext context, QueryData doc) throws Exception {
  	boolean match =  mrule.matches(doc, 1) != null ;
  	return match ;
  }
}