package org.vsw.nlp.query;

import org.vsw.nlp.query.eval.EvalExpression;
import org.vsw.nlp.query.eval.EvalExpressionParser;

public class Query {
	private String   name ;
	private int      priority ;
	private String   description ;
	
	private int      matchmax = 1;
	private String   matchselector = "first";
	private String[] prematch ;
	private String[] match ;
	private String[] extract ;
	private String[] postmatch ;
	
	transient private EvalExpression[] preMatchEval ;
	transient private MatchRules       matchRules ;
	transient private ExtractRules     extractRules ;
	transient private EvalExpression[] postMatchEval ;
	
	public Query() {
		
	}

	public String getOType() { return "query"; }
  public void   setOType(String type) { }

	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public int getPriority() { return priority; }
	public void setPriority(int priority) { this.priority = priority; }

	public String getDescription() { return description; }
	public void   setDescription(String description) { this.description = description; }

	public int  getMatchmax() { return this.matchmax ; }
	public void setMatchmax(int max) { this.matchmax = max ; } 
	
	public String getMatchselector() { return matchselector ; }
	public void   setMatchselector(String name) { this.matchselector = name; }

	public String[] getPrematch() { return prematch; }
	public void setPrematch(String[] prematch) { this.prematch = prematch; }

	public String[] getMatch() { return match; }
	public void setMatch(String[] match) { this.match = match; }

	public String[] getExtract() { return extract; }
	public void setExtract(String[] extract) { this.extract = extract; }

	public String[] getPostmatch() { return postmatch; }
	public void setPostmatch(String[] postmatch) { this.postmatch = postmatch; }

	public void compile() throws Exception {
		if(prematch != null) {
			preMatchEval = new EvalExpression[prematch.length] ;
			for(int i = 0; i < prematch.length; i++) {
				preMatchEval[i] = EvalExpressionParser.parse(prematch[i]) ;
			}
		}
		matchRules = new MatchRules(match, matchmax) ;
		extractRules = new ExtractRules(extract) ;
		if(postmatch != null) {
			postMatchEval = new EvalExpression[postmatch.length] ;
			for(int i = 0; i < postmatch.length; i++) {
				postMatchEval[i] = EvalExpressionParser.parse(postmatch[i]) ;
			}
		}
	}

	public void query(QueryContext context, QueryData doc) throws Exception {
		context.setMatchResult(null) ;
		if(preMatchEval != null) {
			for(int i = 0; i < preMatchEval.length; i++) {
				preMatchEval[i].eval(context, doc) ;
				if(context.isComplete()) break ;
			}
		}
		if(!context.isComplete()) {
			MatchResult[] mresult = matchRules.matches(doc) ;
			extractRules.extract(context, mresult) ;
			if(postMatchEval != null && mresult != null) {
				context.setMatchResult(mresult) ;
				for(int i = 0; i < postMatchEval.length; i++) {
					postMatchEval[i].eval(context, doc) ;
					if(context.isComplete()) break ;
				}
			}
		}
		context.commit() ;
	}
}