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
package org.vsw.nlp.token.tag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NameTag extends Tag {
	final static public String VNNAME = "vnname" ;
	final static public String NAME   = "name" ;
	
	private String otype ;
	private String value ;
	
	public NameTag(String otype, String value) {
	  this.otype = otype ;
	  this.value = value ;
  }

	public String getOType() { return this.otype; }
	
	public String getTagValue() { return value ; }
	
	public boolean isTypeOf(String type) {
		if("word".equals(type)) return true ;
		return otype.equals(type) ;
	}
}
