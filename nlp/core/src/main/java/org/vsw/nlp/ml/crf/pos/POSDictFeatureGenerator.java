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
package org.vsw.nlp.ml.crf.pos;

import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.LexiconTag;
import org.vsw.util.StringUtil;

public class POSDictFeatureGenerator implements FeatureGenerator {
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		//addMeaning(token, pos - 4, "meaning:-4:", holder) ;
		//addMeaning(token, pos - 3, "meaning:-3:", holder) ;
		addMeaning(token, pos - 2, "p2:", holder) ;
		addMeaning(token, pos - 1, "p1:", holder) ;
		addMeaning(token, pos    , "p0:", holder) ;
		//addMeaning(token, pos + 1, "meaning:1:", holder) ;
		//addMeaning(token, pos + 2, "meaning:2:", holder) ;
		//addMeaning(token, pos + 3, "meaning:3:", holder) ;
		//addMeaning(token, pos + 4, "meaning:4:", holder) ;		
	}
	
  void addMeaning(IToken[] token, int pos, String label, FeatureHolder holder) {
    if(pos < 0 || pos >= token.length) return ;   
    LexiconTag ltag = token[pos].getFirstTagType(LexiconTag.class) ;
    if(ltag != null) {
      for (int i = 0; i < ltag.getPosTag().length; i++)
      holder.addFeature(label + ltag.getPosTag()[i]) ;      
    } else {
      return ;
    }
    
    String value = StringUtil.joinStringArray(token[pos].getWord(), "+") ;
    holder.addFeature(label + value) ;
  } 
}