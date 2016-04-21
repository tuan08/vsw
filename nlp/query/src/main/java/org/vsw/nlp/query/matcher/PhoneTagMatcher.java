package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.PhoneTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class PhoneTagMatcher extends TagMatcher {
	private StringMatcher[] typeMatcher ;
	private StringMatcher[] providerMatcher ;
	private StringMatcher[] numberMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		String[] type = holder.getFieldValue("type") ;
		if(type != null) {
			typeMatcher = new StringMatcher[type.length] ;
			for(int i = 0; i < typeMatcher.length; i++) {
				typeMatcher[i] = new StringMatcher(type[i]) ;
			}
		}
		
		String[] provider = holder.getFieldValue("provider") ;
		if(provider != null) {
			providerMatcher = new StringMatcher[provider.length] ;
			for(int i = 0; i < providerMatcher.length; i++) {
				providerMatcher[i] = new StringMatcher(provider[i]) ;
			}
		}
		
		String[] number = holder.getFieldValue("number") ;
		if(number != null) {
			numberMatcher = new StringMatcher[number.length] ;
			for(int i = 0; i < numberMatcher.length; i++) {
				numberMatcher[i] = new StringMatcher(number[i]) ;
			}
		}
		return this ;
	}
	
  public PhoneTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof PhoneTag)) continue ;
  		PhoneTag implTag = (PhoneTag) tag ;
  		if(typeMatcher != null) {
  			if(!matches(typeMatcher, implTag.getType())) return null ;
  		}
  		if(providerMatcher != null) {
  			if(!matches(providerMatcher, implTag.getProvider())) return null ;
  		}
  		if(numberMatcher != null) {
  			if(!matches(numberMatcher, implTag.getNumber())) return null ;
  		}
  		return implTag ;
  	}
  	return null ;
  }
}