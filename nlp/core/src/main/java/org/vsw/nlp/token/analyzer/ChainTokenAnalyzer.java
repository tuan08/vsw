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
package org.vsw.nlp.token.analyzer;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class ChainTokenAnalyzer implements TokenAnalyzer {
	static private TokenAnalyzer[] COMMON_CHAIN = {
		new PunctuationTokenAnalyzer() 
	} ;
	
	final static public ChainTokenAnalyzer DEFAULT = new ChainTokenAnalyzer(COMMON_CHAIN) ;
	
	private TokenAnalyzer[] chain ;
	
  public ChainTokenAnalyzer(TokenAnalyzer ... chain) {
  	this.chain = chain ;
  }
  
	public IToken[] analyze(IToken[] tokens) throws TokenException {
		for(TokenAnalyzer sel : chain) {
  		tokens = sel.analyze(tokens) ;
  	}
		return tokens ;
  }
	
}