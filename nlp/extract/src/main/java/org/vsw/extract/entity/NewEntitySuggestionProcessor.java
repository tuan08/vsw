package org.vsw.extract.entity;

public class NewEntitySuggestionProcessor implements EntitySuggestionProcessor {
  public EntitySuggestion process(EntitySuggestion suggestion) {
  	String name = suggestion.getName() ;
  	char[] buf = name.toCharArray() ;
  	for(int i = 0; i < buf.length; i++) {
  		if(buf[i] == ','  || buf[i] == '+' || buf[i] == '_') {
  			return null ;
  		}
  	}
  	return suggestion ;
  }
}
