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
public class PhoneTag extends Tag {
	final static public String TYPE = "phone" ;
	
	private String number ;
	private String type ;
	private String provider ;

	public PhoneTag(String number, String provider, String type) {
		this.number    = number ;
		this.provider  = provider ;
		this.type = type ;
	}
	
	public String getNumber() { return this.number  ; }
	
	public String getProvider() { return this.provider ; }
	
	public String getType() { return this.type ; }
	
	public String getTagValue() { return number ; }

	public String getOType() { return TYPE ; }
	
  public boolean isTypeOf(String type) {
	  return TYPE.equals(type);
  }
}