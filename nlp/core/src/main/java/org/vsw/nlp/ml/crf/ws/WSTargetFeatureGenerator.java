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
package org.vsw.nlp.ml.crf.ws;

import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/

public class WSTargetFeatureGenerator implements FeatureGenerator {
	final static public String BWORD = "B_W" ;
	final static public String IWORD = "I_W" ;
	
	public WSTargetFeatureGenerator() {
	}

	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		char[] buf = token[pos].getNormalizeFormBuf() ;
		if(CharacterSet.isNewLine(buf[0])){
			holder.setTargetFeature("O") ;
			return ;
		}
		
		if(buf.length == 1 && CharacterSet.isIn(buf[0], CharacterSet.PUNCTUATION) && 
			 token[pos].hasTagType(BoundaryTag.TYPE)) {
			holder.setTargetFeature("O") ;
			return ;
		}
		BoundaryTag btag = token[pos].getFirstTagType(BoundaryTag.class) ;
		for(String sel : btag.getFeatures()) {
			if(sel.equals(BWORD) || sel.equals(IWORD)) {
				holder.setTargetFeature(sel) ;
				break ;
			}
		}
	}	
}