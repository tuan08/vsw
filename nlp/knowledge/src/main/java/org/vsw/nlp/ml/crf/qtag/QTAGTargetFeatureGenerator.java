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
package org.vsw.nlp.ml.crf.qtag;

import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.ml.crf.ent.EntitySetConfig;
import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class QTAGTargetFeatureGenerator implements FeatureGenerator {
	private String[] tag  ;
	
	public QTAGTargetFeatureGenerator(EntitySetConfig config) {
		tag = config.tagPrefix ;
	}
	
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		BoundaryTag btag = (BoundaryTag)token[pos].getFirstTagType(BoundaryTag.TYPE) ;
		if(btag != null) {
			String[] feature = btag.getFeatures() ;
			if(feature != null && feature.length > 0) {
				for(String sel : feature) {
					for(String selPrefix : tag) {
						if(sel.startsWith(selPrefix)) {
							holder.setTargetFeature(sel) ;
							return ;
						}
					}
				}
			}
		}
		holder.setTargetFeature("O") ;
	}	
}