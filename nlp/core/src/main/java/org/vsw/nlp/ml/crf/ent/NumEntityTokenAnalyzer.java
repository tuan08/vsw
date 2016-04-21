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

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.ml.crf.ent.tag.EntityTag;
import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NumEntityTokenAnalyzer extends EntityTokenAnalyzer {
	public NumEntityTokenAnalyzer(NLPResource resource) throws Exception {
		super("nlp/entity.num.crf", EntitySetConfig.getConfig("num"), resource) ;
	}
	
	public NumEntityTokenAnalyzer(String crfModelFile, NLPResource resource) throws Exception {
		super(crfModelFile, EntitySetConfig.getConfig("num"), resource) ;
  }

	protected void setEntityTag(String type, IToken token) {
		token.add(new EntityTag(type, token.getOriginalForm())) ;
	}
}