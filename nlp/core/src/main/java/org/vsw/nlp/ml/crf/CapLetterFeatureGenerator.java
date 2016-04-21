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
package org.vsw.nlp.ml.crf;

import org.vsw.nlp.token.IToken;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CapLetterFeatureGenerator implements FeatureGenerator {
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		addFeature(token, pos - 2, "p2:", holder) ;
		addFeature(token, pos - 1, "p1:", holder) ;
		addFeature(token, pos,     "p0:", holder) ;
		addFeature(token, pos + 1, "n1:", holder) ;
		addFeature(token, pos + 2, "n2:", holder) ;
	}
	
	public void addFeature(IToken[] token, int pos, String prefix, FeatureHolder holder) {
		if(pos < 0) return ;
		if(pos >= token.length) return ;
		String orig = token[pos].getOriginalForm() ;
		if(!Character.isUpperCase(orig.charAt(0))) return ;
		for(int i = 1; i < orig.length(); i++) {
			if(!Character.isUpperCase(orig.charAt(i))) {
				holder.addFeature(prefix + "ic") ;
				return ;
			}
		}
		holder.addFeature(prefix + "ac") ;
	}
}