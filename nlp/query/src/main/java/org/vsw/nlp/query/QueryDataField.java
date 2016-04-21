package org.vsw.nlp.query;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.WordTokenizer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

public class QueryDataField {
	private String   name ;
	private String   data ;
	private IToken[] tokens ;
	private TokenAnalyzer[] analyzer ;
	
	public QueryDataField(String name, String data) throws TokenException {
		this.name = name ;
		this.data = data ;
	}
	
	public QueryDataField(String name, String data, TokenAnalyzer[] analyzer) throws TokenException {
		this.name = name ;
		this.data = data ;
		this.analyzer = analyzer ;
	}
	
	public String getName() { return this.name ; }
	
	public IToken[] getTokens() throws TokenException { 
		if(tokens == null) {
			if(data == null) {
				tokens = new IToken[0] ;
			} else {
				WordTokenizer tokenizer = new WordTokenizer(data) ;
				this.tokens = tokenizer.allTokens() ;
				if(this.analyzer != null) {
					for(int i = 0; i < analyzer.length; i++) {
						this.tokens = analyzer[i].analyze(this.tokens) ;
					}
				}
			}
		}
		return this.tokens ; 
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder() ;
		b.append(this.name).append(": ").append(this.data) ;
		return b.toString() ;
	}
}