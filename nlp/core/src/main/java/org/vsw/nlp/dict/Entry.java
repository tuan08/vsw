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
package org.vsw.nlp.dict;

import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.StringPool;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Entry {
	private String[] word ;
	private String   name ;
	private Tag[]    tag ;
	
	public Entry(String name, String[] word) {
		this.name = name ;
		this.word = word ;
	}
	
	public String[] getWord() { return word; }
	public void setWord(String[] word) { this.word = word; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public void add(Tag tag) {
		if(tag == null) return ;
		if(this.tag == null) {
			this.tag = new Tag[] { tag } ;
		} else {
			Tag[] tmp = new Tag[this.tag.length + 1] ;
			System.arraycopy(this.tag, 0, tmp, 0, this.tag.length) ;
			tmp[this.tag.length] = tag ;
			this.tag = tmp ;
		}
	}
	public Tag[] getTag() { return tag; }
	public void setTag(Tag[] tag) { this.tag = tag; } 
	
	public <T extends Tag> T getFirstTagType(Class<T> type) {
		if(type == null || tag == null) return null ;
		for(Tag sel : tag) if(type.isInstance(sel)) return type.cast(sel) ;
		return null ;
	}
	
	public void optimize(StringPool pool) {
		word = pool.getString(word) ;
		name = pool.getString(name) ;
		if(tag != null) {
			for(int i = 0; i < tag.length; i++) {
				tag[i].optimize(pool) ;
			}
		}
	}
}
