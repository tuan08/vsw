package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.meaning.Synset;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.SynsetTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class SynsetTagMatcher extends TagMatcher {
	private StringMatcher[] nameMatcher ;
	private StringMatcher[] typeMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		nameMatcher = createMatcher(holder, "name") ;
		typeMatcher = createMatcher(holder, "type") ;
		return this ;
	}
	
  public SynsetTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof SynsetTag)) continue ;
  		SynsetTag synsetTag = (SynsetTag) tag ;
  		Synset synset = synsetTag.getSynset() ;
  		if(!matches(nameMatcher, synset.getName())) return null ;
  		if(!matches(typeMatcher, synset.getType())) return null ;
  		return synsetTag ;
  	}
  	return null ;
  }
}