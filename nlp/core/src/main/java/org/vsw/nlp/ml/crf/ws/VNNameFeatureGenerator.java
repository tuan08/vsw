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

import java.io.IOException;

import org.vsw.nlp.dict.DictionaryVNName;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class VNNameFeatureGenerator implements FeatureGenerator {
	private DictionaryVNName dict ;
	
	public VNNameFeatureGenerator() throws IOException {
		dict = new DictionaryVNName() ;
	}
	
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		char fletter = token[pos].getOriginalForm().charAt(0) ;
		if(Character.isUpperCase(fletter)) {
			String nform = token[pos].getNormalizeForm() ;
			if(dict.containLastName(nform, false )) {
				holder.addFeature("vnname:last") ;
			} 
//			else if(dict.containFirstName(nform, false ) || dict.containMiddleName(nform, false )) {
//				holder.addFeature("vnname:name") ;
//			}
		}
	}
}