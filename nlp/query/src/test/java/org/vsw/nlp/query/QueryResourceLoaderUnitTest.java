package org.vsw.nlp.query;

import org.junit.Test;

public class QueryResourceLoaderUnitTest {
	@Test
	public void test() throws Exception {
		QueryResourceLoader loader = new QueryResourceLoader() ;
		ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
		loader.load(cl.getResourceAsStream("nlp/vietnamese.lexicon.json"), "lexicon") ;
		loader.load(cl.getResourceAsStream("nlp/vn.place.json"), "place") ;
		loader.load(cl.getResourceAsStream("nlp/mobile.product.json"), "product") ;
		loader.load("src/test/resources/mix.json") ;
	}
}
