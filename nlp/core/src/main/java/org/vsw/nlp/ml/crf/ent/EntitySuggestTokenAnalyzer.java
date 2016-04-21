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
package org.vsw.nlp.ml.crf.ent;

import org.vsw.nlp.ml.SuggestTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class EntitySuggestTokenAnalyzer implements TokenAnalyzer {
	EntityTokenAnalyzer entityAnalyzer ;
	
	public EntitySuggestTokenAnalyzer(EntityTokenAnalyzer entityAnalyzer) {
		this.entityAnalyzer = entityAnalyzer ;
	}
	
	public IToken[] analyze(IToken[] token) throws TokenException {
		String[] tag = entityAnalyzer.tags(token) ;
		for(int i = 0; i < token.length; i++) {
			if(!"O".equals(tag[i])) {
				token[i].add(new SuggestTag(tag[i])) ;
			}
		}
		return token;
	}

	protected void setEntityTag(String type, IToken token) {}
}
