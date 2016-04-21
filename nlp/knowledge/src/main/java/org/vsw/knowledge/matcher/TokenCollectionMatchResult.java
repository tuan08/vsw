/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.knowledge.matcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.vsw.nlp.ml.crf.ent.tag.EntityTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TokenCollectionMatchResult {
	final static public Comparator<TokenCollectionMatchResult> SCORE_COMPARATOR = new  Comparator<TokenCollectionMatchResult>() {
    public int compare(TokenCollectionMatchResult o1, TokenCollectionMatchResult o2) {
	    if(o1.score < o2.score) return 1 ;
	    else if(o1.score > o2.score) return -1 ;
    	return 0;
    }
	} ;
	
	private TokenCollection   collection ;
	private ITokenMatchInfo[] info ;
	private float score ;
	
	public TokenCollectionMatchResult(TokenCollection collection, ITokenMatchInfo[] info, float score) {
		this.collection = collection ;
		this.info = info ;
		this.score = score ;
	}
	
	public float getScore() { return this.score ; }
	
	public TokenCollection getTokenCollection() { return this.collection ; }
	
	public int getMatchFrom() { return info[0].getCandidateTokenPosition() ; }
	
	public int getMatchTo() { return info[info.length - 1].getCandidateTokenPosition() ; }
	
	public List<MatchEntity> getMatchEntity(String entityType) {
		IToken[] token = collection.getTokens() ;
		List<MatchEntity> holder = null ;
		for(int i = 0; i < token.length; i++) {
			EntityTag tag = token[i].getFirstTagType(EntityTag.class) ;
			if(tag != null && tag.getEntity().equals(entityType)) {
				if(holder == null) holder = new ArrayList<MatchEntity>() ;
				holder.add(new MatchEntity(token[i], getMatchDistance(i))) ;
			}
		}
		return holder ;
	}
	
	public String getMatchBlock(int expand) {
		IToken[] token = collection.getSingleTokens() ;
		int from = getMatchFrom() - expand;
		if(from < 0) from = 0 ;
		int to = getMatchTo() + expand + 1;
		if(to > token.length) to = token.length ;
		StringBuilder b = new StringBuilder() ;
		for(int i = from; i < to; i++) {
			if(i > 0) b.append(" ") ;
			b.append(token[i].getOriginalForm()) ;
		}
		return b.toString() ;
	}
	
	private int getMatchDistance(int pos) {
		int from = getMatchFrom() ;
		int to   = getMatchTo() ;
		if( pos < from) return from - pos ;
		if(pos >= from && pos <= to) return 0 ;
		return pos - to ;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder() ;
		for(int i = 0; i < info.length; i++) {
			if(i > 0) b.append(" .. ") ;
			b.append(info[i].getCandidateToken().getOriginalForm()) ;
		}
		return b.toString() ;
	}
	
	static public class MatchEntity {
		private IToken token ;
		private int    distance ;
	
		MatchEntity(IToken token, int distance) {
			this.token = token ;
			this.distance = distance ;
		}
		
		public IToken getToken() { return this.token ; }
		
		public int getDistance() { return this.distance ; }
	}
}