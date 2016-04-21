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

import org.junit.Assert;
import org.junit.Test;
import org.vsw.nlp.token.Token;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WordTreeUnitTest {
	@Test
	public void test() throws Exception {
		String[] words = {"Ha Noi", "Viet Nam", "chien tranh", "chien tranh Viet Nam"} ;
		DictionaryLexicon dictLexicon = new DictionaryLexicon() ;
		for(String sel : words) {
			dictLexicon.add(sel, null) ;
		}
		WordTree wordTree = dictLexicon.getWordTree() ;
		wordTree.dump(System.out, "") ;
		
		String text = "cuoc chien tranh viet nam nam 72 tai Ha Noi" ;
		String[] word = text.split(" ") ;
		Token[] token = new Token[word.length] ;
		for(int i = 0; i < word.length; i++) token[i] = new Token(word[i]) ;
		Assert.assertEquals("chien tranh Viet Nam", wordTree.matches(token, 1).getEntry().getName()) ;
		Assert.assertEquals("Ha Noi", wordTree.matches(token, 8).getEntry().getName()) ;
	}
}