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
package org.vsw.nlp.ml.io;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.Field;
import org.vsw.nlp.doc.Section;
import org.vsw.nlp.ml.crf.sent.CRFSentenceSpliterAnalyzer;
import org.vsw.nlp.token.TabularTokenPrinter;
import org.vsw.nlp.token.TokenCollection;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WTagDocumentReaderUnitTest {
	@Test
	public void test() throws Exception {
		String sample1 = 
			"This:{} đến is:{} the:{} first:{} sentence:{} .:{} This:{} đến is:{} the:{} 2nd:{} sentence:{} .:{}\n" +
			"This:{} đến is:{} the:{} 3rd:{} sentence:{} ...:{} This:{} đến is:{} the:{} 4th:{} sentence:{} .:{}\n" ;
		WTagDocumentReader reader = new WTagDocumentReader() ;
		reader.setSentenceSpliter(new CRFSentenceSpliterAnalyzer(NLPResource.getInstance())) ;
		Document doc = reader.read(sample1) ;
		Field contentField = doc.getField("content") ;
		Assert.assertNotNull(contentField) ;
		List<Section> sections = contentField.getSections() ;
		Assert.assertEquals(2, sections.size()) ;
		
		TabularTokenPrinter printer = new TabularTokenPrinter();
		for(int i = 0; i < sections.size(); i++) {
			Section section = sections.get(i) ;
			TokenCollection[] collection = section.getTokenCollection() ;
			Assert.assertEquals(2, collection.length) ;
			printer.print(System.out, collection) ;
		}
	}
}
