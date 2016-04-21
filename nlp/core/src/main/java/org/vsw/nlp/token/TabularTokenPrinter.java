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

import java.io.PrintStream;
import java.util.List;

import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.CharacterSet;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TabularTokenPrinter extends TokenPrinter {
	private String[] printTag;
	
	public TabularTokenPrinter() { }
	
	public TabularTokenPrinter(String[] printTag) { 
		this.printTag = printTag ;
	}
	
	public void printCollection(PrintStream out, TokenCollection[] collection) {
		for(int i = 0; i < collection.length; i++) {
			print(out, collection[i]) ;
		}
	}

	public void print(PrintStream out, TokenCollection[] collection) {
		for(int i = 0; i < collection.length; i++) {
			print(out, collection[i].getTokens()) ;
			out.println();
		}
	}

	public void print(PrintStream out, IToken[] token) {
		for(int i = 0; i < token.length; i++) {
			print(out, token[i]) ;
		}
	}

	private void print(PrintStream out, IToken token) {
		String label = token.getOriginalForm() ;
		if(label.length() == 1 && CharacterSet.isIn(label.charAt(0), CharacterSet.NEW_LINE)) {
			label = "NEW LINE" ;
		}
		printCol(out, label, 25) ;
		List<Tag> tags = token.getTag() ;
		if(tags != null) {
			for(Tag sel : tags) {
				if(isPrintTag(sel)) {
					printCol(out, sel) ;
					out.print(' ');
				}
			}
		}
		out.println() ;
	}

	private void printCol(PrintStream out, String label, int width) {
		out.print(label) ;
		for(int i = label.length(); i < width; i++) {
			out.print(' ') ;
		}
	}

	protected void printCol(PrintStream out, Tag sel) {
		out.append(sel.getInfo()) ;
	}
	
	private boolean isPrintTag(Tag sel) {
		if(printTag == null) return true ;
		return StringUtil.isIn(sel.getOType(), printTag) ;
	}
}
