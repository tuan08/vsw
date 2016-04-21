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
package org.vsw.nlp.test;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NLPTestSuite {
	private String[]  resource ;
	private String[]  analyzer ;
	private NLPTest[] test ;
	
	public String[] getResource() { return this.resource ; }
	public void     setResource(String[] resource) { this.resource = resource ; }
	
	public String[] getAnalyzer() { return analyzer; }
	public void setAnalyzer(String[] analyzer) { this.analyzer = analyzer; }
	
	public NLPTest[] getTest() { return test; }
	public void setTest(NLPTest[] test) { this.test = test; }
	
	public void compile() throws Exception {
		for(NLPTest sel : test) sel.compile() ;
	}
	
	public void run() throws Exception {
		NLPTestContext context = new NLPTestContext(resource, analyzer) ;
		compile() ;
		for(NLPTest sel : test) sel.run(context) ;
	}
}
