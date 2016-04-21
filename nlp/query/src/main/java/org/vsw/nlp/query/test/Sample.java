package org.vsw.nlp.query.test;

import java.io.PrintStream;
import java.util.Iterator;

import org.vsw.nlp.query.QueryDataField;
import org.vsw.nlp.query.MatchResult;
import org.vsw.nlp.query.Query;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.test.NLPTestContext;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.ConsoleUtil;

public class Sample {
	private String[] data ;
	private String[] expect ;
	private SampleVerifier verifer ;
	
	public String[] getData() { return data; }
	public void setData(String[] data) { this.data = data; }
	
	public String[] getExpect() { return expect; }
	public void setExpect(String[] expect) { 
		this.expect = expect; 
		this.verifer = new SampleVerifier(expect); 
	}

	public void run(Query query, NLPTestContext context) throws Exception {
		QueryData document = new QueryData() ;
		TokenAnalyzer[] analyzer = context.getTokenAnalyzer() ;
		for(String sel : data) {
			int idx      = sel.indexOf(':') ;
			String fname = sel.substring(0, idx).trim() ;
			String data  = sel.substring(idx + 1).trim() ;
			document.add(fname, data, analyzer) ;
		}
		QueryContext qcontext = new QueryContext() ;
		query.query(qcontext, document) ;
		try {
			verifer.verify(qcontext) ;
		} catch(Exception ex) {
			Iterator<QueryDataField> i = document.getDocumentFields().values().iterator() ;
			PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
			while(i.hasNext()) {
				QueryDataField field = i.next() ;
				out.append("field ").append(field.getName()).append(": ") ;
				IToken[] tokens = field.getTokens() ;
				for(int j = 0; j < tokens.length; j++) {
					if(j > 0) out.append(" | ") ;
					out.append(tokens[j].getOriginalForm()) ;
				}
				out.println();
			}
			if(qcontext.getMatchResults() != null) {
  			for(MatchResult sel : qcontext.getMatchResults()) {
  				out.append("match result: ").println(sel.getMatchTokenSequence().getSegmentTokens()) ;
  			}
  		}
			throw ex ;
		}
	}
}
