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
package org.vsw.nlp.ml.crf.sent;

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
public class SentTargetFeatureGenerator implements FeatureGenerator {
	
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		char[] character  = token[pos].getNormalizeFormBuf() ;
		if(character.length == 1 && CharacterSet.isIn(character[0], CharacterSet.END_SENTENCE)) {
			holder.setTargetFeature("eos") ;
		} else if(character.length == 3 && "...".equals(token[pos].getNormalizeForm())) {	
			holder.setTargetFeature("eos") ;
		} else {
			holder.setTargetFeature("O") ;
		}
	}	
}