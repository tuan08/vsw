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

import java.util.List;

import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Token extends IToken {
	private String[] word ;
	private String   orginalForm ;
	private String   normalizeForm ;
	private char[]   normalizeFormBuf ;
	
	public Token(String originalForm) {
		reset(originalForm, false) ;
	}
	
	public Token(String originalForm, boolean split) {
		reset(originalForm, split) ;
	}
	
	public Token(IToken[] token, int from, int to) {
		StringBuilder b = new StringBuilder() ;
		for(int i = from; i < to ; i++) {
			String oform = token[i].getOriginalForm() ;
			if(i > from) {
				b.append(' ') ;
			}
			b.append(oform) ;
			
		}
		this.orginalForm = b.toString() ;
		this.normalizeForm = orginalForm.toLowerCase() ;
		List<String> holder = StringUtil.split(this.normalizeForm, ' ') ;
		this.word = holder.toArray(new String[holder.size()]) ;
	}
	
	public Token(String[] token, int from, int to) {
		StringBuilder b = new StringBuilder() ;
		for(int i = from; i < to ; i++) {
			String oform = token[i] ;
			if(i > from) {
				b.append(' ') ;
			}
			b.append(oform) ;
			
		}
		this.orginalForm = b.toString() ;
		this.normalizeForm = orginalForm.toLowerCase() ;
		List<String> holder = StringUtil.split(this.normalizeForm, ' ') ;
		this.word = holder.toArray(new String[holder.size()]) ;
	}
	
	public String[] getWord() { return word ; }
	
	public String getOriginalForm() { return this.orginalForm ; }
	
	public String getNormalizeForm() { return this.normalizeForm ; }

	public char[] getNormalizeFormBuf() { 
		if(normalizeFormBuf == null) normalizeFormBuf = normalizeForm.toCharArray() ;
		return normalizeFormBuf ; 
	}
	
	public void reset(String text, boolean split) {
		this.orginalForm = text ;
		this.normalizeForm = orginalForm.toLowerCase() ;
		if(split) {
			List<String> holder = StringUtil.split(this.normalizeForm, ' ') ;
			this.word = holder.toArray(new String[holder.size()]) ;
		} else  {
			this.word = new String[] {this.normalizeForm} ;
		}
	}
	
	public void reset(String text) {
		reset(text, false);
	}
}
