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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TokenCollectionMatcher {
	private IToken[] token ;
	private ITokenMatcher[] tokenMatcher ;
	
	public TokenCollectionMatcher(IToken[] token) {
		this.token = token ;
		tokenMatcher = new ITokenMatcher[token.length] ;
		for(int i = 0; i < token.length; i++) {
			tokenMatcher[i] = new ITokenMatcher(token[i]) ;
		}
	}
	
	public boolean isCandidate(TokenCollection collection) {
		IToken[] token = collection.getTokens() ;
		for(ITokenMatcher selMatcher : tokenMatcher) {
			if(!selMatcher.isMandatory()) continue ;
			boolean matches = false ;
			for(int i = 0; i < token.length; i++) {
				if(selMatcher.isCandidate(token, i)) {
					matches = true ;
					break ;
				}
			}
			if(!matches) return false ;
		}
		return true  ;
	}
	
	public TokenCollectionMatchResult matches(TokenCollection collection) {
		IToken[] token = collection.getTokens() ;
		List<ITokenMatchInfo> holder = new ArrayList<ITokenMatchInfo>() ;
		for(ITokenMatcher selMatcher : tokenMatcher) {
			for(int i = 0; i < token.length; i++) {
				ITokenMatchInfo iTokenMatchInfo = selMatcher.matches(token, i) ;
				if(iTokenMatchInfo != null) {
					holder.add(iTokenMatchInfo) ;
				}
			}
		}
		ITokenMatchInfo[] info = holder.toArray(new ITokenMatchInfo[holder.size()]) ;
		Arrays.sort(info, ITokenMatchInfo.POSITION_COMPARATOR) ;
		info = removeDuplicate(info) ;
		float score = info.length/(float)tokenMatcher.length ;
		TokenCollectionMatchResult result = new TokenCollectionMatchResult(collection, info, score) ;
		return result ;
	}
	
	private ITokenMatchInfo[] removeDuplicate(ITokenMatchInfo[] info) {
		Map<String, ITokenMatchInfo> keepHolder = new HashMap<String, ITokenMatchInfo>() ; 
		for(int i = 0; i < info.length; i++) {
			String nform = info[i].getCandidateToken().getNormalizeForm() ;
			ITokenMatchInfo exist = keepHolder.get(nform) ;
			if(exist == null) {
				keepHolder.put(nform, info[i]) ;
			} else {
				int existSumDistance = getSumDistance(info, exist) ;
				int currentSumDistance = getSumDistance(info, info[i]) ;
				if(currentSumDistance < existSumDistance) {
					keepHolder.put(nform, info[i]) ;
				}
			}
		}
		if(keepHolder.size() < info.length) {
			List<ITokenMatchInfo> holder = new ArrayList<ITokenMatchInfo>() ;
			for(int i = 0; i < info.length; i++) {
				String nform = info[i].getCandidateToken().getNormalizeForm() ;
				ITokenMatchInfo keep = keepHolder.get(nform) ;
				if(keep.getCandidateTokenPosition() == info[i].getCandidateTokenPosition()) {
					holder.add(keep) ;
				}
			}
			info = holder.toArray(new ITokenMatchInfo[holder.size()]) ;
		}
		return info ;
	}
	
	private int getSumDistance(ITokenMatchInfo[] info, ITokenMatchInfo target) {
		int sum = 0;
		for(int i = 0; i < info.length; i++) {
			if(info[i] == target) continue ;
			sum += Math.abs(target.getCandidateTokenPosition() - info[i].getCandidateTokenPosition()) ;
		}
		return sum ;
	}
}
