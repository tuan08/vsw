package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.EmailTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class EmailTagMatcher extends TagMatcher {
	private StringMatcher[] providerMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		String[] provider = holder.getFieldValue("provider") ;
		if(provider != null) {
			providerMatcher = new StringMatcher[provider.length] ;
			for(int i = 0; i < providerMatcher.length; i++) {
				providerMatcher[i] = new StringMatcher(provider[i]) ;
			}
		}
		return this ;
	}
	
  public EmailTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof EmailTag)) continue ;
  		EmailTag implTag = (EmailTag) tag ;
  		if(providerMatcher != null) {
  			if(!matches(providerMatcher, implTag.getProvider())) return null ;
  		}
  		return implTag ;
  	}
  	return null ;
  }
}