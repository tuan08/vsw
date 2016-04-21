package org.vsw.nlp.ml.crf.pos;

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.IOB2TokenFeaturesPrinter;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.util.ConsoleUtil;

public class FeatureGeneratorUnitTest {
	static String SAMPLE1 = 
		"Nguyễn Tấn Dũng:{} đến thăm:{} Hà Nội:{} vào:{} ngày:{} 1/1/2011:{} .:{}";
	
	@Test
	public void test() throws Exception {
		NLPResource resource = NLPResource.getInstance() ;
    TokenFeaturesGenerator featuresGenerator = POSTrainer.newTokenFeaturesGenerator(resource) ;
    Document doc = featuresGenerator.getDocumentReader().read(SAMPLE1) ;
    test(featuresGenerator, doc) ;
	}
	
	private void test(TokenFeaturesGenerator featuresGenerator, Document doc) throws Exception {
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		IOB2TokenFeaturesPrinter printer = new IOB2TokenFeaturesPrinter(out, featuresGenerator);
		printer.print(doc, true) ;
	}	
}
