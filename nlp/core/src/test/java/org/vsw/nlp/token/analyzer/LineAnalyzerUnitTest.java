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
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.WordTokenizer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class LineAnalyzerUnitTest {
	@Test
	public void test() throws Exception {
		String line = "this is a test.\n\n this is the second phrase! \nthird phrase.\n\n" ;
		LineAnalyzer analyzer = new LineAnalyzer() ;
		IToken[] tokens = new WordTokenizer(line).allTokens() ;
		TokenCollection[] collections = analyzer.analyze(tokens) ;
		Assert.assertEquals(3, collections.length) ;
	}
}
