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
package org.vsw.nlp.ml.crf;

import java.io.PrintStream;

import org.junit.Test;
import org.vsw.nlp.ml.WTagTokenizer;
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
public class WTagTokenizerUnitTest {
	@Test
	public void run() throws Exception {
		String sample1 = 
			"Nguyễn Tấn Dũng:{tt:word, ent:vnname, lex:np} đến thăm:{lex:vb} Hà Nội:{ent:loc, lex:np}\n" +
			"vào:{lex:ad} ngày:{lex:nn} 1/1/2011:{tt:date} .:{tt:punc} ...:{} test:{} \n";
		PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
		TabularTokenPrinter printer = new TabularTokenPrinter();
		WTagTokenizer tokenizer = new WTagTokenizer(sample1) ;
		printer.print(out, tokenizer.allTokens()) ;
		out.println("-----------------------------------------------------------") ;
	}
}
