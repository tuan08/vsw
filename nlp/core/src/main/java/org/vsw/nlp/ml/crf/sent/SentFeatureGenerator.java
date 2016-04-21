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

import java.util.List;

import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.CharacterTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.util.CharacterSet;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class SentFeatureGenerator implements FeatureGenerator {
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		char[] character  = token[pos].getNormalizeFormBuf() ;
		if(character.length == 1 && CharacterSet.isIn(character[0], CharacterSet.END_SENTENCE)) {
			candidate(token, pos, holder) ;
		} else if(character.length == 3 && "...".equals(token[pos].getNormalizeForm())) {	
			candidate(token, pos, holder) ;
		} else {
			holder.addFeature("ignore") ;
		}
	}	
	
	void candidate(IToken[] token, int pos, FeatureHolder holder) {
		holder.addFeature(token[pos].getNormalizeForm()) ;
		addCandidate(token, pos - 2, "token:-2:", holder) ;
		addCandidate(token, pos - 1, "token:-1:", holder) ;
		addCandidate(token, pos    , "token:0:", holder) ;
		addCandidate(token, pos + 1, "token:+1:", holder) ;
		addCandidate(token, pos + 2, "token:+2:", holder) ;
	}
	
	void addCandidate(IToken[] token, int pos, String prefix, FeatureHolder holder) {
		if(pos < 0) return ;
		if(pos > token.length) return ;
		if(pos == token.length) {
			holder.addFeature(prefix + "eof") ;
			return ;
		}
		List<Tag> tags = token[pos].getTag() ;
		if(tags != null) {
			for(int i = 0 ; i < tags.size(); i++) {
				Tag tag = tags.get(i) ;
				if(tag instanceof CharacterTag) {
					addCharFeatures((CharacterTag) tag, prefix, holder) ;
				} else if(tag instanceof BoundaryTag) {
				} else {
					holder.addFeature(prefix + tag.getOType()) ;
				}
			}
		}
	}
	
	static public void addCharFeatures(CharacterTag ctag, String prefix, FeatureHolder holder) {
		for(CharacterTag.CharDescriptor desc : ctag.getCharDescriptors()) {
			char type = desc.character ;
			if(type == 'l' || type == 'V' || type == 'd') {
				holder.addFeature(prefix + "char:" + type)  ;
			} else {
				if (desc.count >= 4) holder.addFeature("char:" + type)  ;
				else holder.addFeature(prefix + "char:" + type + ":" + desc.count)  ;
			}
		}
		if(ctag.getSuffix() != null) {
			holder.addFeature(prefix + "char:suffix:" + ctag.getSuffix()) ;
		}
	}
}