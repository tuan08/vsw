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

import org.vsw.nlp.ml.crf.pos.tag.PosTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.LexiconTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class ITokenMatcher {
	private IToken token ;
	private boolean mandatory = false ;
	
	public ITokenMatcher(IToken token) {
		this.token = token ;
		String oform = token.getOriginalForm();
		if(Character.isUpperCase(oform.charAt(0))) {
			mandatory = true; 
		} else {
			LexiconTag ltag = token.getFirstTagType(LexiconTag.class) ;
			if(ltag != null) {
				PosTag ptag = token.getFirstTagType(PosTag.class) ;
				String pos = ptag.getTagValue() ;
				if("pos:Np".equals(pos) || "pos:N".equals(pos)) {
					mandatory = true; 
				}
			}
		}
	}
	
	public boolean isMandatory() { return mandatory ; }
	
	public boolean isCandidate(IToken[] token, int pos) {
		return this.token.getNormalizeForm().equals(token[pos].getNormalizeForm())  ;
	}
	
	public ITokenMatchInfo matches(IToken[] token, int pos) {
		if(this.token.getNormalizeForm().equals(token[pos].getNormalizeForm())) {
			ITokenMatchInfo info = new ITokenMatchInfo(this.token, token[pos], pos) ;
			return info ;
		}
		return null ;
	}
}
