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

import java.util.ArrayList;
import java.util.List;

import org.vsw.util.CommandParser;
import org.vsw.util.FileUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class CRFReport {
	public void run(String[] args) throws Exception {
		CommandParser command = new CommandParser("crfreport:") ;
    command.addMandatoryOption("sample", true, "The sampple file or directory, the file extension should be iob2 or tagged") ;
    command.addMandatoryOption("model", true, "The output model file") ;
    command.addOption("iteration", true, "The maximum number of iteration, default is 1000") ;
    command.addOption("train", true, "Should train or not, the default value is true") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;
    
    String sample = command.getOption("sample", null) ;
    String save   = command.getOption("model", null) ;
    int iteration = Integer.parseInt(command.getOption("iteration", "1000")); 
    String train = command.getOption("train", "true") ;
    
    String[] files = FileUtil.findFiles(sample, ".*\\.(tagged|wtag)") ;
    List<String> trainFiles = new ArrayList<String>() ;
    List<String> testFiles = new ArrayList<String>() ;
    for(int i = 0; i < files.length; i++) {
    	if(i % 10 == 0) {
    		testFiles.add(files[i]) ;
    	} else {
    		trainFiles.add(files[i]) ;
    	}
    }
    if("true".equals(train)) {
    	train(trainFiles.toArray(new String[trainFiles.size()]), save, iteration) ;
    }
    test(testFiles.toArray(new String[testFiles.size()]), save) ;
	}
	
	abstract protected void train(String[] trainFiles, String save, int iteration) throws Exception ;
	
	abstract protected void test(String[] testFiles, String crfModel) throws Exception ;
}
