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
import org.vsw.nlp.util.StringPool;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Synset implements Model {
	final static public String TYPE = "synset" ;
	
	private String   name ;
	private String[] variant ;
	private String[] type ;

	@JsonIgnore
	public String getId() { return name ; }
	
	public String getName() { return name; }
	public void   setName(String name) { this.name = name; }

	public String[] getVariant() { return variant; }
	public void setVariant(String[] variant) { 
		if(variant == null || variant.length == 0) {
			this.variant = null ;
		} else {
		  this.variant = variant;
		}
	}

	public String[] getType() { return type; }
	public void     setType(String[] type) { this.type = type; }

	public String getOType() { return TYPE ; }
	public void   setOType(String type) { } 
	
	public void optimize(StringPool pool) {
		name = pool.getString(name) ;
		variant = pool.getString(variant) ;
		this.type = pool.getString(type) ;
	}
}