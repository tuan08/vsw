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

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.TokenFeatures;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.util.ConsoleUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WSFeatureGeneratorUnitTest {
	@Test
	public void test() throws Exception {
		TokenFeaturesGenerator featuresGenerator = WSTrainer.newTokenFeaturesGenerator() ;
		String text1 = 
			//"This:{} is:{} the:{} 1st:{} sentence:{} .:{}\n" +
			"ABC:{} Nguyễn Tấn Dũng:{} đến thăm:{} Hà Nội:{} vào:{} ngày:{} 1/1/2011:{} .:{}";
		String text2 = "Voi:{} về:{} !:{}" ;
		test(featuresGenerator, text1) ;
	}
	
	private void test(TokenFeaturesGenerator featuresGenerator, String text) throws Exception {
		Document doc = featuresGenerator.getDocumentReader().read(text) ;
		TokenCollection[] collection = doc.getTokenCollections() ;
		for(int i = 0; i < collection.length; i++) {
			test(featuresGenerator, collection[i].getTokens()) ;
		}
	}	
	
	private void test(TokenFeaturesGenerator featuresGenerator, IToken[] token) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		TokenFeatures[] tokenFeatures = featuresGenerator.generate(token, true) ;
		for(int i = 0; i < tokenFeatures.length; i++) {
			String[] feature = tokenFeatures[i].getFeatures();
			for(int j = 0; j < feature.length; j++) {
				if(j > 0) out.append(' ') ;
				out.append(feature[j]) ;
			}
			out.append(' ').append(tokenFeatures[i].getTargetFeature()).println() ;
		}
	}
}
