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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Lexicon implements Model {
	private String      name ;
	private String[]    tag ;
	private boolean     manageable = true ; 
	
	public Lexicon() {} 
	
	public Lexicon(String name, String[] tag) { 
		setName(name) ;
		this.tag = tag ; 
	}
	
	@JsonIgnore
	public String getId() { return name ; }
	
	public String getName() { return name; }
	public void   setName(String name) { this.name = name; }
	
	public String getOType() { return "lexicon"; }
  public void   setOType(String type) { }
	
  @JsonIgnore
  public boolean getManageable() { return this.manageable ; }
  public void    setManageable(boolean b) { this.manageable = b ; }
  
  public void addTag(String atag) {
  	if(this.tag == null) {
  		this.tag = new String[] {atag} ;
  	} else {
  		for(int i = 0 ; i < this.tag.length; i++) {
  			if(tag[i].equals(atag)) return  ;
  		}
  		String[] temp = new String[tag.length + 1] ;
  		System.arraycopy(tag, 0, temp, 0, this.tag.length) ;
  		temp[tag.length] = atag ;
  		tag = temp ;
  	}
  }
  
	public String[] getTag() { return this.tag ; }
	public void     setTag(String[] tag) { this.tag = tag ; }

	public void merge(Lexicon other) {
		this.tag = StringUtil.merge(tag, other.getTag()) ;
	}
}