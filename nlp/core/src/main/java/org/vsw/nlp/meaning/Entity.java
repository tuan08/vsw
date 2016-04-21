/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.nlp.meaning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.vsw.nlp.util.StringPool;
import org.vsw.util.MD5;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Entity extends HashMap<String, Object> implements Model {
	final static public String OTYPE        = "otype" ;
	final static public String NAME         = "name" ;
	final static public String VARIANT      = "variant" ;
	final static public String TOPIC        = "topic" ;
	final static public String UDESCRIPTION = "udescription" ;
	
	@JsonIgnore
	public String getId() {
		String uid = getUDescription() ;
		if(uid == null) uid = getTopic() ;
		if(uid == null) uid = getName() ;
		return MD5.digest(uid).toString() ; 
	}
	
	public String getOType() { return (String)get("otype") ; }
	public void   setOType(String type) { put("otype", type) ;}
	
	public String   getName() { 
		return (String) get(NAME) ;
	}
	public void     setName(String name) { 
		if(name == null || name.length() == 0) {
			remove(NAME) ;
		} else {
		  put(NAME, name) ;
		}
	}

	public String[] getVariant() { 
		try { 
			return (String[]) get(VARIANT) ;
		} catch(Throwable t) {
			System.out.println("Cast Exception for " + getName());
			return null ;
		}
	}
	public void     setVariant(String[] variant) { put(VARIANT, variant) ; }

	public String   getTopic() { return (String) get(TOPIC) ; }
	public void     setTopic(String topic) { 
		if(topic == null || topic.length() == 0) {
			remove(TOPIC) ;
		} else {
			put(TOPIC, topic) ;
		}
	}
	
	public String getUDescription() { return (String) get(UDESCRIPTION); }
	public void   setUDescription(String string) { put(UDESCRIPTION, string) ; }
	
	public String[] getType() { return (String[]) get("type") ; }
	public void     setType(String[] type) { put("type", type) ;}
	
	public Object put(String key, Object value) {
		if(value == null) {
			remove(key) ;
			return value ;
		}
		if(value instanceof List) {
			List<String> list = (List<String>) value ;
			value = list.toArray(new String[list.size()]) ;
		}
		return super.put(key, value) ;
	}
	
	public void optimize(StringPool pool) {
		Iterator<String> i = keySet().iterator() ;
		while(i.hasNext()) {
			String key = i.next() ;
			Object value = get(key) ;
			key = pool.getString(key) ;
			if(value instanceof String[]) {
				value = pool.getString((String[])value) ;
			} else {
				value = pool.getString((String)value) ;
			}
			put(key, value) ;
		}
	}
}