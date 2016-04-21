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

import java.util.List;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CharacterTag extends Tag {
	final static public String TYPE   = "character" ;
	
	private CharDescriptor[] descriptors ;
	private String suffix ;
	
	public CharacterTag(CharDescriptor[] descriptors, String suffix) {
		this.descriptors = descriptors ;
		this.suffix = suffix ;
	}
	
	public String getSuffix() { return this.suffix ; }
	
	public CharacterTag(List<CharDescriptor> descriptors, String suffix) {
		this.descriptors = descriptors.toArray(new CharDescriptor[descriptors.size()]) ;
		this.suffix = suffix ;
	}
	
	public String getOType() { return TYPE ; }
	
	public CharDescriptor[] getCharDescriptors() { return this.descriptors ; }

	public CharDescriptor getCharDescriptors(char c) { 
		for(CharDescriptor sel : this.descriptors) {
			if(sel.character == c) return sel ;
		}
		return null ; 
	}
	
	public boolean isTypeOf(String type) { return TYPE.equals(type) ; }
	
	public String getInfo() {
		StringBuilder b = new StringBuilder() ;
		b.append(getOType()).append(": {") ;
		for(int i = 0; i < descriptors.length; i++) {
			if(i > 0) b.append(", ") ;
			b.append(descriptors[i].character).append("=").append(descriptors[i].count) ;
		}
		b.append("}") ;
		return b.toString() ;
	}
	
	static public class CharDescriptor {
		final public char character ;
		final public byte count ;
		
		public CharDescriptor(char character, byte count) {
			this.character = character ;
			this.count = count ;
		}
	}
}