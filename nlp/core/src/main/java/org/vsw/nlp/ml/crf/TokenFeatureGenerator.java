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

import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.CharacterTag;
import org.vsw.nlp.token.tag.DigitTag;
import org.vsw.nlp.token.tag.LexiconTag;
import org.vsw.nlp.token.tag.NumberTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.token.tag.WordTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$ 
 * $Date$ 
 * $LastChangedBy$ 
 * $LastChangedDate$
 * $URL$
 **/
public class TokenFeatureGenerator implements FeatureGenerator {
	final static int MAX_SPECIAL_CHAR_COUNT = 4 ;

	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		IToken sel = token[pos] ;
		List<Tag> tags = sel.getTag() ;
		if(tags == null) return ;
		for(int i = 0; i < tags.size(); i++) {
			Tag selTag = tags.get(i) ;
			if(selTag instanceof CharacterTag) {
				addCharFeatures((CharacterTag) selTag, holder) ;
				return ;
			} else if(selTag instanceof DigitTag) {
				holder.addFeature("digit" ) ;
				return ;
			} else if(selTag instanceof NumberTag) {
				holder.addFeature("number") ;
				return ;
			} else if(selTag instanceof LexiconTag) {
				holder.addFeature("lexicon") ;
				return;
			} else if(selTag == WordTag.WLETTER) {
				holder.addFeature("wletter") ;
				return;
			}
		}
	}

	static public void addCharFeatures(CharacterTag ctag, FeatureHolder holder) {
		for(CharacterTag.CharDescriptor desc : ctag.getCharDescriptors()) {
			char type = desc.character ;
			if(type == 'l' || type == 'V' || type == 'd') {
				holder.addFeature("char:" + type)  ;
			} else {
				if (desc.count >= MAX_SPECIAL_CHAR_COUNT) holder.addFeature("char:" + type)  ;
				else holder.addFeature("char:" + type + ":" + desc.count)  ;
			}
		}
		if(ctag.getSuffix() != null) {
			holder.addFeature("char:suffix:" + ctag.getSuffix()) ;
		}
	}
}