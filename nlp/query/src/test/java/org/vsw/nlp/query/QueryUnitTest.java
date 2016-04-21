package org.vsw.nlp.query;

import org.junit.Test;
import org.vsw.nlp.query.Query;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.test.Util;

public class QueryUnitTest extends BaseQueryUnitTest {
	@Test
	public void test() throws Exception {
		Query query=  Util.getInstance(LIENHE_QUERY, Query.class);
		query.compile() ;
		
		QueryData document = createDocument(
	    "title: this is a document title",
	    "body:  liên hệ 186 trương định quận hai bà trưng hà nội việt nam"
		) ;
		QueryContext context = new QueryContext() ;
		query.query(context, document) ;
		//System.out.println(context) ;
	}
}
