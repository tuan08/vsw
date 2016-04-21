package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.LexiconTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class LexiconTagMatcher extends TagMatcher {
	private StringMatcher[] tagMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance);
		String[] tag = holder.getFieldValue("tag") ;
		if(tag != null) {
			tagMatcher = new StringMatcher[tag.length] ;
			for(int i = 0; i < tagMatcher.length; i++) {
				tagMatcher[i] = new StringMatcher(tag[i]) ;
			}
		}
		return this ;
	}
	
  public LexiconTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof LexiconTag)) continue ;
  		LexiconTag implTag = (LexiconTag) tag ;
  		if(tagMatcher != null) {
  			if(!matches(tagMatcher, implTag.getLexicon().getTag())) return null ;
  		}
  		return implTag ;
  	}
  	return null ;
  }
}