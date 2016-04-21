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
package org.vsw.nlp.test;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.WordTokenizer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NLPTest {
	private String   description ;
	private String   sample ;
	private String[] expect ;
	private TokenVerifier[] tverifier ;
	
	public String getDescription() { return this.description ;}
	public void setDescription(String desc) { this.description = desc ; }
	
	public String getSample() { return sample; }
	public void setSample(String sample) { this.sample = sample; }
	
	public String[] getExpect() { return expect; }
	public void     setExpect(String[] expect) { this.expect = expect; }

	public void compile() throws Exception {
		tverifier = new TokenVerifier[expect.length] ;
		for(int i = 0; i < expect.length; i++) {
			tverifier[i] = new TokenVerifier(expect[i]) ;
		}
	}
	
	public void run(NLPTestContext context) throws Exception {
		WordTokenizer tokenizer = new WordTokenizer(sample) ;
		TokenAnalyzer[] analyzer = context.getTokenAnalyzer() ;
		IToken[] token = tokenizer.allTokens() ;
		for(TokenAnalyzer sel : analyzer) {
			token = sel.analyze(token) ;
		}
		if(tverifier.length != token.length) {
			for(int i = 0; i < token.length; i++) {
				if(i > 0) System.out.print(" | ") ;
				System.out.print(token[i].getOriginalForm()) ;
			}
			throw new Exception(sample + ", expect " + tverifier.length + " token but " + token.length) ;
		}
		for(int i = 0; i < token.length; i++) {
			tverifier[i].verify(token[i]) ;
		}
	}	
}