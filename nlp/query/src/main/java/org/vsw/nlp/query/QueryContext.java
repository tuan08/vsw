package org.vsw.nlp.query;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.vsw.nlp.doc.DynaObject;
import org.vsw.util.StringUtil;

public class QueryContext implements Serializable {
  private static final long serialVersionUID = 1L;
  static Map<String, String[]> EMPTY = Collections.unmodifiableMap(new HashMap<String, String[]>()) ;
  
  private boolean complete = false ;
  private boolean isContinue = false ;
  private MatchResult[] matchResults ;
  private HashSet<String> queryTags = new HashSet<String>() ;;
  private LinkedHashMap<String, String[]> queryAttrs = new LinkedHashMap<String, String[]>() ;
  
  
  private HashSet<String> tags = new HashSet<String>() ;;
  private LinkedHashMap<String, String[]> attrs = new LinkedHashMap<String, String[]>() ;
  
  private boolean debug = false ;
  
  public QueryContext() {}
  
  public boolean isComplete() { return complete ; }
  public void    setComplete(boolean b) { this.complete = b ; }
  
  public boolean isContinue() { return isContinue ; }
  public void    setContinue(boolean isContinue) { this.isContinue = isContinue ; }
  
  public boolean isDebug̣̣̣() { return debug ; }
  public void    setDebug(boolean b) { debug = b  ; } 
  
  public HashSet<String> getQueryTags() { return queryTags ; }
  public HashSet<String> getTags() { return tags ; }
  
  public void addQueryTag(String tag) { queryTags.add(tag) ; }
  
  public void addQueryTag(String[] tag) {
  	for(int i = 0; i < tag.length; i++) queryTags.add(tag[i]) ;
  }
  
  public boolean hasQueryTag(String tag) {  return queryTags.contains(tag) ; }
  
  public void set(String key, String[] value, boolean attr, boolean tag) {
  	if(attr) queryAttrs.put(key, value) ;
  	if(tag) {
  		for(int i = 0; i < value.length; i++) addQueryTag(key + "=" + value[i]) ;
  	}
  }
  
  public void removeQueryAttribute(String key) { queryAttrs.remove(key) ;}
  
  public String[] getQueryAttribute(String key) { return queryAttrs.get(key) ; }
  
  public MatchResult[] getMatchResults() { return this.matchResults ; }
  public void setMatchResult(MatchResult[] mresult) { this.matchResults = mresult ; }
  
  public void commit() {
  	if(queryAttrs.size() > 0) {
  		Iterator<Map.Entry<String, String[]>> i = queryAttrs.entrySet().iterator() ;
  		while(i.hasNext()) {
  			Map.Entry<String, String[]> entry = i.next() ;
  			attrs.put(entry.getKey(), StringUtil.merge(queryAttrs.get(entry.getKey()), entry.getValue())) ;
  		}
  		queryAttrs.clear() ;
  	}
  	if(queryTags.size() > 0) {
  		this.tags.addAll(queryTags) ;
  		queryTags.clear() ;
  	}
  	this.complete = false ;
    this.isContinue = false ;
  }
  
  public void clear() {
    queryAttrs.clear() ;
    queryTags.clear() ;
    this.matchResults = null ;
  }
  
  public void reset() {
    queryAttrs.clear() ;
    queryTags.clear() ;
    this.matchResults = null ;
    this.complete = false ;
    this.isContinue = false ;
    this.tags.clear() ;
    this.attrs.clear() ;
  }
  
  public Map<String, String[]> getQueryAttributes() { return queryAttrs ; }
  public Map<String, String[]> getAttributes() { return attrs ; }
  
  public Map<String, String[]> getAttributes(String namePrefix) { 
  	Map<String, String[]> sub = new HashMap<String, String[]>() ;
  	Iterator<Map.Entry<String, String[]>> iterator = attrs.entrySet().iterator(); 
  	while(iterator.hasNext()) {
  		Map.Entry<String, String[]> entry = iterator.next() ;
  		if(entry.getKey().startsWith(namePrefix)) {
  			sub.put(entry.getKey(), entry.getValue()) ;
  		}
  	}
  	return sub ; 
  }
  
  public String getQueryAttributesAsString() {
  	StringBuilder b = new StringBuilder() ;
  	Iterator<Map.Entry<String, String[]>> i = queryAttrs.entrySet().iterator() ;
  	b.append("Attributes: \n") ;
  	while(i.hasNext()) {
  		Map.Entry<String, String[]> entry = i.next() ;
  		b.append(entry.getKey()).append(": ").append(StringUtil.joinStringArray(entry.getValue())).append("\n") ;
  	}
  	return b.toString() ;
  }
  
  public String getQueryTagsAsString() {
  	StringBuilder b = new StringBuilder() ;
  	Iterator<String> itr = queryTags.iterator() ;
  	b.append("Tags: \n") ;
  	while(itr.hasNext()) {
  		b.append(itr.next()).append("\n") ;
  	}
  	return b.toString() ;
  }
  
  public String toString() {
  	StringBuilder b = new StringBuilder() ;
  	Iterator<Map.Entry<String, String[]>> i = attrs.entrySet().iterator() ;
  	b.append("Attributes: \n") ;
  	while(i.hasNext()) {
  		Map.Entry<String, String[]> entry = i.next() ;
  		b.append(entry.getKey()).append(": ").append(StringUtil.joinStringArray(entry.getValue())).append("\n") ;
  	}
  	Iterator<String> itr = tags.iterator() ;
  	b.append("Tags: \n") ;
  	while(itr.hasNext()) {
  		b.append(itr.next()).append("\n") ;
  	}
  	return b.toString() ;
  }
}