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
public class WordTag extends Tag {
	final static public String TYPE   = "word" ;
	final static public String LETTER = "letter" ;
	
	final static public WordTag WLETTER = new WordTag(LETTER) ;
	final static public WordTag SEQ_LD  = new WordTag("letter+digit") ;
	final static public WordTag SEQ_LDD = new WordTag("letter+digit+dash") ;
	final static public WordTag UNKNOWN = new WordTag("unknown") ;
	final static public WordTag KNOWN   = new WordTag("known") ;
	
	private String otype ;
	
	public WordTag(String otype) {
	  this.otype = otype ;
  }

	public String getOType() { return this.otype; }
	
	public boolean isTypeOf(String type) {
		if("word".equals(type)) return true ;
		return otype.equals(type) ;
	}
	
	public String getInfo() {
		return getOType() + ": {}" ;
	}
}
