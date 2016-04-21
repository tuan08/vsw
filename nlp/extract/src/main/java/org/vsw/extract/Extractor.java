package org.vsw.extract;

import org.vsw.nlp.ml.dict.LongestMatchingAnalyzer;
import org.vsw.nlp.query.Query;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.query.QueryResourceLoader;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.DateTokenAnalyzer;
import org.vsw.nlp.token.analyzer.EmailTokenAnalyzer;
import org.vsw.nlp.token.analyzer.NameTokenAnalyzer;
import org.vsw.nlp.token.analyzer.PunctuationTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TimeTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.USDTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNDTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNMobileTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNNameTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNPhoneTokenAnalyzer;

public class Extractor {
	private TokenAnalyzer[] analyzer ;
	private Query[] queries ;
	
	public Extractor(QueryResourceLoader loader) {
		analyzer = new TokenAnalyzer[] {
				PunctuationTokenAnalyzer.INSTANCE, new CommonTokenAnalyzer(), 
				new LongestMatchingAnalyzer(loader.getDictionaries()), 
				new VNNameTokenAnalyzer(),  new NameTokenAnalyzer(),
				new DateTokenAnalyzer(),    new TimeTokenAnalyzer(), 
				new VNDTokenAnalyzer(),     new USDTokenAnalyzer(),
				new VNPhoneTokenAnalyzer(), new VNMobileTokenAnalyzer(),
				new EmailTokenAnalyzer()
		};
		this.queries = loader.getQueryArray() ;
	}
	
	public TokenAnalyzer[] getTokenAnalyzer() { return this.analyzer ; }
	
	protected void onLoadResource() throws Exception {
	}
	
	public void process(QueryContext context, QueryData document) throws Exception {
		for(Query query : queries) {
			context.setComplete(false) ;
			query.query(context, document) ;
		}
	}
}