package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.token.tag.WordTag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class WordTagMatcher extends TagMatcher {
	private StringMatcher[] otypeMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		otypeMatcher = createMatcher(holder, "otype") ;
		return this ;
	}
	
	public Tag matches(IToken token) {
		List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof WordTag)) continue ;
  		WordTag implTag = (WordTag) tag ;
  	  if(!matches(otypeMatcher, implTag.getOType())) return null ;
  	  System.out.println("====> " + token.getOriginalForm() + ", tag " + tag.getOType());
  	  return implTag ;
  	}
  	return null ;
	}
}