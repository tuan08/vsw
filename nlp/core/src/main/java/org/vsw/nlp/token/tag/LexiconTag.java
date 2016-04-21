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

import org.vsw.nlp.meaning.Lexicon;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class LexiconTag extends WordTag {
	final static public String TYPE   = "lexicon" ;
	
	private Lexicon lexicon ;
	
	public LexiconTag(Lexicon lexicon) {
		super("lexicon");
		this.lexicon = lexicon ;
	}
	
	public Lexicon getLexicon() { return this.lexicon ; }
	public void    setLexicon(Lexicon lexicon) { this.lexicon = lexicon ; }
	
	public boolean hasTag(String name) {
		for(String sel : lexicon.getTag()) {
			if(sel.equals(name)) return true ;
		}
		return false ;
	}

	public String[] getPosTag() { return lexicon.getTag() ; }
	public String getTagValue() { return lexicon.getName() ; }

	public String getInfo() {
		StringBuilder b = new StringBuilder() ;
		b.append(getOType()).append(": {") ;
		String[] tag = lexicon.getTag() ;
		for(int i = 0; i < tag.length; i++) {
			if(i > 0) b.append(", ") ;
			b.append(tag[i]) ;
		}
		b.append("}") ;
		return b.toString() ;
	}
}