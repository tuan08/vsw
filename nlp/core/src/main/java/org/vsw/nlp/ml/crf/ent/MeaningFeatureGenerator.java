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

import java.util.List;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.DictionaryEntity;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.ml.crf.pos.tag.PosTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.CharacterTag;
import org.vsw.nlp.token.tag.DigitTag;
import org.vsw.nlp.token.tag.EmailTag;
import org.vsw.nlp.token.tag.NumberTag;
import org.vsw.nlp.token.tag.SynsetTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.token.tag.URLTag;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class MeaningFeatureGenerator implements FeatureGenerator {
	static String[] NORMALIZE_TYPES = {
		DigitTag.TYPE, NumberTag.TYPE, CharacterTag.TYPE, URLTag.TYPE, EmailTag.TYPE
	} ;
	
	private DictionaryEntity dictEntity ;
	private String hintTag  ;
	
	
	public MeaningFeatureGenerator(NLPResource resource, EntitySetConfig config) {
		hintTag = "ent:hint:" + config.set ;
		this.dictEntity = resource.getDictionaries().getDictionaryEntity() ;
	}
	
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		addMeaning(token, pos - 3,  "p3:", holder) ;
		addMeaning(token, pos - 2,  "p2:", holder) ;
		addMeaning(token, pos - 1,  "p1:", holder) ;
		addMeaning(token, pos,      "p0:", holder) ;
		addMeaning(token, pos + 1,  "n1:", holder) ;
		addMeaning(token, pos + 2,  "n2:", holder) ;
		addMeaning(token, pos + 3,  "n3:", holder) ;
		
		DictionaryEntity.Entities entities = dictEntity.getEntities(token[pos].getNormalizeForm()) ;
		if(entities != null) {
			for(String selType : entities.getOTypes()) holder.addFeature(selType) ;
		}
	}
	
  void addMeaning(IToken[] token, int pos, String label, FeatureHolder holder) {
		if(pos < 0 || pos >= token.length) return ;
		List<Tag> tags = token[pos].getTagByType(NORMALIZE_TYPES) ;
  	if(tags != null && tags.size() > 0) {
  		for(int i = 0; i < tags.size(); i++) {
  			holder.addFeature(label + tags.get(i).getOType()) ;
  		}
  		return ;
  	}
  	
  	Tag tag = token[pos].getFirstTagType(PosTag.class) ;
		if(tag != null) holder.addFeature(label + tag.getTagValue()) ;
		
  	List<SynsetTag> synsetTag = token[pos].getTagByType(SynsetTag.TYPE) ;
		if(synsetTag != null) {
			for(int i = 0; i < synsetTag.size(); i++) {
				SynsetTag sel = synsetTag.get(i) ;
				for(String selType : sel.getSynset().getType()) {
					if(selType.equals(hintTag)) {
						String name = sel.getSynset().getName() ;
						holder.addFeature(label + name) ;
					}
				}
			}
		}
  	holder.addFeature(label + StringUtil.joinStringArray(token[pos].getWord(), "+")) ;
  }
}