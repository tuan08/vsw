package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.DigitTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;

public class DigitTagMatcher extends TagMatcher {
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		return this ;
	}
	
  public DigitTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof DigitTag)) continue ;
  		DigitTag implTag = (DigitTag) tag ;
  		return implTag ;
  	}
  	return null ;
  }
}