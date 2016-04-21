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
package org.vsw.nlp.ml.crf.ent.tag;

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.ml.crf.ent.EntityTrainer;
import org.vsw.nlp.token.IToken;
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
public class CurrencyTagUnitTest {
	@Test
	public void test() throws Exception {
		DocumentReader reader = EntityTrainer.createEntityTagger(NLPResource.getInstance()) ;
		String text = "12.000 đồng, 12.000đồng, 12 triệu" ;
		IToken[] token = reader.read(text).getTokens() ;
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(out, token) ;
	}
}