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

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.IOB2TokenFeaturesPrinter;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
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
		TokenFeaturesGenerator featuresGenerator = SentTrainer.newTokenFeaturesGenerator() ;
		String text2 = "This:{} is:{} sent:{} 1:{} ?:{} " + 
		               "This:{} is:{} sent:{} 2:{} ...:{} " +
		               "this:{} is:{} sent:{} 3:{} .:{}";
		Document doc = featuresGenerator.getDocumentReader().read(text2) ;
		test(featuresGenerator, doc) ;
	}
	
	private void test(TokenFeaturesGenerator featuresGenerator, Document doc) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		IOB2TokenFeaturesPrinter printer = new IOB2TokenFeaturesPrinter(out, featuresGenerator) ;
		printer.print(doc, true) ;
	}	
}
