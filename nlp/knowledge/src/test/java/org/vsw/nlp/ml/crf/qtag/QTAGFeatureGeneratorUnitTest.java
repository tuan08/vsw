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

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.IOB2TokenFeaturesPrinter;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.ent.EntitySetConfig;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
import org.vsw.nlp.ml.io.WTagDocumentReader;
import org.vsw.util.ConsoleUtil;

public class QTAGFeatureGeneratorUnitTest {
	static String SAMPLE1 = 
		"Ai:{} là:{} người:{} đưa ra:{} thuyết tương đối:{}";
	@Test
	public void test() throws Exception {
	  NLPResource resource = NLPResource.getInstance() ;
    TokenFeaturesGenerator featuresGenerator = 
      QTAGTrainer.newTokenFeaturesGenerator(resource, EntitySetConfig.getConfig("qtag")) ;
    Document doc = new WTagDocumentReader(resource).read(SAMPLE1) ;
    doc.analyze(new POSTokenAnalyzer(resource)) ;
		test(featuresGenerator, doc) ;
	}
	
	private void test(TokenFeaturesGenerator featuresGenerator, Document doc) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		IOB2TokenFeaturesPrinter printer = new IOB2TokenFeaturesPrinter(out, featuresGenerator) ;
		printer.print(doc, true) ;
	}	
}
