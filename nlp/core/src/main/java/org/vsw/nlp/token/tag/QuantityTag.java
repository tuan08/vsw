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
public class QuantityTag extends Tag {
	private String otype ;
	private String unit ;
	
	public QuantityTag(String typeName) {
	  this.otype = typeName; 
  }
	
	public String getUnit() { return this.unit ; }
	public void setUnit(String unit) { this.unit = unit ; }

	public String getOType() { return otype ; }
	
	public boolean isTypeOf(String type) {
		if("quantity".equals(type)) return true ;
		return otype.equals(type) ;
	}
}
