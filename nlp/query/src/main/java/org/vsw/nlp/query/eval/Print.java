package org.vsw.nlp.query.eval;

import java.io.IOException;
import java.io.PrintStream;

import org.vsw.nlp.query.MatchResult;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.util.ConsoleUtil;
import org.vsw.util.StringUtil;

public class Print implements EvalExpression {
	private boolean tag = false, attr = false, match = false ; 
	
	public Print(String paramexp) throws Exception {
		init(paramexp) ;
	}
	
  public String getName() { return "print"; }

  public void init(String paramexp) throws Exception {
  	for(String sel : StringUtil.toStringArray(paramexp)) {
  		if("all".equals(sel)) {
  			tag = true; attr = true ; match = true ;
  			break ;
  		} else if("tag".equals(sel)) {
  			tag = true ;
  		} else if("attr".equals(sel)) {
  			attr = true ;
  		} else if("match".equals(sel)) {
  			match = true ;
  		}
  	}
  }

  public Object eval(QueryContext context, QueryData doc) throws IOException {
  	PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
  	if(match) {
  		if(context.getMatchResults() != null) {
  			for(MatchResult sel : context.getMatchResults()) {
  				out.println(sel.getMatchTokenSequence().getSegmentTokens()) ;
  			}
  		}
  	}
  	if(tag) out.println(context.getQueryTagsAsString());
  	if(attr) out.println(context.getQueryAttributesAsString());
  	return true ;
  }
}
