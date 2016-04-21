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
package org.vsw.nlp.doc;

import java.util.LinkedHashMap;
import java.util.List;

import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen $ 
 * $Revision: 71 $
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DynaObject extends LinkedHashMap<String, Object> {
  private static final long serialVersionUID = 1L;
  
  public Object getProperty(String name) {
  	if(name.indexOf(':') < 0) {
  		return get(name) ;
  	} else {
  		List<String> path = StringUtil.split(name, ':') ;
  		return getProperty(path, 0) ;
  	}
  }
  
  Object getProperty(List<String> path, int pos) {
  	String cpath = path.get(pos) ;
  	if(pos == path.size() - 1) {
  		return get(cpath) ;
  	} else {
  		DynaObject sub = (DynaObject) get(cpath) ;
  		if(sub == null) return null ;
  		return sub.getProperty(path, pos + 1) ;
  	}
  }
  
  public void setProperty(String name, Object value) {
  	if(name.indexOf(':') < 0) {
  		put(name, value) ;
  	} else {
  		List<String> path = StringUtil.split(name, ':') ;
  		setProperty(path, 0, value) ;
  	}
  }

  void setProperty(List<String> path, int pos, Object value) {
  	String cpath = path.get(pos) ;
  	if(pos == path.size() - 1) {
  		put(cpath, value) ;
  	} else {
  		DynaObject sub = (DynaObject) get(cpath) ;
  		if(sub == null) {
  			sub = new DynaObject() ;
  			put(cpath, sub) ;
  		}
  		sub.setProperty(path, pos + 1, value) ;
  	}
  }
}