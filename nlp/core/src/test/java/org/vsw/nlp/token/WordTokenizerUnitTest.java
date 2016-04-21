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

import org.junit.Test;
import org.vsw.nlp.test.TokenVerifier;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WordTokenizerUnitTest {
	@Test
	public void testWordTokenizer() throws Exception {
		new Verifier(
		  "this is a test",
		  "this | is | a | test"
		).verify() ;
		new Verifier(
			"this 'is'a test",
			"this | ' |is | ' | a | test"
		).verify() ;
		
		new Verifier(
			"a>b a > b",
		  "a | > | b | a | > | b"
		).verify() ;
		new Verifier(
		  "Mr. Test 1 2 3 4.",
			"Mr. | Test | 1 | 2 | 3 | 4."
		).verify() ;
	}

	static public class Verifier {
		private String text ;
		private TokenVerifier[] verifier ;
		
		Verifier(String text, String expects) {
			this.text = text ;
			String[] expect = expects.split("\\|") ;
			verifier = new TokenVerifier[expect.length] ;
			for(int i = 0; i < verifier.length; i++) {
			  verifier[i] = new TokenVerifier(expect[i].trim()) ;
			}
		}
		
		public void verify() throws Exception {
			WordTokenizer tokenizer = new WordTokenizer(text) ;
			IToken[] token = tokenizer.allTokens() ;
			for(int i = 0; i < token.length; i++) {
				verifier[i].verify(token[i]) ;
			}
		}
	}
}