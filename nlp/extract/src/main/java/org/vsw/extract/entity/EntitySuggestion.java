package org.vsw.extract.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.Writable;
import org.vsw.hadoop.util.WritableUtil;

public class EntitySuggestion implements Writable {
	private String     ctype ;
	private String     prefix ;
	private String     name ;
	
	private Map<String, PropertySuggestion> suggestions = new HashMap<String, PropertySuggestion>() ;
	
	public String computeId() { return prefix + " " + name; }
	
	public String getCType() { return this.ctype ; }
	public void setCType(String ctype) { this.ctype = ctype ;}
	
	public String getPrefix() { return this.prefix ; }
	public void setPrefix(String prefix) { this.prefix = prefix ;}
	
	public String getName() { return this.name ; }
	public void setName(String name) { this.name = name ; }
	
	public void addSuggestion(PropertySuggestion sugg) {
		this.suggestions.put(sugg.getName(), sugg) ;
	}
	public Map<String, PropertySuggestion> getSuggestions() { return suggestions; }
	public void setSuggestions(Map<String, PropertySuggestion> suggestions) { 
		this.suggestions = suggestions; 
	}

	public void readFields(DataInput in) throws IOException {
		this.ctype = WritableUtil.readString(in) ;
		this.prefix = WritableUtil.readString(in) ;
		this.name = WritableUtil.readString(in) ;
		int size = in.readInt() ;
		for(int i = 0; i < size; i++) {
			PropertySuggestion suggestion = new PropertySuggestion() ;
			suggestion.readFields(in) ;
			this.suggestions.put(suggestion.getName(), suggestion) ;
		}
	}

	public void write(DataOutput out) throws IOException {
		WritableUtil.writeString(out, ctype) ;
		WritableUtil.writeString(out, prefix) ;
		WritableUtil.writeString(out, name) ;
		
		out.writeInt(this.suggestions.size()) ;
		Iterator<PropertySuggestion> i = suggestions.values().iterator() ;
		while(i.hasNext()) {
			PropertySuggestion suggestion = i.next() ;
			suggestion.write(out) ;
		}
	}
	
  public boolean simmilar(EntitySuggestion other) throws Exception {
		return prefix.equals(other.prefix) && name.equals(other.name) ;
	}
	
  public void merge(EntitySuggestion other) throws Exception {
  	Iterator<PropertySuggestion> suggestions = other.suggestions.values().iterator() ;
  	while(suggestions.hasNext()) {
  		PropertySuggestion suggestion = suggestions.next() ;
  		PropertySuggestion exist = this.suggestions.get(suggestion.getName()) ;
  		if(exist != null) {
  			exist.merge(suggestion) ;
  		} else {
  			this.suggestions.put(suggestion.getName(), suggestion) ;
  		}
  	}
  }
  
  public String toString() {
  	return computeId() ; 
  }
}