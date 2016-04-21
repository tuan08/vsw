package org.vsw.nlp.query.matcher;

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.PlaceTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.topic.Place;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringMatcher;

public class PlaceTagMatcher extends TagMatcher {
	private StringMatcher[] streetMatcher ;
	private StringMatcher[] quarterMatcher ;
	private StringMatcher[] districtMatcher ;
	private StringMatcher[] cityMatcher ;
	private StringMatcher[] countryMatcher ;
	private StringMatcher[] postalCodeMatcher ;
	private StringMatcher[] typeMatcher ;
	
	public TagMatcher init(ParamHolder holder, int allowNextMatchDistance) throws Exception {
		setAllowNextMatchDistance(allowNextMatchDistance) ;
		streetMatcher = createMatcher(holder, "street") ;
		quarterMatcher = createMatcher(holder, "quarter") ;
	  districtMatcher = createMatcher(holder, "district") ;
	  cityMatcher = createMatcher(holder, "city") ;
	  countryMatcher = createMatcher(holder, "country") ;
	  postalCodeMatcher = createMatcher(holder, "postalCode") ;
	  typeMatcher = createMatcher(holder, "type") ;
		return this ;
	}
	
  public PlaceTag matches(IToken token) {
  	List<Tag> tags = token.getTag() ;
  	if(tags == null) return null ;
  	for(int i = 0; i < tags.size(); i++) {
  		Tag tag = tags.get(i) ;
  		if(!(tag instanceof PlaceTag)) continue ;
  		PlaceTag implTag = (PlaceTag) tag ;
  		Place place = ((PlaceTag) tag).getPlace() ;
  		if(!matches(streetMatcher, place.getStreet())) return null ;
  		if(!matches(quarterMatcher, place.getQuarter())) return null ;
  		if(!matches(districtMatcher, place.getDistrict())) return null ;
  		if(!matches(cityMatcher, place.getCity())) return null ;
  		if(!matches(countryMatcher, place.getCountry())) return null ;
  		if(!matches(postalCodeMatcher, place.getPostalCode())) return null ;
  		if(!matches(typeMatcher, place.getType())) return null ;
  		return implTag ;
  	}
  	return null ;
  }
}