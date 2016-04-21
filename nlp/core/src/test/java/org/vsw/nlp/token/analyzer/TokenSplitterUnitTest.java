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
package org.vsw.nlp.token.analyzer;

import junit.framework.Assert;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TabularTokenPrinter;
import org.vsw.nlp.token.WordTokenizer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TokenSplitterUnitTest {
	@Test
	public void test() throws Exception {
		String line = 
			"P.9-Q.3 Mr. Tuan tiếng,việt 1-12-2003 MMX-2345 Xã Hội-Chủ Nghĩa 12km 1.2m 12,5 " +
			"U.S. trong,";
	  NLPResource resource = NLPResource.getInstance() ;
		IToken[] tokens = new WordTokenizer(line).allTokens() ;
		tokens = new CommonTokenAnalyzer().analyze(tokens) ;
		TokenSplitterAnalyzer analyzer = new TokenSplitterAnalyzer(resource) ;
		tokens = analyzer.analyze(tokens) ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(System.out, tokens) ;
		
		assertSplit(analyzer, "Mr. Tuan", new String[] {"Mr.", "Tuan"}) ;
		assertSplit(analyzer, "tiếng,việt", new String[] {"tiếng", ",", "việt"}) ;
		assertSplit(analyzer, "1-12-2003", new String[] {"1-12-2003"}) ;
		assertSplit(analyzer, "MMX-2345", new String[] {"MMX-2345"}) ;
		assertSplit(analyzer, "12km 1.2m 12,5", new String[] {"12", "km", "1.2", "m", "12,5"}) ;
		assertSplit(analyzer, "1,5km", new String[] {"1,5", "km"}) ;
		assertSplit(analyzer, "nam-nam", new String[] {"nam", "-", "nam"}) ;
		assertSplit(analyzer, "P.9-Q.3", new String[] {"P.9", "-", "Q.3"}) ;
		assertSplit(analyzer, "Xã Hội-Chủ Nghĩa", new String[] {"Xã", "Hội", "-", "Chủ", "Nghĩa"}) ;
	}
	
	private void assertSplit(TokenAnalyzer analyzer, String text, String[] expect) throws Exception {
		IToken[] tokens = new WordTokenizer(text).allTokens() ;
		tokens = new CommonTokenAnalyzer().analyze(tokens) ;
		tokens = analyzer.analyze(tokens) ;
		Assert.assertEquals(tokens.length, expect.length) ;
		for(int i = 0; i < tokens.length; i++) {
			Assert.assertEquals(tokens[i].getOriginalForm(), expect[i]) ;
		}
	}
}
