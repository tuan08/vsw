package org.vsw.nlp.query;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.vsw.nlp.query.test.QueryTestSuite;
import org.vsw.util.IOUtil;

public class SuiteUnitTest {
	@Test
	public void test() throws Exception {
		run("src/data/test/BasicMatcherSuite.json") ;
		run("src/data/test/PlaceQueriesSuite.json") ;
	}
		
	private void run(String file) throws Exception {
		String JSON_SUITE = IOUtil.getFileContenntAsString(file, "UTF-8") ;
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		QueryTestSuite suite = mapper.readValue(JSON_SUITE , QueryTestSuite.class);
		suite.run() ;
	}
}
