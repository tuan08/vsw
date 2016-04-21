package org.vsw.nlp.query.matcher;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.token.tag.WordTag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class TokenMatcher extends TagMatcher {
	final static public WordTag MATCH_TAG = new WordTag("TokenMatcher") ;
	private StringMatcher[] normMatcher ;
	private StringMatcher[] origMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		normMatcher = createMatcher(holder, "norm") ;
		origMatcher = createMatcher(holder, "orig") ;
		return this ;
	}
	
	public Tag matches(IToken token) {
		if(!matches(normMatcher, token.getNormalizeForm())) return null ;
		if(!matches(origMatcher, token.getOriginalForm())) return null ;
		return MATCH_TAG ;
	}
}