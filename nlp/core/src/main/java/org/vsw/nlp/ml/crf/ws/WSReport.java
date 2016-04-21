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
package org.vsw.nlp.ml.crf.ws;

import org.vsw.nlp.ml.crf.CRFReport;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WSReport extends CRFReport {
	protected void train(String[] trainFiles, String save, int iteration) throws Exception {
		WSTrainer trainer = new WSTrainer() ;
  	trainer.train(trainFiles, save, iteration) ;
	}
	
	protected void test(String[] testFiles, String crfModel) throws Exception {
		WSTester tester = new WSTester() ;
    tester.init(crfModel) ;
    tester.test(testFiles, false) ;
	}
	
	static public void main(String[] args) throws Exception {
		args = new String[] {
		  "-sample", "src/test/resources/tagged",
			"-model",   "target/vnword.crf"
		};
		WSReport report = new WSReport() ;
		report.run(args) ;
	}
}
