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

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.vsw.util.IOUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NLPTestSuiteUnitTest {
	@Test
	public void testDeserializer() throws Exception {
		String JSON_SUITE = IOUtil.getFileContenntAsString("src/test/resources/nlptest.suite") ;
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		NLPTestSuite suite = mapper.readValue(JSON_SUITE , NLPTestSuite.class);
		Assert.assertNotNull(suite.getResource()) ;
		Assert.assertNotNull(suite.getAnalyzer()) ;
		Assert.assertEquals(2, suite.getTest().length) ;
		Assert.assertEquals("token1 token2 token3", suite.getTest()[0].getSample()) ;
	}
}