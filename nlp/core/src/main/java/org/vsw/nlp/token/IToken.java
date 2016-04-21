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
package org.vsw.nlp.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.vsw.nlp.token.tag.MeaningTag;
import org.vsw.nlp.token.tag.Tag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class IToken {
	private List<Tag> tag ;
	
	abstract public String[] getWord() ;
	abstract public String   getOriginalForm() ;
	abstract public String   getNormalizeForm() ;
	abstract public char[]   getNormalizeFormBuf() ;

	public void reset(String text) {
		throw new RuntimeException("Method is not supported") ;
	}
	
	public boolean hasTag() { return tag != null && tag.size() > 0 ; }
	public List<Tag> getTag() { return this.tag ; }
	public void  setTag(List<Tag> tag) { this.tag = tag ; }
	
	public boolean hasTagType(String type) {
		if(tag == null) return false ; 
		for(int i = 0; i < tag.size(); i++) {
			if(tag.get(i).isTypeOf(type)) return true ;
		}
		return false ;
	}
	
	public boolean hasTagType(String[] type) {
		if(tag == null) return false ; 
		for(Tag sel : tag) {
			for(String selType : type) {
				if(sel.isTypeOf(selType)) return true ;
			}
		}
		return false ;
	}
	
	public void add(Tag tag) {
		if(tag == null) return ;
		if(this.tag == null) this.tag = new ArrayList<Tag>(3) ;
		this.tag.add(tag) ;
	}
	
	public void add(Tag[] tag) {
		if(tag == null) return ;
		if(this.tag == null) this.tag = new ArrayList<Tag>(5) ;
		for(int i = 0; i < tag.length; i++) this.tag.add(tag[i]) ;
	}
	
	public Tag getFirstTagType(String type) {
		if(tag == null) return null ; 
		for(Tag sel : tag) if(sel.isTypeOf(type)) return sel ;
		return null ;
	}
	
	public <T extends Tag> T getFirstTagType(Class<T> type) {
		if(type == null || tag == null) return null ;
		for(Tag sel : tag) if(type.isInstance(sel)) return type.cast(sel) ;
		return null ;
	}
	
	public <T extends Tag> HashSet<String> getMeaningTypes() {
		if(tag == null) return null ;
		HashSet<String> set = null ;
		for(Tag sel : tag) { 
			if(sel instanceof MeaningTag) {
				if(set == null) set = new HashSet<String>() ;
				set.add(((MeaningTag)sel).getOType()) ;
			}
		}
		return set ;
	}
	
	public <T extends Tag> void removeTagType(Class<T> type) {
		if(type == null || tag == null) return  ;
		Iterator<Tag> i = tag.iterator() ;
		while(i.hasNext()) {
			if(type.isInstance(i.next())) {
				i.remove() ;
			}
		}
	}
	
	public <T extends Tag> void removeTagType(Class<T>[] type) {
		if(type == null || tag == null) return  ;
		Iterator<Tag> i = tag.iterator() ;
		while(i.hasNext()) {
			for(Class<T> sel : type) {
			  if(sel.isInstance(i.next())) {
			  	i.remove() ;
			  	break ;
			  }
			}
		}
	}
	
	public <T extends Tag> List<T> getTagByType(String type) {
		if(tag == null) return null ; 
		List<T> holder = new ArrayList<T>(tag.size()) ;
		for(Tag sel : tag) {
			if(sel.isTypeOf(type)) holder.add((T)sel) ;
		}
		return holder ;
	}
	
	public <T extends Tag> List<T> getTagByType(String[] type) {
		if(tag == null) return null ; 
		List<T> holder = new ArrayList<T>(tag.size()) ;
		for(Tag sel : tag) {
			for(String selType : type) {
			  if(type.equals(sel.isTypeOf(selType))) {
			  	holder.add((T)sel) ;
			  	break ;
			  }
			}
		}
		return holder ;
	}
	
	public void clearTag() { tag = null ; }
}