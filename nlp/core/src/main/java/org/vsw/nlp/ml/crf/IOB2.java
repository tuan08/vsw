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

import java.io.FileOutputStream;
import java.io.PrintStream;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.io.DocumentSet;
import org.vsw.nlp.ml.io.WTagDocumentReader;
import org.vsw.util.CommandParser;
import org.vsw.util.ConsoleUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class IOB2 {
	abstract protected TokenFeaturesGenerator createTokenFeaturesGenerator() ;
	
	protected WTagDocumentReader createWTagDocumentReader() throws Exception {
		return new WTagDocumentReader(NLPResource.getInstance()) ;
	}
	
	public void run(String[] args) throws Exception {
		CommandParser command = new CommandParser("iob2:") ;
		command.addMandatoryOption("file", true, "The file or directory, the file extension should be *.tagged") ;
		command.addOption("save", true, "The output model file") ;
		if(!command.parse(args)) return ;
		command.printHelp() ;

		String inFile   = command.getOption("file", null) ;
		String save     = command.getOption("save", null) ;
		
		PrintStream out = null ;
		if(save != null) out = new PrintStream(new FileOutputStream(save), true, "UTF-8") ;
		else out = ConsoleUtil.getUTF8SuportOutput() ;
  	TokenFeaturesGenerator featuresGenerator = createTokenFeaturesGenerator() ;
  	IOB2TokenFeaturesPrinter printer = new IOB2TokenFeaturesPrinter(out, featuresGenerator) ;
  	DocumentSet set = new DocumentSet(inFile, ".*\\.(tagged|wtag)", createWTagDocumentReader()) ;
  	
  	for(int i = 0; i < set.size(); i++) {
  		printer.print(set.getDocument(i), true) ;
  	}
  	if(save != null) out.close() ;
	}
}