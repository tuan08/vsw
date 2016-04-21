package org.vsw.nlp.query;

import org.vsw.nlp.token.TokenCollection;

public class MatchResult {
	private TokenCollection tsequence ;
	private int from ;
	private int to   ;
	
	public MatchResult(TokenCollection tsequence, int from, int to) {
		this.tsequence = tsequence ;
		this.from = from ;
		this.to   = to   ;
	}
	
	public TokenCollection getMatchTokenSequence() { return this.tsequence ; }

	public String getMatch(int from , int to) {
		return tsequence.getOriginalForm(from, to);
	}
	
	public int getFrom() { return this.from ; }
	
	public int getTo() { return this.to ; }
}
