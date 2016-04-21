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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
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
public class SentenceSplitterUnitTest {
	CRFSentenceSpliterAnalyzer sSplitter ;
	DefaultDocumentReader reader ;
	@Before
	public void setup() throws Exception {
		NLPResource resource = NLPResource.getInstance() ;
		sSplitter = new CRFSentenceSpliterAnalyzer(resource) ;
		reader = new DefaultDocumentReader(resource) ;
		reader.setSentenceSpliter(sSplitter) ;
		reader.setWordAnalyzer(null);
	}
	
	@Test
	public void test() throws Exception {
		assertSplit("this is sent one. This is sent 2. Mr. Tuan test 3.", 3) ;
		assertSplit("this is N. Huong. Sent 2.", 2) ;
		assertSplit("this is N. Huong! Sent 2.", 2) ;
		assertSplit("this is N. Huong? Sent 2.", 2) ;
		assertSplit("Sent 1... Sent 2.", 2) ;
	}
	
	private void assertSplit(String text, int expect) throws Exception {
		Document doc = reader.read(text) ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		TokenCollection[] collection = doc.getTokenCollections() ;
		printer.print(System.out, collection) ;
		Assert.assertEquals(expect, collection.length) ;
	}
}
