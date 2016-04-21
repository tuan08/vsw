package org.vsw.extract;

import org.vsw.extract.io.Reader;
import org.vsw.nlp.ml.dict.LongestMatchingAnalyzer;
import org.vsw.nlp.query.Query;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.query.QueryDataIterator;
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

abstract public class Processor {
	private Reader reader  ;
	private Query[] queries ;
	protected TokenAnalyzer[] analyzer ;
	private int limit = 100000000;
	
	protected Processor() {}
	
	public Processor(Reader reader) throws Exception {
		init(reader) ;
	}
	
	protected void init(Reader reader) throws Exception {
		this.reader = reader ;
		QueryResourceLoader loader = new QueryResourceLoader() ;
		onLoad(loader) ;
		analyzer = new TokenAnalyzer[] {
				PunctuationTokenAnalyzer.INSTANCE, new CommonTokenAnalyzer(), 
				new LongestMatchingAnalyzer(loader.getDictionaries()), 
				new VNNameTokenAnalyzer(), new NameTokenAnalyzer(),
				new DateTokenAnalyzer(), new TimeTokenAnalyzer(), 
				new VNDTokenAnalyzer(), new USDTokenAnalyzer(),
				new VNPhoneTokenAnalyzer(), new VNMobileTokenAnalyzer(),
				new EmailTokenAnalyzer()
		};
		this.queries = loader.getQueryArray() ;
	}
	
	public void setLimit(int limit) { this.limit = limit ; }
	
	public TokenAnalyzer[] getTokenAnalyzer() { return this.analyzer ; }
	
	protected void onLoad(QueryResourceLoader loader) throws Exception {
		
	}
	
	abstract protected QueryDataIterator createQueryDataIterator(Document doc) throws Exception ;
	
	public void process() throws Exception {
		QueryContext context = new QueryContext() ;
		Document doc = null ;
		int count = 0;
		while(count < limit && (doc = reader.next()) != null) {
			QueryDataIterator i = createQueryDataIterator(doc) ;
			QueryData queryData = null ;
			while((queryData = i.next()) != null) { 
				for(Query query : queries) {
					context.setComplete(false) ;
					query.query(context, queryData) ;
				}
				onPost(doc, queryData, context) ;
			}
			onPost(doc, context) ;
			context.reset() ;
			if(count > 1 && count % 1000 == 0) System.out.print(".") ;
			count++ ;
		}
		System.out.println() ;
		reader.close() ;
	}

	protected void onPost(Document doc, QueryData data, QueryContext context) throws Exception {
		
	}
	
  protected void onPost(Document doc, QueryContext context) throws Exception {
		
	}
}