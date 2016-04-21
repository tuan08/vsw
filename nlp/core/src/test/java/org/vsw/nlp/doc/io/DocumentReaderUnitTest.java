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
package org.vsw.nlp.doc.io;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
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
public class DocumentReaderUnitTest {
	@Test
	public void test() throws Exception {
		NLPResource resource = NLPResource.getInstance() ;
		final TokenAnalyzer[] wAnalyzer = {
			new DictionaryTaggingAnalyzer(resource), 
			new POSTokenAnalyzer(resource),
			//new NPEntityTokenAnalyzer(dict)
			//new NumEntityTokenAnalyzer(dict)
		};
		final DefaultDocumentReader reader = new DefaultDocumentReader(resource) {
			protected void onPostAnalyzeWord(Document doc) throws TokenException { 
				doc.analyze(wAnalyzer) ;
			}
		};
		String text = 
			"Nếu như bình thường, ngay sau thời điểm mở cửa giao dịch lúc 9h, " +
			"lượng khách ghé cửa hàng đã đông nghịt, thì sáng nay, đến 10h trưa, " +
			"không khí mua bán tại đây vẫn yên ắng. Thậm chí, có nhiều cửa hàng, " +
			"chủ và nhân viên đứng rỗi tán chuyện vì không có khách đến trao đổi, mua bán. " + 
			"Nhân viên cửa hàng vàng bạc ở cuối phố Hà Trung cho biết vẫn chưa buôn bán gì. " +
			"Mọi giao dịch dù là vàng hay ngoại tệ đều ngừng trong ngày hôm nay." ;
		Document doc = reader.read(text) ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(ConsoleUtil.getUTF8SuportOutput(), doc.getTokens()) ;
		
		//PERFORMANCE
		for(int i = 0; i < 25; i++) {
			doc = reader.read(text) ;
		}
		
		long start = System.currentTimeMillis() ;
		int NUMBER = 100 ;
		for(int i = 0; i < NUMBER; i++) {
			doc = reader.read(text) ;
		}
		
		long time = System.currentTimeMillis() - start ;
		System.out.println("Analyze " + NUMBER + " in " + time + "ms");
	}
}
