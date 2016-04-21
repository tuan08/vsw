package org.vsw.nlp.query.test;

import org.vsw.nlp.meaning.Synset;
import org.vsw.nlp.test.NLPTestContext;
import org.vsw.nlp.token.tag.SynsetTag;

public class QueryTestSuite {
	private String[]  resource ;
	private String[]  analyzer ;
	private Synset[]  synset ;
	private QueryTest[] test ;
	
	public String[] getResource() { return this.resource ; }
	public void     setResource(String[] resource) { this.resource = resource ; }
	
	public String[] getAnalyzer() { return analyzer; }
	public void setAnalyzer(String[] analyzer) { this.analyzer = analyzer; }
	
	public Synset[] getSynset() { return this.synset ; }
	public void setSynset(Synset[] array) { this.synset = array ; }
	
	
	public QueryTest[] getTest() { return test; }
	public void setTest(QueryTest[] test) { this.test = test; }

	
	public void run() throws Exception {
		NLPTestContext context = new NLPTestContext(resource, analyzer) ;
		if(synset != null) {
			for(Synset sel : synset) {
				context.getDictionaries().getDictionaryLexicon().add(new SynsetTag(sel));
			}
		}
		for(QueryTest sel : test) sel.compile(context) ;
		for(QueryTest sel : test) sel.run(context) ;
	}
}
