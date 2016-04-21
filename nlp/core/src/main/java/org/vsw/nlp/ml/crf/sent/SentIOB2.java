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

import org.vsw.nlp.ml.crf.IOB2;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.io.WTagDocumentReader;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class SentIOB2 extends IOB2 {

	protected WTagDocumentReader createWTagDocumentReader() throws Exception {
		WTagDocumentReader reader = new WTagDocumentReader() ;
		reader.setSentenceSpliter(null) ;
		return reader ;
	}
	
  protected TokenFeaturesGenerator createTokenFeaturesGenerator() {
	  return SentTrainer.newTokenFeaturesGenerator() ;
  }

  static public void main(String[] args) throws Exception {
  	if(args == null || args.length == 0) {
  		args = new String[] {
  			"-file", "d:/vswdata3/nlp/wtag/set1.finish"	
  		};
  	}
  	new SentIOB2().run(args) ;
  }
}
