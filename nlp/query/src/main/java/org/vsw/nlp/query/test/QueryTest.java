package org.vsw.nlp.query.test;

import org.vsw.nlp.query.Query;
import org.vsw.nlp.test.NLPTestContext;

public class QueryTest {
	private String   description ;
	private Query  query ;
	private Sample[] sample ;
	
	public String getDescription() { return this.description ;}
	public void setDescription(String desc) { this.description = desc ; }

	public Query getQuery() { return this.query ; }
	public void setQuery(Query query) { this.query =  query ; }
	
	public Sample[] getSample() { return this.sample ; }
	public void setSample(Sample[] sample) { this.sample = sample ; }

	public void compile(NLPTestContext context) throws Exception {
		this.query.compile() ;
	}
	
	public void run(NLPTestContext context) throws Exception {
		for(Sample sel : sample) {
			sel.run(query, context) ;
		}
	}
}
