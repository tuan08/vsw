package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.CurrencyTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class QuantityTagMatcher extends TagMatcher {
	private StringMatcher[] unitMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		String[] unit = holder.getFieldValue("unit") ;
		if(unit != null) {
			unitMatcher = new StringMatcher[unit.length] ;
			for(int i = 0; i < unitMatcher.length; i++) {
				unitMatcher[i] = new StringMatcher(unit[i]) ;
			}
		}
		return this ;
	}
	
  public CurrencyTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof CurrencyTag)) continue ;
  		CurrencyTag implTag = (CurrencyTag) tag ;
  		if(unitMatcher != null) {
  			if(!matches(unitMatcher, implTag.getUnit())) return null ;
  		}
  		return implTag ;
  	}
  	return null ;
  }
}