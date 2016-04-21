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
package org.vsw.nlp.ml;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.LexiconTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WTagHintGenerator extends TokenProcessor {
	static String TAG = "evt" ;
	
	private HashSet<String> prefix = new HashSet<String>() ;
	private HashSet<String> suffix = new HashSet<String>() ;
	private int maxDistance = 2;
	private String targetTag = TAG + ":" ;
	
  public WTagHintGenerator(String file) throws Exception {
	  super(file, false);
  }

	protected IToken[] process(IToken[] token, int pos) {
		if(!isTagToken(token, pos)) return new IToken[] {token[pos]} ;
	
		for(int i = pos; i >= pos - maxDistance; i--) {
			if(getLexiconTag(token, i) == null) continue ;
			prefix.add(token[i].getNormalizeForm()) ;
		}
		
		for(int i = pos + 1; i <= pos + maxDistance; i++) {
			if(getLexiconTag(token, i) == null) continue ;
			suffix.add(token[i].getNormalizeForm()) ;
		}
		return new IToken[] {token[pos]} ;
  }

	private boolean isTagToken(IToken[] token, int pos) {
		if(pos < 0) return false ;
		if(pos >= token.length) return false ;
		BoundaryTag btag = token[pos].getFirstTagType(BoundaryTag.class) ;
		if(btag == null) return false ;
		for(String selTag : btag.getFeatures()) {
			if(selTag.startsWith(targetTag)) return true ;
		}
		return false ;
	}
	
	private LexiconTag getLexiconTag(IToken[] token, int pos) {
		if(pos < 0) return null ;
		if(pos >= token.length) return null ;
		LexiconTag tag = token[pos].getFirstTagType(LexiconTag.class) ;
		return tag ;
	}

	public void dumpPrefix(PrintStream out) { dump(out, prefix) ; }
	public void dumpSuffix(PrintStream out) { dump(out, suffix) ; }
	
	void dump(PrintStream out, HashSet<String> set) {
		String[] array = set.toArray(new String[set.size()]) ;
		Arrays.sort(array) ;
		for(int i = 0; i < array.length; i++) {
			out.println(array[i]) ;
		}
		System.out.println("set = " + array.length);
	}
	
	static public void main(String[] args) throws Exception {
		WTagHintGenerator inspector = new WTagHintGenerator("d:/vswdata3/nlp/wtag") ;
		inspector.process() ;
		inspector.dumpPrefix(System.out) ;
		PrintStream out = new PrintStream(new FileOutputStream(TAG + ".prefix"), true, "UTF-8") ;
		inspector.dumpPrefix(out) ;
		out.close() ;
	}
}
