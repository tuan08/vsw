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
import org.vsw.nlp.doc.io.DocumentReader;
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
public class EntityTaggerUnitTest {
	@Test
	public void test() throws Exception {
		EntitySetConfig set = EntitySetConfig.getConfig("nlp") ;
		NLPResource resource = NLPResource.getInstance() ;
		DocumentReader reader = EntityTrainer.createEntityTagger(resource) ;
		String text = "chủ tịch nước Nguyễn Tấn Dũng đến thăm và làm việc tại Hà Nội." ;
		IToken[] token = reader.read(text).getTokens() ;
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		printer.print(out, token) ;
	}
}