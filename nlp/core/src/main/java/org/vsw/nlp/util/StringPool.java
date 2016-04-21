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
package org.vsw.nlp.util;

import java.util.HashMap;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class StringPool extends HashMap<String, String> {
	public String getString(String value) {
		if(value == null) return null ;
		String ret = get(value) ;
		if(ret != null) return ret ;
		put(value, value) ;
		return value ;
	}
	
	public String[] getString(String[] value) {
		if(value == null) return null ;
		for(int i = 0; i < value.length; i++) {
			String ret = get(value[i]) ;
			if(ret != null) {
				value[i] = ret ;
			} else {
			  put(value[i], value[i]) ;
			}
		}
		return value ;
	}
}