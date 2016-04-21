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

import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.ml.dict.LongestMatchingAnalyzer;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.DateTokenAnalyzer;
import org.vsw.nlp.token.analyzer.EmailTokenAnalyzer;
import org.vsw.nlp.token.analyzer.GroupTokenMergerAnalyzer;
import org.vsw.nlp.token.analyzer.NameTokenAnalyzer;
import org.vsw.nlp.token.analyzer.PunctuationTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TimeTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.USDTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNDTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNMobileTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNNameTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNPhoneTokenAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NLPTestContext {
	private Dictionaries dict ;
	private TokenAnalyzer[] analyzer ;
	
	public NLPTestContext(String[] resource, String[] analyzerName) throws Exception {
		this.dict = new Dictionaries() ;
		if(analyzerName == null) {
			analyzer = new TokenAnalyzer[] {} ;
		} else {
			analyzer = new TokenAnalyzer[analyzerName.length] ;
			for(int i = 0; i < analyzer.length; i++) {
				if("common".equals(analyzerName[i])) {
					analyzer[i] = new CommonTokenAnalyzer() ;
				} else if("date".equals(analyzerName[i])) {
					analyzer[i] = new DateTokenAnalyzer() ;
				} else if("email".equals(analyzerName[i])) {
					analyzer[i] = new EmailTokenAnalyzer() ;
				} else if("GroupTokenMerger".equals(analyzerName[i])) {
					analyzer[i] = new GroupTokenMergerAnalyzer() ;
				} else if("punctuation".equals(analyzerName[i])) {
					analyzer[i] = new PunctuationTokenAnalyzer() ;
				} else if("time".equals(analyzerName[i])) {
					analyzer[i] = new TimeTokenAnalyzer() ;
				} else if("usd".equals(analyzerName[i])) {
					analyzer[i] = new USDTokenAnalyzer() ;
				} else if("vnd".equals(analyzerName[i])) {
					analyzer[i] = new VNDTokenAnalyzer() ;
				} else if("vnmobile".equals(analyzerName[i])) {
					analyzer[i] = new VNMobileTokenAnalyzer() ;
				} else if("vnphone".equals(analyzerName[i])) {
					analyzer[i] = new VNPhoneTokenAnalyzer() ;
				} else if("vnname".equals(analyzerName[i])) {
					analyzer[i] = new VNNameTokenAnalyzer() ;
				} else if("name".equals(analyzerName[i])) {
					analyzer[i] = new NameTokenAnalyzer() ;
				} else if("MaxMatching".equals(analyzerName[i])) {
					analyzer[i] = new LongestMatchingAnalyzer(dict) ;
				} else {
					throw new RuntimeException("Unknow analyzer " + analyzerName[i]) ;
				}
			}
		}
	}
	
	public Dictionaries getDictionaries() { return this.dict ; }
	
	public TokenAnalyzer[] getTokenAnalyzer() { return this.analyzer ; }
}