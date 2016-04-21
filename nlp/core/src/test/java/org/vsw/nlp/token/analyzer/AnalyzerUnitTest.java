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

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.vsw.nlp.test.NLPTestSuite;
import org.vsw.util.IOUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class AnalyzerUnitTest {
	@Test
	public void testWordTokenizer() throws Exception {
		run("src/data/test/CommonTokenAnalyzer.suite") ;
		run("src/data/test/DateTokenAnalyzer.suite") ;
		run("src/data/test/EmailTokenAnalyzer.suite") ;
		run("src/data/test/GroupTokenMergerAnalyzer.suite") ;
		run("src/data/test/PunctuationTokenAnalyzer.suite") ;
		run("src/data/test/TimeTokenAnalyzer.suite") ;
		run("src/data/test/USDTokenAnalyzer.suite") ;
		run("src/data/test/VNDTokenAnalyzer.suite") ;
		run("src/data/test/VNMobileTokenAnalyzer.suite") ;
		run("src/data/test/VNPhoneTokenAnalyzer.suite") ;
		run("src/data/test/VNNameTokenAnalyzer.suite") ;
		run("src/data/test/MaxMatchingAnalyzer.suite") ;
	}
	
	static private void run(String file) throws Exception {
		String JSON = IOUtil.getFileContenntAsString(file, "UTF-8") ;
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		NLPTestSuite suite = mapper.readValue(JSON , NLPTestSuite.class);
		suite.run() ;
	}
}