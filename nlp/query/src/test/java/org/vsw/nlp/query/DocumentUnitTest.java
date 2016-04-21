package org.vsw.nlp.query;

import org.junit.Test;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.test.TokenVerifier;
import org.vsw.nlp.test.Util;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

public class DocumentUnitTest {
	@Test
	public void testDocument() throws Exception {
		QueryData document = new QueryData() ;
		TokenAnalyzer[] analyzer = Util.getTextAnalyzer(new Dictionaries()) ;
		document.add("title", "this is a document title", analyzer) ;
		document.add("desc", "this is a document description", analyzer) ;
		document.add("body", "body 0988922860 tuan08@gmail.com 125 ngàn", analyzer) ;
	
		new Verifier(
				document,	"title",
			  "this | is | a | document | title"
		).verify() ;
		
		new Verifier(
			document,	"body",
			"body | 0988922860{phone} | tuan08@gmail.com{email} | 125{currency} | ngàn"
		).verify() ;
	}
	
	static public class Verifier {
		private QueryData doc ;
		private String field ;
		private TokenVerifier[] verifier ;
		
		Verifier(QueryData doc, String field, String expects) {
			this.doc = doc ;
			this.field = field ;
			String[] expect = expects.split("\\|") ;
			verifier = new TokenVerifier[expect.length] ;
			for(int i = 0; i < verifier.length; i++) {
			  verifier[i] = new TokenVerifier(expect[i].trim()) ;
			}
		}
		
		public void verify() throws Exception {
			IToken[] token = doc.getDocumentField(field).getTokens() ;
			for(int i = 0; i < token.length; i++) {
				verifier[i].verify(token[i]) ;
			}
		}
	}
}
