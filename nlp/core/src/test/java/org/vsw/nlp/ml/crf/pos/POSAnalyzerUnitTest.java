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
package org.vsw.nlp.ml.crf.pos;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
import org.vsw.nlp.ml.dict.DictionaryTaggingAnalyzer;
import org.vsw.nlp.token.TabularTokenPrinter;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.ConsoleUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class POSAnalyzerUnitTest {
	@Test
	public void test() throws Exception {
		NLPResource resource = NLPResource.getInstance() ;
		final DictionaryTaggingAnalyzer dictTagger = new DictionaryTaggingAnalyzer(resource);
		final POSTokenAnalyzer posTagger = new POSTokenAnalyzer(resource) ;
		final TokenAnalyzer[] wanalyzer = {dictTagger, posTagger} ;
		DefaultDocumentReader reader = new DefaultDocumentReader(resource) {
			protected void onPostAnalyzeWord(Document doc) throws TokenException { 
				doc.analyze(wanalyzer);
			}
		};
		
		String text = 
			"Nếu như bình thường, ngay sau thời điểm mở cửa giao dịch lúc 9h, " +
			"lượng khách ghé cửa hàng đã đông nghịt, thì sáng nay, đến 10h trưa, " +
			"không khí mua bán tại đây vẫn yên ắng." ;
		String text2 = "Ai là người đầu tiên lên mặt trời" ;
		Document doc = reader.read(text2) ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(ConsoleUtil.getUTF8SuportOutput(), doc.getTokenCollections()) ;
	}
}