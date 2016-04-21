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

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.IOB2TokenFeaturesPrinter;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.token.TabularTokenPrinter;
import org.vsw.util.ConsoleUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class FeatureGeneratorUnitTest {
	@Test
	public void test() throws Exception {
		TokenFeaturesGenerator featuresGenerator = 
			EntityTrainer.newTokenFeaturesGenerator(NLPResource.getInstance(), EntitySetConfig.getConfig("np")) ;
		
		String text1 = 
			"Nguyễn Tấn Dũng:{per:B} đến thăm:{} thành phố:{} Hà Nội:{loc:B} vào:{} ngày:{} 1/1/2011:{} 123:{} 1.234:{} 123km:{} .:{}";
		
		String text2 = "12.000:{} đồng:{} ,:{} 12.000đồng:{qt:currency:B} ,:{} 12:{} triệu:{}" ;
		Document doc = featuresGenerator.getDocumentReader().read(text1);
		test(featuresGenerator, doc) ;
	}
	
	private void test(TokenFeaturesGenerator featuresGenerator, Document doc) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(out, doc.getTokenCollections()) ;
		IOB2TokenFeaturesPrinter fprinter = new IOB2TokenFeaturesPrinter(out, featuresGenerator) ;
		fprinter.print(doc, true) ;
	}	
}