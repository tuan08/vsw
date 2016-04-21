package org.vsw.nlp.query.matcher;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

abstract public class TagMatcher {
	private int allowNextMatchDistance ;

	abstract public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception ;
	abstract public Tag matches(IToken token) ;

	public int getAllowNextMatchDistance() { return this.allowNextMatchDistance ; }
	public void setAllowNextMatchDistance(int distance) { allowNextMatchDistance = distance ; }

	final protected boolean matches(StringMatcher[] matcher, String value) {
		if(matcher == null) return true ;
		for(StringMatcher sel : matcher) {
			if(sel.matches(value)) return true ;
		}
		return false  ;
	}

	final protected boolean matches(StringMatcher[] matcher, String[] value) {
		if(matcher == null) return true ;
		for(StringMatcher sel : matcher) {
			for(String selValue : value) {
				if(sel.matches(selValue)) return true ;
			}
		}
		return false  ;
	}

	protected StringMatcher[] createMatcher(ParamHolder holder, String name) throws Exception {
		String[] matchexp = holder.getFieldValue(name) ;
		if(matchexp != null) {
			StringMatcher[] matcher = new StringMatcher[matchexp.length] ;
			for(int i = 0; i < matcher.length; i++) {
				matcher[i] = new StringMatcher(matchexp[i]) ;
			}
			return matcher ;
		}
		return null ;
	}
}