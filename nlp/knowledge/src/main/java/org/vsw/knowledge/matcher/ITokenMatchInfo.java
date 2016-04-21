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

import java.util.Comparator;

import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class ITokenMatchInfo {
	final static public PositionComparator POSITION_COMPARATOR = new PositionComparator() ;
	
	private IToken token ;
	private IToken candidateToken ;
	private int    position       ;
	private float  score          ;
	
	public ITokenMatchInfo(IToken token, IToken candidateToken, int pos) {
		this.token = token ;
		this.candidateToken = candidateToken ;
		this.position = pos ;
	}
	
	public IToken getToken() { return token ; }
	
	public IToken getCandidateToken() { return this.candidateToken ; }
	
	public int getCandidateTokenPosition() { return this.position ; }
	
	public float getScore() { return this.score ; }
	
	static class PositionComparator implements Comparator<ITokenMatchInfo> {
    public int compare(ITokenMatchInfo o1, ITokenMatchInfo o2) {
	    return o1.position - o2.position ;
    }
	}
}