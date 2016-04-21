package org.vsw.nlp.query;

import java.util.ArrayList;
import java.util.List;

public class MatchRules {
	private int matchmax ;
	private MatchRule[] rules ;
	
	public MatchRules(String[] exp, int matchmax) throws Exception {
		this.matchmax = matchmax ;
		this.rules = new MatchRule[exp.length] ;
		for(int i = 0; i < rules.length; i++) {
			this.rules[i] = new MatchRule(exp[i]) ;
		}
	}
	
	final public MatchResult[] matches(QueryData doc) throws Exception {
		List<MatchResult> holder = new ArrayList<MatchResult>() ;
		for(int i = 0; i < rules.length; i++) {
			rules[i].matches(holder, doc, matchmax) ;
		}
		if(holder.size() == 0) return null ;
		return holder.toArray(new MatchResult[holder.size()]) ;
	}
}
