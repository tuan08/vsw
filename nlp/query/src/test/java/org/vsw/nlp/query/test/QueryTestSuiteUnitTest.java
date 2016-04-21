package org.vsw.nlp.query.test;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.vsw.util.IOUtil;

public class QueryTestSuiteUnitTest {
	@Test
	public void testSerializer() throws Exception {
		String JSON_SUITE = IOUtil.getFileContenntAsString("src/data/test/PlaceQueriesSuite.json") ;
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		QueryTestSuite suite = mapper.readValue(JSON_SUITE , QueryTestSuite.class);
		Assert.assertNotNull(suite.getResource()) ;
		Assert.assertNotNull(suite.getAnalyzer()) ;
		Assert.assertEquals(1, suite.getTest().length) ;
	}
}
